package dcfixes.utils

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

data class IntermediaryJarBridgeSpec(
  val key: String,
  val moduleNotation: String,
  val remappedJarBasename: String = key,
  val configureDependency: ExternalModuleDependency.() -> Unit = {},
  val remapEmbeddedJars: Boolean = false,
  val embeddedJarFilter: (String) -> Boolean = { entryName ->
    entryName.startsWith("META-INF/jars/") && entryName.endsWith(".jar")
  }
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
    val tempRemappedJar = layout.buildDirectory.file("remapped/tmp/${spec.remappedJarBasename}-mojmap.jar")
    dependencies.add("compileOnly", files(remappedJar))

    val taskSuffix = spec.key.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    val remapMainTask = tasks.register<JavaExec>("remap${taskSuffix}ToMojmap") {
      dependsOn(createIntermediaryToMojmapBridge)

      classpath = tinyRemapperTool
      mainClass.set("net.fabricmc.tinyremapper.Main")

      val outputJar = if (spec.remapEmbeddedJars) tempRemappedJar else remappedJar
      args(
        inputConfiguration.singleFile.absolutePath,
        outputJar.get().asFile.absolutePath,
        intermediaryToMojmapBridge.get().asFile.absolutePath,
        "left",
        "right"
      )

      outputs.file(outputJar)
    }

    val finalRemapTask = if (spec.remapEmbeddedJars) {
      tasks.register("remap${taskSuffix}EmbeddedToMojmap") {
        dependsOn(remapMainTask)
        dependsOn(createIntermediaryToMojmapBridge)

        inputs.file(inputConfiguration.singleFile)
        inputs.file(tempRemappedJar)
        inputs.file(intermediaryToMojmapBridge)
        outputs.file(remappedJar)

        doLast {
          remapEmbeddedJarsInJar(
            inputConfiguration.singleFile,
            tempRemappedJar.get().asFile,
            remappedJar.get().asFile,
            intermediaryToMojmapBridge.get().asFile,
            tinyRemapperTool,
            spec.embeddedJarFilter
          )
        }
      }
    } else {
      remapMainTask
    }

    finalRemapTask
  }

  tasks.withType(JavaCompile::class.java).configureEach {
    dependsOn(remapTasks)
  }

  tasks.matching { it.name.startsWith("compile") && it.name.endsWith("Kotlin") }.configureEach {
    dependsOn(remapTasks)
  }
}

private fun Project.remapEmbeddedJarsInJar(
  inputJar: File,
  remappedMainJar: File,
  outputJar: File,
  mappingTiny: File,
  tinyRemapperClasspath: FileCollection,
  embeddedJarFilter: (String) -> Boolean
) {
  val tempRoot = layout.buildDirectory.dir("tmp/intermediary-embedded-jars").get().asFile
  val extractedDir = tempRoot.resolve("input")
  val remappedDir = tempRoot.resolve("remapped")
  extractedDir.mkdirs()
  remappedDir.mkdirs()

  val embeddedEntries = ZipFile(inputJar).use { zip ->
    zip.entries().asSequence()
      .filter { !it.isDirectory && embeddedJarFilter(it.name) }
      .toList()
  }

  if (embeddedEntries.isEmpty()) {
    outputJar.parentFile.mkdirs()
    remappedMainJar.copyTo(outputJar, overwrite = true)
    return
  }

  val remappedByName = mutableMapOf<String, File>()
  ZipFile(inputJar).use { zip ->
    for (entry in embeddedEntries) {
      val extractedFile = extractedDir.resolve(entry.name)
      extractedFile.parentFile.mkdirs()
      zip.getInputStream(entry).use { input ->
        extractedFile.outputStream().use { output ->
          input.copyTo(output)
        }
      }

      val remappedFile = remappedDir.resolve(entry.name)
      remappedFile.parentFile.mkdirs()

      javaexec {
        classpath = tinyRemapperClasspath
        mainClass.set("net.fabricmc.tinyremapper.Main")
        args(
          extractedFile.absolutePath,
          remappedFile.absolutePath,
          mappingTiny.absolutePath,
          "left",
          "right"
        )
      }

      remappedByName[entry.name] = remappedFile
    }
  }

  outputJar.parentFile.mkdirs()
  ZipFile(remappedMainJar).use { zip ->
    ZipOutputStream(outputJar.outputStream()).use { out ->
      val entries = zip.entries()
      while (entries.hasMoreElements()) {
        val entry = entries.nextElement()
        if (entry.isDirectory) {
          val dirEntry = ZipEntry(entry.name)
          dirEntry.time = entry.time
          out.putNextEntry(dirEntry)
          out.closeEntry()
          continue
        }

        val replacement = remappedByName[entry.name]
        val newEntry = ZipEntry(entry.name)
        newEntry.time = entry.time
        out.putNextEntry(newEntry)
        val inputStream = if (replacement != null) {
          replacement.inputStream()
        } else {
          zip.getInputStream(entry)
        }
        inputStream.use { it.copyTo(out) }
        out.closeEntry()
      }
    }
  }
}
