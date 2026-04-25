import java.util.Properties

plugins {
    `java-library`
    id("net.neoforged.moddev.legacyforge")
}

val activeStonecutterVersion = Regex("""stonecutter\s+active\s+\"([^\"]+)\"""")
    .find(rootProject.file("stonecutter.gradle.kts").readText())
    ?.groupValues
    ?.get(1)
    ?: error("Unable to determine active Stonecutter version from stonecutter.gradle.kts")

val versionProperties = Properties().apply {
    rootProject.file("versions/$activeStonecutterVersion/gradle.properties").inputStream().use(::load)
}

val forgeVersion = versionProperties.getProperty("deps.forge")
    ?: error("Missing deps.forge in versions/$activeStonecutterVersion/gradle.properties")

val modId = rootProject.findProperty("mod.id") as String
val modVersion = rootProject.findProperty("mod.version") as String
val modGroup = rootProject.findProperty("mod.group") as String

group = "$modGroup.$modId"
version = modVersion
base.archivesName = "$modId-early-loader"

repositories {
    mavenCentral()
    maven("https://maven.minecraftforge.net")
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
}

legacyForge {
    version = forgeVersion
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

tasks.named<Jar>("jar") {
    manifest {
        attributes(
            "Implementation-Title" to "$modId early-loader",
            "Implementation-Version" to modVersion
        )
    }
}

