plugins {
    id("conventions")
}

dependencies {
    val apacheCommonsVersion: String by rootProject.extra

    implementation("org.apache.commons:commons-lang3:$apacheCommonsVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.assertj:assertj-core:3.24.2")

    implementation(project(":annotation"))
    implementation(project(":core"))
}

