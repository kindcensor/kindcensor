package org.kindcensor.ksp

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.FileSpec
import org.kindcensor.annotation.bind.AnnotationRegistry
import kotlin.reflect.KClass

internal class Processor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private var done = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // TODO find other way
        if(done) {
            return emptyList()
        }
        done = true

        val visitor = FindPropertiesVisitor(AnnotationRegistry.all)
        resolver.getAllFiles().forEach { it.accept(visitor, Unit) }
        val propertiesToAnnotations = visitor.propertiesToAnnotations
        if (propertiesToAnnotations.isEmpty()) {
            return emptyList()
        }

        val byClasses = propertiesToAnnotations.groupBy { (property, _) ->
            property.parentDeclaration ?: error("No parentDeclaration for $property")
        }
        val sourceFiles = byClasses.keys.mapNotNull { it.containingFile }.toSet()
        val functionsFile: FileSpec = generateToStringFunctionsFile(byClasses)
        passGeneratedCodeToEnvironment(functionsFile, sourceFiles)

        return emptyList()
    }

    private fun passGeneratedCodeToEnvironment(fileSpec: FileSpec, sourceFiles: Set<KSFile>) {
        val dependencies = Dependencies(false, *sourceFiles.toTypedArray())
        val outFile = environment.codeGenerator.createNewFile(dependencies, fileSpec.packageName, fileSpec.name)
        outFile.write(fileSpec.toString().toByteArray())
    }

    inner class FindPropertiesVisitor(annotations: List<KClass<out Annotation>>) : KSVisitorVoid() {
        private val shortNames: Set<String>

        private val qualifiedNames: Set<String>

        init {
            val shortNames = mutableSetOf<String>()
            val qualifiedNames = mutableSetOf<String>()

            annotations.forEach {
                shortNames.add(it.simpleName ?: error("Failed to get simple name for $it"))
                qualifiedNames.add(it.qualifiedName ?: error("Failed to get qualified name for $it"))
            }

            this.shortNames = shortNames
            this.qualifiedNames = qualifiedNames
        }

        val propertiesToAnnotations = mutableListOf<Pair<KSPropertyDeclaration, KSAnnotation>>()

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) =
        // TODO check if toString is present
            // TODO data optional filter by kinds
            classDeclaration.getAllProperties().forEach { it.accept(this, Unit) }

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
            val annotation = property.annotations.firstOrNull {
                val shortName = it.shortName.getShortName()
                if (shortName !in shortNames) {
                    return@firstOrNull false
                }
                val qualifiedName = it.annotationType.resolve().declaration.qualifiedName?.asString()
                return@firstOrNull qualifiedName in qualifiedNames
            }
            if (annotation != null) {
                propertiesToAnnotations.add(property to annotation)
            }
        }

        override fun visitFile(file: KSFile, data: Unit) = file.declarations.forEach { it.accept(this, Unit) }

    }
}
