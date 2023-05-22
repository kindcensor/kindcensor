package org.kindcensor.ksp.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSVisitorVoid
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

    private val shortNames: Set<String>

    private val qualifiedNames: Set<String>

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

    private var done = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // TODO find other way


        val visitor = FindPropertiesVisitor()
        resolver.getAllFiles().forEach { it.accept(visitor, Unit) }
        val classesIR = visitor.classes.map { ClassIR.fromKSP(it, shortNames, qualifiedNames) }
        val sourceFiles = visitor.classes.mapNotNull { it.containingFile }.toSet()
        environment.logger.info("1234567")
        environment.logger.info(visitor.classes.toString())

        if (done) {
            return emptyList()
        }
        done = true

        val result = generator.generate(classesIR)
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
        val outFile = environment.codeGenerator.createNewFile(dependencies, result.packageName, result.fileName)
        outFile.write(result.content.toByteArray())
    }

    inner class FindPropertiesVisitor : KSVisitorVoid() {

        val classes = mutableSetOf<KSClassDeclaration>()

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            // TODO check if toString is present
            // TODO data optional filter by kinds
            val annotationsOnAllProperties = classDeclaration.getAllProperties().flatMap { it.annotations }
            val annotationPresent = annotationsOnAllProperties.any {
                val shortName = it.shortName.getShortName()
                if (shortName !in shortNames) {
                    return@any false
                }
                val qualifiedName = it.annotationType.resolve().declaration.qualifiedName?.asString()
                return@any qualifiedName in qualifiedNames
            }
            if (annotationPresent) {
                classes.add(classDeclaration)
            }
            classDeclaration.declarations
                .forEach {
                    if (it is KSClassDeclaration) {
                        visitClassDeclaration(it, data)
                    }
                }
        }

        override fun visitFile(file: KSFile, data: Unit) = file.declarations.forEach { it.accept(this, Unit) }

    }
}
