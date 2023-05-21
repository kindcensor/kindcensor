package org.kindcensor.ksp.ir

import com.google.devtools.ksp.symbol.KSAnnotation

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