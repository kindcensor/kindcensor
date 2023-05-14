pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
    }

}

rootProject.name = "kindcensor"
include("annotation", "core", "reflection")