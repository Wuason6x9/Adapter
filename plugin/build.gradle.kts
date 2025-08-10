plugins {
    id("com.gradleup.shadow")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT") //Vanilla
    implementation(project(project.rootProject.path))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set("AdapterTestingDev")
        relocate("dev.wuason.adapter", "dev.wuason.testing.adapter")
    }

    build {
        dependsOn(shadowJar)
    }
}