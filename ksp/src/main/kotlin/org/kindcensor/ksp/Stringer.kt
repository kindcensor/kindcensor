package org.kindcensor.ksp

import kotlin.reflect.KClass

@Suppress("unused")
object Stringer {

    private val functions = mutableMapOf<KClass<*>, (Any) -> String>()

    @Suppress("UNCHECKED_CAST")
    @Synchronized
    fun <T : Any> register(clazz: KClass<T>, function: (T) -> String) {
        functions[clazz] = function as (Any) -> String
    }

    fun toString(subject: Any): String = functions[subject::class]?.let { it(subject) }
        ?: subject::class.simpleName
        ?: "undefined"
}