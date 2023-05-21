package org.kindcensor.ksp.ir

import com.google.devtools.ksp.symbol.KSClassDeclaration

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

