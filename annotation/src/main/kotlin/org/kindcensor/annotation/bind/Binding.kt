package org.kindcensor.annotation.bind

/**
 * The interface for function that generates string by input object
 * Purely for code generations simplification
 */
@Suppress("unused")
fun interface Binding {

    fun process(subject: Any?): String?

}