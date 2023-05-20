package org.kindcensor.annotation.bind

import org.kindcensor.annotation.ToStringHide
import org.kindcensor.annotation.ToStringInitial
import org.kindcensor.annotation.ToStringMaskBeginning
import org.kindcensor.annotation.ToStringMaskEmail
import org.kindcensor.annotation.ToStringMaskEnding
import org.kindcensor.core.DataMasker

object AnnotationBindingRegistry {

    fun getProcessor(annotation: Annotation): Binding? = when (annotation) {
        is ToStringHide -> Binding {
            DataMasker.hide(it, annotation.preserveLength, annotation.preserveNull, annotation.mask)
        }
        is ToStringInitial -> Binding {
            DataMasker.initial(it, annotation.withPeriodEnding)
        }
        is ToStringMaskBeginning ->  Binding {
            DataMasker.maskBeginning(it, annotation.maxOpen, annotation.minHidden, annotation.mask)
        }
        is ToStringMaskEmail -> Binding {
            DataMasker.maskEmail(it, annotation.mask)
        }
        is ToStringMaskEnding -> Binding {
            DataMasker.maskEnding(it, annotation.maxOpen, annotation.minHidden, annotation.mask)
        }
        else -> null
    }
}