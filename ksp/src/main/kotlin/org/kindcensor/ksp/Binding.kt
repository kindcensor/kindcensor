package org.kindcensor.ksp

import kotlin.reflect.KClass

/**
 * Binding of target class to some toString function
 * @param targetClass The target class
 * @param toStringFunction The function
 */
data class Binding(val targetClass: KClass<*>, val toStringFunction: (Any) -> String)
