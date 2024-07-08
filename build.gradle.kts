@file:Suppress("UnstableApiUsage")

plugins {
    `maven-publish`
    kotlin("jvm") version "1.9.22"
    id("dev.architectury.loom") version "1.7-SNAPSHOT"
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("me.modmuss50.mod-publish-plugin") version "0.5.+"
}

class ModData {
    val id = property("mod_id").toString()
    val name = property("mod_name").toString()
    val version = property("mod_version").toString()
    val group = property("mod_group").toString()
    val minecraftDependency = property("minecraft_dependency").toString()
}

class LoaderData {
    private val name = loom.platform.get().name.lowercase()

    fun getVersion() : String = property("forge_loader").toString()

    override fun toString(): String {
        return name
    }
}

class MinecraftVersionData {
    private val name = stonecutter.current.version.substringBeforeLast("-")

    override fun toString(): String {
        return name
    }
}

fun DependencyHandler.forge(dep: Any) = add("forge", dep)

val mod = ModData()
val loader = LoaderData()
val minecraftVersion = MinecraftVersionData()
val awName = "dcfixes.accesswidener"


version = "${mod.version}+$minecraftVersion"
group = mod.group
base.archivesName.set(mod.name)

repositories {
    mavenCentral()
    maven("https://cursemaven.com")
    maven("https://api.modrinth.com/maven")
    maven("https://maven.shedaniel.me")
    maven("https://maven.minecraftforge.net/")
    maven("https://maven.neoforged.net/releases/")
    maven("https://jitpack.io/")
}

dependencies {
    forge("net.minecraftforge:forge:$minecraftVersion-${loader.getVersion()}")

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$minecraftVersion+build.${property("yarn_build")}:v2")

    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:${property("mixin_extras")}")!!)
    implementation(include("io.github.llamalad7:mixinextras-forge:${property("mixin_extras")}")!!)

    compileOnly(annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-common:${property("mixin_squared")}")!!)
    implementation(include("com.github.bawnorton.mixinsquared:mixinsquared-forge:${property("mixin_squared")}")!!)

    modCompileOnly(fileTree(rootProject.file("libs")) {
        include("*.jar")
    })
}

loom {
    accessWidenerPath.set(rootProject.file("src/main/resources/$awName"))

    runConfigs.all {
        ideConfigGenerated(true)
        runDir = "../../run"
    }

    runConfigs["client"].apply {
        vmArgs("-Dmixin.debug.export=true")
        programArgs("--username Bawnorton")
    }

    forge {
        convertAccessWideners = true
        mixinConfig("${mod.id}.mixins.json")
    }
}

tasks {
    withType<JavaCompile> {
        options.release = 17
    }

    processResources {
        val map = mapOf(
            "version" to mod.version,
            "minecraft_dependency" to mod.minecraftDependency,
            "loader_version" to loader.getVersion()
        )

        inputs.properties(map)
        filesMatching("META-INF/mods.toml") { expand(map) }
    }
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
