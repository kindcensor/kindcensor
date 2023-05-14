package org.x27.datamasker.annotation

import org.x27.datamasker.DataMasker

/**
 * The annotation to be processed by [org.x27.datamasker.MaskedReflectionToStringBuilder]. It will use
 * information from annotation to call [org.x27.datamasker.DataMasker.maskEmail] on field value.
 *
 * @param mask The mask char
 */
annotation class ToStringMaskEmail(
    val mask: Char = DataMasker.DEFAULT_MASK
)
