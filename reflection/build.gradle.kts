plugins {
    id("conventions")
    id("conventions-test")
}

dependencies {
    implementation(project(":annotation"))
    implementation("org.apache.commons:commons-lang3")
}

publishing {
    publications {
        register("reflection", MavenPublication::class) {
            from(components["java"])
            artifactId = "reflection"
        }
    }
}