package org.kindcensor.ksp

import kotlin.reflect.KClass

@Suppress("unused")
object Stringer {

    private val functions : Map<KClass<*>, (Any) -> String> by lazy { init() }

    @Suppress("UNCHECKED_CAST")
    private fun init():  Map<KClass<*>, (Any) -> String> {
        val initializerClass = this::class.java.classLoader.loadClass("org.kindcensor.ksp.generated.Initializer")
        val initializer = initializerClass.constructors[0].newInstance()
        val bindings = initializerClass.getMethod("getBindings").invoke(initializer) as List<Binding>
        return bindings.associate { it.targetClass to it.toStringFunction }
    }

    fun toString(subject: Any): String {
        return functions[subject::class]?.let { it(subject) }
            ?: subject::class.simpleName
            ?: "undefined"
    }
}