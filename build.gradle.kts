plugins {
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
}

repositories {
    mavenCentral()
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                pom {
                    name.set("kindcensor")
                    //  description.set("")
                    licenses {
                        license {
                            name.set("Apache-2.0")
                            url.set("https://opensource.org/licenses/Apache-2.0")
                        }
                    }
                    url.set("https://github.com/kindcensor/kindcensor")
                    issueManagement {
                        system.set("Github")
                        url.set("https://github.com/kindcensor/kindcensor/issues")
                    }
                    scm {
                        connection.set("https://github.com/kindcensor/kindcensor.git")
                        url.set("https://github.com/kindcensor/kindcensor")
                    }
                    developers {
                        developer {
                            name.set("Aleksandr Borovkov")
                            email.set("xander27b@gmail.com")
                        }
                    }
                }
            }


        }

    }

//    configure<SigningExtension> {
//        sign(publishing.publications["mavenJava"])
//    }
}

nexusPublishing {
    this.repositories {
        this.sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}