import org.gradle.internal.declarativedsl.parsing.main

plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
    id("org.gradle.maven-publish")
}

allprojects {

    group = "dev.wuason"
    version = "1.0"

    apply(plugin = "java")

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://jitpack.io")
        maven("https://repo.nexomc.com/snapshots/")
        maven("https://repo.nexomc.com/releases/")
        maven("https://repo.oraxen.com/snapshots/")
        maven("https://repo.oraxen.com/releases/")
        maven("https://nexus.phoenixdevt.fr/repository/maven-public/")
        maven("https://mvn.lumine.io/repository/maven-public/")
    }

}

dependencies {
    implementation(project(":common")) //Common
    implementation(project(":oraxen2")) //Oraxen 2
    implementation(project(":bukkit")) //Bukkit
    implementation("org.jetbrains:annotations:24.1.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
    dependsOn(tasks.javadoc)
}

tasks {

    javadoc {
        setDestinationDir(file("build/javadoc"))
        val submoduleSources = subprojects.flatMap { subproject ->
            subproject.sourceSets["main"].allJava.srcDirs
        }.filter { it.exists() }
        source(submoduleSources)
        val submoduleClasspath = subprojects
            .flatMap { it.sourceSets["main"].compileClasspath }
        classpath = files(submoduleClasspath)
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveBaseName.set("Adapter")
        archiveClassifier.set("")
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                groupId = rootProject.group.toString()
                artifactId = rootProject.name
                version = rootProject.version.toString()
                artifact(shadowJar)
                artifact(javadoc)
            }
        }
    }
}