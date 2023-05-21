package org.kindcensor.ksp.ir

import com.google.devtools.ksp.symbol.KSPropertyDeclaration

/**
 * The intermediate representation of property
 * @property name The name of the property
 * @property annotation The processed annotation on property if any
 */
data class PropertyIR(val name: String, val annotation: AnnotationIR?) {

    companion object {

        /**
         * Generate [PropertyIR] from KSP [KSPropertyDeclaration]
         * @param declaration The source object
         * @param shortNames Short names of processed annotations
         * @param qualifiedNames Qualified names of processed annotations
         * @return Generated IR
         */
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