package org.kindcensor.annotation.bind

fun interface Binding {

    fun process(subject: Any?): String?

}