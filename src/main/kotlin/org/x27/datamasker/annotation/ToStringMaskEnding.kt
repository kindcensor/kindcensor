package org.x27.datamasker.annotation

import org.x27.datamasker.DataMasker

/**
 * The annotation to be processed by [org.x27.datamasker.MaskedReflectionToStringBuilder]. It will use
 * information from annotation to call [org.x27.datamasker.DataMasker.maskEnding] on field value.
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
