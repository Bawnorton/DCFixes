import dcfixes.utils.applyMixinDebugSettings
import dcfixes.utils.deps
import dcfixes.utils.mod
import dev.kikugie.fletching_table.annotation.MixinEnvironment

plugins {
    kotlin("jvm")
    `maven-publish`
    id("net.neoforged.moddev.legacyforge")
    id("dcfixes.common")
    id("me.modmuss50.mod-publish-plugin")
    id("com.google.devtools.ksp") version "2.3.6"
    id("dev.kikugie.fletching-table.lexforge") version "0.1.0-alpha.22"
    id("dev.isxander.secrets") version "0.1.0"
}

repositories {
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }

    maven("https://maven.minecraftforge.net")
    maven("https://maven.bawnorton.com/releases")
    maven("https://maven.parchmentmc.org")
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    maven("https://maven.isxander.dev/releases")
    maven("https://jitpack.io")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.sinytra.org/")

    strictMaven("https://www.cursemaven.com", "Curseforge", "curse.maven")
    strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")

    flatDir {
        dir(rootProject.file("libs/jars"))
    }
}

val minecraft: String by project
val loader: String by project

base.archivesName = "${mod("id")}-${mod("version")}+$minecraft-$loader"

dependencies {
    // Mixin
    implementation(annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.3")!!)
    jarJar(implementation("io.github.llamalad7:mixinextras-forge:0.5.3")!!)

    implementation(annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-common:0.3.7-beta.1")!!)
    jarJar(implementation("com.github.bawnorton.mixinsquared:mixinsquared-forge:0.3.7-beta.1")!!)

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

    // Yacl
    modImplementation("dev.isxander:yet-another-config-lib:3.6.1+1.20.1-forge")

    // Compats
    modCompileOnly("curse.maven:engineered-schematics-1207780:7666550")

    modCompileOnly("curse.maven:minecraft-transport-simulator-286703:7423733")
    modRuntimeOnly("curse.maven:spark-361579:4738952")

    // Fabric Mod Compats
    modImplementation("org.sinytra:Connector:1.0.0-beta.48+1.20.1")
    implementation("com.github.FxMorin.MoreCulling:moreculling:v0.24.0") {
        exclude(group = "net.fabricmc")
        exclude(group = "maven.modrinth")
    }

    // Physics Mod + Geckolib Compats
    modImplementation("curse.maven:physics-mod-442735:7781938")
    modImplementation("software.bernie.geckolib:geckolib-forge-$minecraft:4.8.3")
    implementation("com.eliotlash.mclib:mclib:20")
    modImplementation("curse.maven:theundead-479710:7446558")
    modImplementation("curse.maven:zombie-extreme-392809:7014500")
    modImplementation("curse.maven:customnpcs-unofficial-1052708:7694841")
    modImplementation("curse.maven:apocalypse-now-448410:6364603")
    modRuntimeOnly("curse.maven:curios-309927:6418456")
    modImplementation("curse.maven:playeranimator-658587:4587214")
    modImplementation("curse.maven:deceased-beast-1426968:7640180")
    modImplementation("curse.maven:naturalist-627986:6863943")

    // Dev Jars of EMF, ETF and Jars of non maven projs: iv packs, HoldMyItems
    for (modJar in rootProject.fileTree("libs/jars") { include("*.jar") }) {
        val basename = modJar.name.substring(0, modJar.name.length - ".jar".length)
        val versionSep = basename.lastIndexOf('-')
        val artifactId = basename.substring(0, versionSep)
        val version = basename.substring(versionSep + 1)
        modImplementation("libs:$artifactId:$version")
    }
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

legacyForge {
    version = deps("forge")

    validateAccessTransformers = true
    accessTransformers.from(rootProject.file("src/main/resources/$minecraft-accesstransformer.cfg"))

    // ATs from EMF, ETF
    rootProject.fileTree("libs/ats") { include("*.cfg") }.forEach { accessTransformers.from(it) }

    mods {
        register(mod("id")!!) {
            sourceSet(sourceSets["main"])
        }
    }

    deps("parchment") {
        parchment {
            val (mc, version) = it.split(':')
            mappingsVersion = version
            minecraftVersion = mc
        }
    }

    runs {
        all {
            gameDirectory = rootProject.file("run")
        }

        register("client") {
            ideName = "Forge Client $minecraft"
            client()

            programArgument("--username=Bawnorton")
            programArgument("--uuid=17c06cab-bf05-4ade-a8d6-ed14aaf70545")

            systemProperty("terminal.ansi", "true")
        }

        register("server") {
            ideName = "Forge Server $minecraft"
            server()

            systemProperty("terminal.ansi", "true")
        }
    }

    afterEvaluate {
        runs.configureEach {
            applyMixinDebugSettings(::jvmArgument, ::systemProperty)
        }
    }
}

mixin {
    add(sourceSets.main.get(), "${mod("id")}.refmap.json")
    config("${mod("id")}.mixins.json")
    config("${mod("id")}.client.mixins.json")
}

fletchingTable {
    mixins.register("main") {
        mixin("default", "${mod("id")}.mixins.json")
        mixin("client", "${mod("id")}.client.mixins.json") {
            environment = MixinEnvironment.Env.CLIENT
        }
    }
}

sourceSets.main {
    resources.srcDirs(project.file("src/main/generated/server"), project.file("src/main/generated/client"))
    resources.exclude(".cache")
}

tasks {
    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(named<Jar>("reobfJar").map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${mod("version")}"))
        dependsOn("build")
    }

    named<Jar>("jar") {
        manifest {
            attributes(
                "Specification-Title" to mod("name")!!,
                "Specification-Vendor" to "${mod("author")}",
                "Specification-Version" to "1",
                "Implementation-Title" to project.name,
                "Implementation-Version" to mod("version")!!,
                "Implementation-Vendor" to "${mod("author")}",
                "MixinConfigs" to listOf(
                    "${mod("id")}.mixins.json",
                    "${mod("id")}.client.mixins.json"
                ).joinToString(", ")
            )
        }
    }
}

val isPublishing = gradle.startParameter.taskNames.any {
    it.contains("publish", ignoreCase = true)
}

extensions.configure<PublishingExtension> {
    repositories {
        maven {
            name = "bawnorton"
            url = uri("https://maven.bawnorton.com/releases")

            if(isPublishing) {
                credentials {
                    username = onePassword["op://Private/Maven API Key/username"].get()
                    password = onePassword["op://Private/Maven API Key/credential"].get()
                }
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "${mod("group")}.${mod("id")}"
            artifactId = "${mod("id")}-$loader"
            version = "${mod("version")}+$minecraft"

            from(components["java"])
        }
    }
}

publishMods {
    val mrTokenProvider = onePassword["op://Private/Modrinth API Key/credential"]
    val cfTokenProvider = onePassword["op://Private/Curseforge API Key/credential"]

    type = STABLE
    file = tasks.named<Jar>("reobfJar").map { it.archiveFile.get() }
    additionalFiles.from(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile.get() })

    displayName = "${mod("name")} Forge ${mod("version")} for $minecraft"
    version = mod("version")
    changelog = provider { rootProject.file("CHANGELOG.md").readText() }
    modLoaders.add(loader)

    val compatibleVersionString = mod("compatible_versions")!!
    val compatibleVersions = compatibleVersionString.split(",").map { it.trim() }

    modrinth {
        projectId = property("publishing.modrinth") as String
        accessToken.set(mrTokenProvider)
        minecraftVersions.addAll(compatibleVersions)
    }

    curseforge {
        projectId = property("publishing.curseforge") as String
        accessToken.set(cfTokenProvider)
        minecraftVersions.addAll(compatibleVersions)
    }
}