plugins {
    id("conventions")
    id("conventions-test")
}

dependencies {
    implementation(project(":annotation"))
    implementation(project(":core"))
    implementation("org.apache.commons:commons-lang3")
}

