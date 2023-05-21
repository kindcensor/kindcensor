package org.kindcensor.ksp

class Options(val logGeneratedCode: Boolean) {
    companion object {
        fun fromEnvironmentOptions(map: Map<String, String>) = Options(
            logGeneratedCode = map["logGeneratedCode"]?.toBoolean() ?: false
        )
    }
}