plugins {
    id("conventions")
    id("conventions-test")
}

dependencies {
    val compileTestingVersion = "1.5.0"

    implementation(project(":annotation"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.21-1.0.11")
    implementation("com.squareup:kotlinpoet:1.13.2")

    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:$compileTestingVersion")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:$compileTestingVersion")
}

publishing {
    publications {
        register("ksp", MavenPublication::class) {
            from(components["java"])
            artifactId = "ksp"
        }
    }
}
