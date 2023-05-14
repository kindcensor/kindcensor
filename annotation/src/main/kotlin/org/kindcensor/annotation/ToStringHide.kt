package org.kindcensor.annotation

import org.kindcensor.core.DataMasker
import org.kindcensor.core.DataMasker.DEFAULT_HIDDEN_LENGTH

/**
 * The annotation to be processed by [org.kindcensor.reflection.MaskedReflectionToStringBuilder]. It will use
 * information from annotation to call [org.kindcensor.core.DataMasker.hide] on field value.
 *
 * @param preserveLength Whether to save length of original [toString] result or replace with fixed
 * [DEFAULT_HIDDEN_LENGTH] length
 * @param preserveNull Whether to return `null` if subject is null or replace it with default string with
 * [DEFAULT_HIDDEN_LENGTH] length
 * @param mask The mask char
 */
@Target(AnnotationTarget.FIELD)
annotation class ToStringHide(
    val preserveLength: Boolean = false,
    val preserveNull: Boolean = true,
    val mask: Char = DataMasker.DEFAULT_MASK
)