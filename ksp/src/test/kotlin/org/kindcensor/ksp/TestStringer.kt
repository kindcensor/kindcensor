package org.kindcensor.ksp

import kotlin.reflect.KClass

@Suppress("unused")
object TestStringer {

    @Suppress("UNCHECKED_CAST")
    private fun init(classLoader: ClassLoader): Map<KClass<*>, (Any) -> String> {
        val initializerClass = classLoader.loadClass(INITIALIZER_QUALIFIED_CLASS)
        val initializer = initializerClass.constructors[0].newInstance()
        val bindings = initializerClass.getMethod(GET_BINDINGS_METHOD).invoke(initializer) as List<Binding>
        return bindings.associate { it.targetClass to it.toStringFunction }
    }

    fun toString(subject: Any): String {
        val functions = init(subject::class.java.classLoader)
        return functions[subject::class]?.let { it(subject) }
            ?: subject::class.simpleName
            ?: "undefined"
    }
}