package org.kindcensor.annotation.bind

import org.kindcensor.annotation.ToStringHide
import org.kindcensor.annotation.ToStringInitial
import org.kindcensor.annotation.ToStringMaskBeginning
import org.kindcensor.annotation.ToStringMaskEmail
import org.kindcensor.annotation.ToStringMaskEnding
import org.kindcensor.core.DataMasker
import kotlin.reflect.KClass

object AnnotationRegistry {

    val all: List<KClass<out Annotation>> = listOf(
        ToStringHide::class,
        ToStringInitial::class,
        ToStringInitial::class,
        ToStringMaskBeginning::class,
        ToStringMaskEmail::class,
        ToStringMaskEnding::class
    )

    val bySimpleNames: Map<String, KClass<out Annotation>> = all.associateBy {
        it.simpleName ?: error("Failed to get simpleName for $it")
    }

    fun apply(annotation: Annotation, subject: Any?): String? = when (annotation) {
        is ToStringHide -> DataMasker.hide(subject, annotation.preserveLength, annotation.preserveNull, annotation.mask)
        is ToStringInitial -> DataMasker.initial(subject, annotation.withPeriodEnding)
        is ToStringMaskBeginning -> DataMasker.maskBeginning(subject, annotation.maxOpen, annotation.minHidden, annotation.mask)
        is ToStringMaskEmail ->  DataMasker.maskEmail(subject, annotation.mask)
        is ToStringMaskEnding -> DataMasker.maskEnding(subject, annotation.maxOpen, annotation.minHidden, annotation.mask)
        else -> null
    }
}