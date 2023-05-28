package org.kindcensor.ksp

import kotlin.reflect.KClass

/**
 * The object to perform actual conversion to string
 */
@Suppress("unused")
object Stringer {

    private val functions : Map<KClass<*>, (Any) -> String> by lazy { init() }

    @Suppress("UNCHECKED_CAST")
    private fun init():  Map<KClass<*>, (Any) -> String> {
        val initializerClass = this::class.java.classLoader.loadClass(INITIALIZER_QUALIFIED_CLASS)
        val initializer = initializerClass.constructors[0].newInstance()
        val bindings = initializerClass.getMethod(GET_BINDINGS_METHOD).invoke(initializer) as List<Binding>
        return bindings.associate { it.targetClass to it.toStringFunction }
    }

    /**
     * Convert subject to string. Will return the subject::class.simpleName if no function registered for subject class
     * @param subject The subject of conversion
     * @return Result string
     */
    fun toString(subject: Any): String {
        return functions[subject::class]?.let { it(subject) }
            ?: subject::class.simpleName
            ?: "undefined"
    }
}