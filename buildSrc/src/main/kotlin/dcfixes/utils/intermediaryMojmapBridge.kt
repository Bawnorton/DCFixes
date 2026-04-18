package dcfixes.utils

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register
import java.io.File

data class IntermediaryJarBridgeSpec(
  val key: String,
  val moduleNotation: String,
  val remappedJarBasename: String = key,
  val configureDependency: ExternalModuleDependency.() -> Unit = {}
)

fun Project.registerIntermediaryToMojmapCompileBridges(
  specs: List<IntermediaryJarBridgeSpec>,
  minecraftVersion: String,
  intermediaryMappingsCoordinate: String = "net.fabricmc:intermediary:$minecraftVersion:v2",
  tinyRemapperCoordinate: String = "net.fabricmc:tiny-remapper:0.10.4"
) {
  if (specs.isEmpty()) return

  val intermediaryToMojmapBridge = layout.buildDirectory.file("tmp/intermediary-mappings/intermediary-to-mojmap.tiny")
  val mojangClientMappings = providers.provider {
    File(gradle.gradleUserHomeDir, "caches/neoformruntime/artifacts/minecraft_${minecraftVersion}_client_mappings.txt")
  }

  val intermediaryMappingsArtifact = configurations.maybeCreate("intermediaryMappingsArtifact").apply {
    isTransitive = false
  }
  val tinyRemapperTool = configurations.maybeCreate("tinyRemapperTool")

  dependencies {
    add(intermediaryMappingsArtifact.name, intermediaryMappingsCoordinate)
    add(tinyRemapperTool.name, tinyRemapperCoordinate)
  }

  val extractIntermediaryMappings = tasks.register<org.gradle.api.tasks.Copy>("extractIntermediaryMappings") {
    from(zipTree(intermediaryMappingsArtifact.singleFile)) {
      include("mappings/mappings.tiny")
      eachFile { path = "mappings.tiny" }
      includeEmptyDirs = false
    }
    into(layout.buildDirectory.dir("tmp/intermediary-mappings"))
  }

  val createIntermediaryToMojmapBridge = tasks.register("createIntermediaryToMojmapBridge") {
    dependsOn(extractIntermediaryMappings)

    val intermediaryTiny = layout.buildDirectory.file("tmp/intermediary-mappings/mappings.tiny")

    inputs.file(intermediaryTiny)
    outputs.file(intermediaryToMojmapBridge)

    doLast {
      val mojangMappingsFile = mojangClientMappings.get()
      check(mojangMappingsFile.isFile) {
        "Missing Mojang client mappings at ${mojangMappingsFile.absolutePath}. " +
          "Run a standard Forge setup task (for example `genSources`) once so NeoForm can cache it."
      }

      MappingBridge.createIntermediaryToMojmap(
        intermediaryTiny.get().asFile,
        mojangMappingsFile,
        intermediaryToMojmapBridge.get().asFile
      )
    }
  }

  val remapTasks = specs.map { spec ->
    val inputConfiguration = configurations.create("${spec.key}IntermediaryInput") {
      isTransitive = false
    }

    val dependency = (dependencies.create(spec.moduleNotation) as ExternalModuleDependency)
      .apply(spec.configureDependency)
    dependencies.add(inputConfiguration.name, dependency)

    val remappedJar = layout.buildDirectory.file("remapped/${spec.remappedJarBasename}-mojmap.jar")
    dependencies.add("compileOnly", files(remappedJar))

    val taskSuffix = spec.key.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    tasks.register<JavaExec>("remap${taskSuffix}ToMojmap") {
      dependsOn(createIntermediaryToMojmapBridge)

      classpath = tinyRemapperTool
      mainClass.set("net.fabricmc.tinyremapper.Main")

      args(
        inputConfiguration.singleFile.absolutePath,
        remappedJar.get().asFile.absolutePath,
        intermediaryToMojmapBridge.get().asFile.absolutePath,
        "left",
        "right"
      )

      outputs.file(remappedJar)
    }
  }

  tasks.withType(JavaCompile::class.java).configureEach {
    dependsOn(remapTasks)
  }

  tasks.matching { it.name.startsWith("compile") && it.name.endsWith("Kotlin") }.configureEach {
    dependsOn(remapTasks)
  }
}

