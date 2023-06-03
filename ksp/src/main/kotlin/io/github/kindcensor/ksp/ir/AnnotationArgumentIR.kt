package io.github.kindcensor.ksp.ir

/**
 * The intermediate representation of annotation argument
 * @property name The of the argument
 * @property value The value of the argument
 */
internal data class AnnotationArgumentIR(val name: String, val value: Any?)