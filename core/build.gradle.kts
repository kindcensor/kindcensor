plugins {
    id("conventions")
    id("conventions-test")
}

dependencies {
    val apacheCommonsVersion: String by rootProject.extra
    implementation("org.apache.commons:commons-lang3:$apacheCommonsVersion")
}

