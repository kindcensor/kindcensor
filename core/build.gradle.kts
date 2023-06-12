plugins {
    id("conventions")
    id("conventions-test")
}

dependencies {
    testImplementation("org.apache.commons:commons-lang3")
}

publishing {
    publications {
        register("core", MavenPublication::class) {
            from(components["java"])
            artifactId = "core"
        }
    }
}
