package org.kindcensor.annotation

import org.kindcensor.core.DataMasker

/**
 * The annotation to be processed by [org.kindcensor.reflection.MaskedReflectionToStringBuilder]. It will use
 * information from annotation to call [org.kindcensor.core.DataMasker.maskEmail] on field value.
 *
 * @param mask The mask char
 */
annotation class ToStringMaskEmail(
    val mask: Char = DataMasker.DEFAULT_MASK
)
