package org.x27.datamasker.annotation

/**
 * The annotation to be processed by [org.x27.datamasker.MaskedReflectionToStringBuilder]. It will use
 * information from annotation to call [org.x27.datamasker.DataMasker.initial] on field value.
 *
 * @param withPeriodEnding Whether to include '.' symbol in the end of the string
 */
@Target(AnnotationTarget.FIELD)
annotation class ToStringInitial(val withPeriodEnding: Boolean = true)
