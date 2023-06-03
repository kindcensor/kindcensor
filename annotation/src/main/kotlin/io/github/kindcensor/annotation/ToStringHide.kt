package io.github.kindcensor.annotation

import io.github.kindcensor.core.DataMasker
import io.github.kindcensor.core.DataMasker.DEFAULT_HIDDEN_LENGTH

/**
 * The annotation to be processed by [io.github.kindcensor.reflection.MaskedReflectionToStringBuilder]. It will use
 * information from annotation to call [io.github.kindcensor.core.DataMasker.hide] on field value.
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
