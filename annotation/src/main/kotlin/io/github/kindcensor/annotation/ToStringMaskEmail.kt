package io.github.kindcensor.annotation

import io.github.kindcensor.core.DataMasker

/**
 * The annotation to be processed by [io.github.kindcensor.reflection.MaskedReflectionToStringBuilder]. It will use
 * information from annotation to call [io.github.kindcensor.core.DataMasker.maskEmail] on field value.
 *
 * @param mask The mask char
 */
annotation class ToStringMaskEmail(
    val mask: Char = DataMasker.DEFAULT_MASK
)
