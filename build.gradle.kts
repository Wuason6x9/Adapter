plugins {
    id("java")
}

allprojects {

    group = "dev.wuason"
    version = "1.0-SNAPSHOT"

    apply(plugin = "java")

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://jitpack.io")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

dependencies {
}
