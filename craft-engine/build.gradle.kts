dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT") //Vanilla
    compileOnly("net.momirealms:craft-engine-core:0.0.59")
    compileOnly("net.momirealms:craft-engine-bukkit:0.0.59")
    compileOnly(project(":common"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}