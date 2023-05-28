package org.kindcensor.ksp

/**
 * The options for processor
 * @property logGeneratedCode will log all generated kotlin code if true
 */
internal data class Options(val logGeneratedCode: Boolean) {
    companion object {
        fun fromEnvironmentOptions(map: Map<String, String>) = Options(
            logGeneratedCode = map["logGeneratedCode"]?.toBoolean() ?: false
        )
    }
}