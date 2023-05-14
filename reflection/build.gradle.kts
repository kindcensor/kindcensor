import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

dependencies {
    val apacheCommonsVersion: String by rootProject.extra

    implementation("org.apache.commons:commons-lang3:$apacheCommonsVersion")

    implementation(project(":annotation"))
    implementation(project(":core"))
}