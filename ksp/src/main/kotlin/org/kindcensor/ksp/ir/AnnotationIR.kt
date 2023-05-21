package org.kindcensor.ksp.ir

import com.google.devtools.ksp.symbol.KSAnnotation

/**
 * The intermediate representation of annotation
 * @property simpleName The short name of annotation class
 * @property arguments The annotation arguments
 */
data class AnnotationIR(val simpleName: String, val arguments: List<AnnotationArgumentIR>) {

    companion object {

        /**
         * Generate [Annotation] from KSP [KSAnnotation]
         * @param annotation The source object
         * @return Generated IR
         */
        fun fromKSP(annotation: KSAnnotation): AnnotationIR = AnnotationIR(
            simpleName = annotation.shortName.getShortName(),
            arguments = annotation.arguments.map {
                val name = it.name?.getShortName() ?: error("Failed to get name for argument $it")
                AnnotationArgumentIR(name, it.value)
            }
        )
    }
}