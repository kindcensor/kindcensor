package io.github.kindcensor.annotation

import io.github.kindcensor.core.DataMasker

/**
 * The annotation to be processed by [io.github.kindcensor.reflection.MaskedReflectionToStringBuilder]. It will use
 * information from annotation to call [io.github.kindcensor.core.DataMasker.maskEnding] on field value.
 *
 * @param maxOpen The maximum number of symbols to open
 * @param minHidden The minimum number of hidden symbols
 * @param mask The mask char
 */
annotation class ToStringMaskEnding(
    val maxOpen: Int = DataMasker.DEFAULT_MAX_OPEN,
    val minHidden: Int = DataMasker.DEFAULT_MIN_HIDDEN,
    val mask: Char = DataMasker.DEFAULT_MASK
)
