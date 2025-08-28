import org.gradle.internal.declarativedsl.parsing.main

plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
    id("maven-publish")
}

allprojects {

    group = "dev.wuason"
    version = "1.0.6.2"

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
        maven("https://repo.momirealms.net/releases/")
        maven("https://maven.devs.beer/")
    }

}

dependencies {
    implementation(project(":common")) //Common
    implementation(project(":oraxen2")) //Oraxen 2
    implementation(project(":bukkit")) //Bukkit
    implementation(project(":nexo")) //NMS
    implementation(project(":craft-engine")) //CraftEngine
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
        dependsOn(named("classes"))

        val includedSubprojects = subprojects.filter { it.path != ":plugin" }

        dependsOn(includedSubprojects.map { it.tasks.named("classes") })

        val modules = includedSubprojects
            .flatMap { it.sourceSets["main"].allJava.srcDirs }
            .filter { it.exists() }

        val submoduleClasspath = includedSubprojects.flatMap {
            val main = it.sourceSets["main"]
            listOf(main.output) + main.compileClasspath
        }

        source(modules)
        classpath = files(submoduleClasspath)

        setDestinationDir(file("build/javadoc"))
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
    repositories {
        maven {
            url = uri("https://repo.techmc.es/releases")
            credentials(PasswordCredentials::class) {
                username = System.getenv("REPO_USERNAME")
                password = System.getenv("REPO_PASSWORD")
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            artifactId = rootProject.name
            version = rootProject.version.toString()
            artifact(tasks.shadowJar)
            artifact(tasks.getByName("javadocJar"))
            pom {
                name = "Adapter API"
                url = "https://github.com/Wuason6x9/Adapter/"
                licenses {
                    license {
                        name = "GNU General Public License v3.0"
                        url = "https://www.gnu.org/licenses/gpl-3.0.html"
                        distribution = "repo"
                    }
                }
            }
        }
    }
}

val file = file("readme.md")
gradle.projectsEvaluated {
    val content = file.readText()
    val newContent = content
        .replace(
            Regex("""(?<=https://repo\.techmc\.es/javadoc/releases/dev/wuason/Adapter/)([^)\s]+)"""),
            "${project.version}"
        )
    file.writeText(newContent)
    println("Readme.md updated")
}