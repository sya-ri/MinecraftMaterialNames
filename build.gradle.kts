import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.4.32"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.github.syari.minecraft"
version = "1.0.0"

repositories {
    mavenCentral()
}

val shadowImplementation: Configuration by configurations.creating
configurations["implementation"].extendsFrom(shadowImplementation)

dependencies {
    shadowImplementation(kotlin("stdlib"))
}

tasks.withType<ShadowJar> {
    configurations = listOf(shadowImplementation)
    archiveClassifier.set("")
    minimize()
}

tasks.withType<Jar> {
    manifest {
        attributes(
            "Main-Class" to "com.github.syari.minecraft.materialnames.MainKt"
        )
    }
}
