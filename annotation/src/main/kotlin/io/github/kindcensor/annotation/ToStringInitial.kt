package io.github.kindcensor.annotation

/**
 * The annotation to be processed by [io.github.kindcensor.reflection.MaskedReflectionToStringBuilder]. It will use
 * information from annotation to call [io.github.kindcensor.core.DataMasker.initial] on field value.
 *
 * @param withPeriodEnding Whether to include '.' symbol in the end of the string
 */
@Target(AnnotationTarget.FIELD)
annotation class ToStringInitial(val withPeriodEnding: Boolean = true)
