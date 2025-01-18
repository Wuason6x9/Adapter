dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    compileOnly(project(":common"))

    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT") //Vanilla
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.5.0b") //ItemsAdder
    compileOnly("com.github.oraxen:oraxen:1.159.0") //Oraxen 1
    compileOnly(fileTree("libs")) // ExecutableItems and ExecutableBlocks
    implementation(fileTree("libs-impl")) // StorageMechanic and CustomItems
    compileOnly("net.Indyuce:MMOItems-API:6.9.5-SNAPSHOT") //MMOItems
    compileOnly("io.lumine:MythicLib-dist:1.6.2-SNAPSHOT") //MythicLib - MMOItems
    compileOnly("io.lumine:Mythic-Dist:5.6.1") //MythicMobs
    compileOnly("io.lumine:MythicCrucible-Dist:2.1.0") //MythicCrucible
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}