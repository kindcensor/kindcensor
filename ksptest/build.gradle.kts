plugins {
    id("conventions")
    id("conventions-test")
    id("com.google.devtools.ksp") version "1.8.21-1.0.11" // TODO single source of ksp version
}

dependencies {
    testImplementation(project(":ksp"))
    testImplementation(project(":annotation"))
    ksp(project(":ksp"))
}

configure<org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension> {
    sourceSets {
        getByName("test") {
            kotlin { srcDirs("build/generated/ksp/test/kotlin") }
        }
    }
}