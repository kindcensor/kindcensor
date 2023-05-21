package org.kindcensor.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

data class ClassIR(
    val simpleName: String,
    val qualifier: String,
    val properties: List<PropertyIR>
) {
    val qualifiedName = "$qualifier.$simpleName"

    companion object {
        fun fromKSP(
            declaration: KSClassDeclaration,
            shortNames: Set<String>,
            qualifiedNames: Set<String>
        ) = ClassIR(
            simpleName = declaration.simpleName.getShortName(),
            qualifier = declaration.qualifiedName?.getQualifier()?: error("Failed to get for $declaration"),
            properties = declaration.getAllProperties()
                .map { PropertyIR.fromKSP(it, shortNames, qualifiedNames) }
                .toList()
        )

    }
}

data class PropertyIR(val name: String, val annotation: AnnotationIR?) {
    companion object {
        fun fromKSP(
            declaration: KSPropertyDeclaration,
            shortNames: Set<String>,
            qualifiedNames: Set<String>
        ): PropertyIR {
            val annotation = declaration.annotations.firstOrNull {
                val shortName = it.shortName.getShortName()
                if (shortName !in shortNames) {
                    return@firstOrNull false
                }
                val qualifiedName = it.annotationType.resolve().declaration.qualifiedName?.asString()
                return@firstOrNull qualifiedName in qualifiedNames
            }
            return PropertyIR(
                name = declaration.simpleName.getShortName(),
                annotation = annotation?.let { AnnotationIR.fromKSP(it) }
            )
        }
    }
}

data class AnnotationIR(val simpleName: String, val arguments: List<ArgumentIR>) {
    companion object {
        fun fromKSP(declaration: KSAnnotation): AnnotationIR = AnnotationIR(
            simpleName = declaration.shortName.getShortName(),
            arguments = declaration.arguments.map {
                val name = it.name?.getShortName() ?: error("Failed to get name for argument $it")
                ArgumentIR(name, it.value)
            }
        )
    }
}

data class ArgumentIR(val name: String, val value: Any?)
