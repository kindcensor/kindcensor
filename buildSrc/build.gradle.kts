plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    val kotlinVersion: String by rootProject.extra

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}
