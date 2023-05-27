package org.kindcensor.ksp.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.ClassKind.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSVisitorVoid
import org.kindcensor.annotation.GenerateToString
import org.kindcensor.annotation.bind.AnnotationRegistry
import org.kindcensor.ksp.Options
import org.kindcensor.ksp.generator.Generator
import org.kindcensor.ksp.generator.GeneratorResult
import org.kindcensor.ksp.ir.ClassIR

internal class Processor(
    private val environment: SymbolProcessorEnvironment,
    private val options: Options,
    private val generator: Generator
) : SymbolProcessor {

    private val targetAnnotationName = GenerateToString::class.qualifiedName
        ?: error("Failed to get target annotation name for ${GenerateToString::class}")

    private val shortNames: Set<String>

    private val qualifiedNames: Set<String>

    private val classesIR: MutableList<ClassIR> = mutableListOf()

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
            .mapNotNull {
                if(it is KSClassDeclaration && (it.classKind == CLASS || it.classKind == ENUM_CLASS)) {
                    it
                } else {
                    null
                }
            }
            .toList()

        classesIR += classes.map { ClassIR.fromKSP(it, shortNames, qualifiedNames) }
        val sourceFiles = classes.mapNotNull { it.containingFile }.toSet()

        val result = generator.generate(classesIR.asSequence())
        if (result != null) {
            if (options.logGeneratedCode) {
                environment.logger.info(result.content)
            }
            passGeneratedCodeToEnvironment(result, sourceFiles)
        }

        return emptyList()
    }

    private fun passGeneratedCodeToEnvironment(result: GeneratorResult, sourceFiles: Set<KSFile>) {
        val dependencies = Dependencies(false, *sourceFiles.toTypedArray())
        val outputStream = try {
            environment.codeGenerator.createNewFile(dependencies, result.packageName, result.fileName)
        } catch (e: FileAlreadyExistsException) {
            e.file.outputStream()
        }
        outputStream.use { it.write(result.content.toByteArray()) }
    }

}
