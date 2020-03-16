import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "me.tongfei"
version = "0.8.1"
description = "A terminal-based progress bar for JVM"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jline:jline:3.13.3")
    implementation("org.jline:jline-terminal-jansi:3.13.3")

    testImplementation("junit:junit:4.13")
    testImplementation("org.slf4j:slf4j-api:1.7.30")
    testImplementation("org.slf4j:slf4j-simple:1.7.30")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("with-deps")
}
tasks.named("build") {
    dependsOn("shadowJar")
}

publishing {
    publications {
        create<MavenPublication>("SonaType") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("http://github.com/ctongfei/progressbar")

                licenses {
                    license {
                        name.set("MIT")
                        url.set("http://opensource.org/licenses/MIT")
                        distribution.set("repo")
                    }
                }
                scm {
                    url.set("git@github.com:ctongfei/progressbar.git")
                    connection.set("scm:git:git@github.com:ctongfei/progressbar.git")
                }
                developers {
                    developer {
                        id.set("ctongfei")
                        name.set("Tongfei Chen")
                        url.set("https://tongfei.me/")
                    }
                    developer {
                        id.set("bwittwer")
                        name.set("Bernard Wittwer")
                    }
                    developer {
                        id.set("AluisioASG")
                        name.set("Aluísio Augusto Silva Gonçalves")
                    }
                    developer {
                        id.set("neopaf")
                        name.set("Alexander Petrossian")
                    }
                    developer {
                        id.set("dani909")
                        name.set("Daniel Huber")
                    }
                    developer {
                        id.set("khmarbaise")
                        name.set("Karl Heinz Marbaise")
                    }
                    developer {
                        id.set("AbhinavVishak")
                        name.set("Abhinav Vishak")
                    }
                    developer {
                        id.set("wfxr")
                        name.set("Wenxuan")
                    }
                    developer {
                        id.set("meawoppl")
                        name.set("Matty G")
                    }
                    developer {
                        id.set("alexpeelman")
                        name.set("Alex Peelman")
                    }
                    developer {
                        id.set("kristofarkas")
                        name.set("Kristof Farkas-Pall")
                    }
                    developer {
                        id.set("mordechaim")
                        name.set("Mordechai Meisels")
                    }
                }
            }

            from(components["java"])
            artifact(tasks["shadowJar"])
        }

        repositories {
            maven {
                name = "ossrh"

                val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            }
        }
    }
}

signing {
    sign(publishing.publications["SonaType"])
}
