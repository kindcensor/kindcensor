package org.kindcensor.ksp

import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.KClass

@Suppress("unused")
object Stringer {

    private val initialized = AtomicBoolean(false)

    private var initializer: Any? = null

    private fun init() {
        val oldValue = initialized.getAndSet(true)
        if (oldValue) {
            return
        }
        try {
            val initializerClass = this::class.java.classLoader.loadClass("org.kindcensor.ksp.generated.Initializer")
            initializerClass.constructors[0].newInstance()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace() // TODO slfrj
        }
    }

    private val functions = mutableMapOf<KClass<*>, (Any) -> String>()

    @Suppress("UNCHECKED_CAST")
    @Synchronized
    fun <T : Any> register(clazz: KClass<T>, function: (T) -> String) {
        println("register $clazz, $function")
        functions[clazz] = function as (Any) -> String
    }

    fun toString(subject: Any): String {
        init()
        return functions[subject::class]?.let { it(subject) }
            ?: subject::class.simpleName
            ?: "undefined"
    }
}