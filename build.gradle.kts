buildscript {
    val kotlinVersion: String by rootProject.extra

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

subprojects {
    repositories {
        mavenCentral()
    }
}

//plugins {
//    id 'java'
//    id 'org.jetbrains.kotlin.jvm' version '1.8.21'
//}
//
//group 'org.kindcensor'
//version '0.0.1'
//
//repositories {
//    mavenCentral()
//}
//
//dependencies {
//    implementation 'org.jetbrains.kotlin:kotlin-stdlib'
//    implementation 'org.apache.commons:commons-lang3:3.12.0'
//
//    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.9.3'
//    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine'
//    testImplementation 'org.assertj:assertj-core:3.24.2'
//}
//
//test {
//    useJUnitPlatform()
//}