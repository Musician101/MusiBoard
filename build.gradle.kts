plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "9.3.0"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.3.1"
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.1"
}

group = "io.musician101"
version = "1.1.0-SNAPSHOT"

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://libraries.minecraft.net/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://jitpack.io")
}

dependencies {
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")
    //TODO waiting for testing to complete before pushing a release
    api("com.github.Musician101.MusiCommand:paper:be49f96ace")
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        dependencies {
            include(dependency("com.github.Musician101.MusiCommand:.*"))
        }

        archiveClassifier = ""
        relocate("io.musician101.musicommand", "io.musician101.musiboard.lib.io.musician101.musicommand")
    }

    runServer {
        minecraftVersion("1.21.11")
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

bukkitPluginYaml {
    main = "io.musician101.musiboard.MusiBoard"
    author = "Musician101"
    apiVersion = "1.21.11"
    foliaSupported = true
}

paperPluginYaml {
    main = "io.musician101.musiboard.MusiBoard"
    author = "Musician101"
    apiVersion = "1.21.11"
    foliaSupported = true
}
