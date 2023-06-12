plugins {
    id("conventions")
}

dependencies {
    implementation(project(":core"))
}

publishing {
    publications {
        register("annotation", MavenPublication::class) {
            from(components["java"])
            artifactId = "annotation"
        }
    }
}