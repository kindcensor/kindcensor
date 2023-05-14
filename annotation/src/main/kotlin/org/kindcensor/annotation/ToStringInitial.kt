package org.kindcensor.annotation

/**
 * The annotation to be processed by [org.kindcensor.reflection.MaskedReflectionToStringBuilder]. It will use
 * information from annotation to call [org.kindcensor.core.DataMasker.initial] on field value.
 *
 * @param withPeriodEnding Whether to include '.' symbol in the end of the string
 */
@Target(AnnotationTarget.FIELD)
annotation class ToStringInitial(val withPeriodEnding: Boolean = true)
