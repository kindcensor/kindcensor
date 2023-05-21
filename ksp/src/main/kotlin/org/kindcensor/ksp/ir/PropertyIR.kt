package org.kindcensor.ksp.ir

import com.google.devtools.ksp.symbol.KSPropertyDeclaration

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