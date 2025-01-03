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
        dependsOn(shadowJar)

        setDestinationDir(file("build/javadoc"))

        val modules = subprojects.flatMap { subproject ->
            subproject.sourceSets["main"].allJava.srcDirs
        }.filter { it.exists() }

        val submoduleClasspath = subprojects
            .flatMap { it.sourceSets["main"].compileClasspath }

        source(modules)
        classpath = files(submoduleClasspath)
    }

    build {
        dependsOn(javadoc)
    }

    shadowJar {
        archiveBaseName.set("Adapter")
        archiveClassifier.set("")
    }

    withType<PublishToMavenLocal> {
        dependsOn(jar)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            artifactId = rootProject.name
            version = rootProject.version.toString()
            artifact(tasks.shadowJar)
            artifact(tasks.getByName("javadocJar"))

            pom {
                withXml {
                    asNode().appendNode("dependencies").appendNode("dependency").apply {
                        appendNode("groupId", "org.jetbrains")
                        appendNode("artifactId", "annotations")
                        appendNode("version", "24.1.0")
                        appendNode("scope", "provided")
                    }
                }
            }
        }
    }
}

val file = file("readme.md")
gradle.projectsEvaluated {
    val content = file.readText()
    val newContent = content.replace(Regex("(?<=Adapter/)(.*?)(?=/javadoc)"), "${project.version}")
    file.writeText(newContent)
    println("Readme.md updated")
}