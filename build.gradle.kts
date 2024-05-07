buildscript {
    configurations {
        classpath {
            resolutionStrategy {
                force("org.ow2.asm:asm:9.6")
                force("org.ow2.asm:asm-commons:9.6")
            }
        }
    }
}

plugins {
    java
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow")
}

group = "io.musician101"
version = "1.1.0-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://libraries.minecraft.net/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")
    compileOnlyApi("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    api("com.github.Musician101:Bukkitier:2.0.0")
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    shadowJar {
        dependencies {
            include(dependency("com.github.Musician101:"))
        }

        archiveClassifier.set("")
        relocate("io.musician101.bukkitier", "io.musician101.musiboard.lib.io.musician101.bukkitier")
        dependsOn("build")
    }

    register<Copy>("prepTestJar") {
        dependsOn("shadowJar")
        from("build/libs/${project.name}-${project.version}.jar")
        into("server/plugins")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "${project.group}"
            artifactId = project.name.lowercase()
            version = "${project.version}"

            from(components["java"])
        }
    }
}
