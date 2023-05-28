package org.kindcensor.ksp.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import org.kindcensor.annotation.GenerateToString
import org.kindcensor.annotation.bind.AnnotationRegistry
import org.kindcensor.ksp.Options
import org.kindcensor.ksp.generator.OutputGenerator
import org.kindcensor.ksp.generator.OutputGeneratorResult
import org.kindcensor.ksp.ir.ClassIR

/**
 * The implementation of processor for toString logic
 */
internal class Processor(
    private val environment: SymbolProcessorEnvironment,
    private val options: Options,
    private val outputGenerator: OutputGenerator
) : SymbolProcessor {

    private val targetAnnotationName = GenerateToString::class.qualifiedName
        ?: error("Failed to get target annotation name for ${GenerateToString::class}")

    private val shortNames: Set<String>

    private val qualifiedNames: Set<String>

    private val classesIR = mutableListOf<ClassIR>()

    private val sourceFiles = mutableSetOf<KSFile>()

    init {
        val shortNames = mutableSetOf<String>()
        val qualifiedNames = mutableSetOf<String>()

        AnnotationRegistry.all.forEach {
            shortNames.add(it.simpleName ?: error("Failed to get simple name for $it"))
            qualifiedNames.add(it.qualifiedName ?: error("Failed to get qualified name for $it"))
        }

        this.shortNames = shortNames
        this.qualifiedNames = qualifiedNames
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val classes = resolver.getSymbolsWithAnnotation(targetAnnotationName, inDepth = true)
            .mapNotNull { asTargetClassDeclarationOrNull(it) }

        classesIR += classes.map { ClassIR.fromKSP(it, shortNames, qualifiedNames) }
        sourceFiles += classes.mapNotNull { it.containingFile }

        val result = outputGenerator.generate(classesIR)
        if (result != null) {
            if (options.logGeneratedCode) {
                environment.logger.info(result.content)
            }
            writeResult(result, sourceFiles)
        }

        return emptyList()
    }

    private fun asTargetClassDeclarationOrNull(annotated: KSAnnotated): KSClassDeclaration? {
        if (annotated !is KSClassDeclaration) {
            return null
        }
        if (!(annotated.classKind == CLASS || annotated.classKind == ENUM_CLASS)) {
            return null
        }
        return annotated
    }

    private fun writeResult(result: OutputGeneratorResult, sourceFiles: Set<KSFile>) {
        val dependencies = Dependencies(false, *sourceFiles.toTypedArray())
        val outputStream = try {
            environment.codeGenerator.createNewFile(dependencies, result.packageName, result.fileName)
        } catch (e: FileAlreadyExistsException) {
            e.file.outputStream()
        }
        outputStream.use { it.write(result.content.toByteArray()) }
    }

}
