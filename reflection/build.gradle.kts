plugins {
    id("conventions")
    id("conventions-test")
}

dependencies {
    implementation(project(":annotation"))
    implementation("org.apache.commons:commons-lang3")
}

