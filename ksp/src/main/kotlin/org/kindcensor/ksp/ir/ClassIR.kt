package org.kindcensor.ksp.ir

import com.google.devtools.ksp.symbol.KSClassDeclaration

/**
 * The intermediate representation of class
 * @property simpleName The short name of class
 * @property qualifier The qualifier (package) of the class
 * @property properties The properties of the class
 */
internal data class ClassIR(
    val simpleName: String,
    val qualifier: String,
    val properties: List<PropertyIR>
) {
    val qualifiedName = "$qualifier.$simpleName"

    companion object {

        /**
         * Generate [ClassIR] from KSP [KSClassDeclaration]
         * @param declaration The source object
         * @param shortNames Short names of processed annotations
         * @param qualifiedNames Qualified names of processed annotations
         * @return Generated IR
         */
        fun fromKSP(
            declaration: KSClassDeclaration,
            shortNames: Set<String>,
            qualifiedNames: Set<String>
        ) = ClassIR(
            simpleName = declaration.simpleName.getShortName(),
            qualifier = declaration.qualifiedName?.getQualifier() ?: error("Failed to get for $declaration"),
            properties = declaration.getAllProperties()
                .map { PropertyIR.fromKSP(it, shortNames, qualifiedNames) }
                .toList()
        )

    }
}

