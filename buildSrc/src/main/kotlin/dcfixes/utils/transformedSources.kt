package dcfixes.utils

import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.dependencies
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.LinkedHashSet
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream
import kotlin.io.path.invariantSeparatorsPathString

private data class RequestedModule(val group: String, val name: String, val version: String) {
  fun transformedJarName(): String = "$name-$version.jar"
}

private open class TransformedSourcesState {
  val modules = LinkedHashSet<RequestedModule>()
  var registered = false
}

/**
 * Marks a dependency for transformed cache source generation via Vineflower.
 */
fun Project.withSourcesJar(dependency: Dependency?): Dependency? {
  val moduleDependency = dependency as? ExternalModuleDependency ?: return dependency
  val group = moduleDependency.group
  val version = moduleDependency.version ?: return dependency

  val state = (extensions.findByName("transformedSourcesState") as? TransformedSourcesState)
    ?: extensions.create("transformedSourcesState", TransformedSourcesState::class.java)

  state.modules.add(RequestedModule(group, moduleDependency.name, version))

  if (!state.registered) {
    state.registered = true
    registerTransformedSourcesTask(state)
  }

  return dependency
}

private fun Project.registerTransformedSourcesTask(state: TransformedSourcesState) {
  val vineflowerTool = configurations.maybeCreate("vineflowerTool").apply {
    isCanBeResolved = true
    isCanBeConsumed = false
    isTransitive = false
  }

  dependencies {
    add(vineflowerTool.name, "org.vineflower:vineflower:1.11.2")
  }

  val generateTask = tasks.register("generateTransformedDependencySources") {
    group = "build setup"
    description = "Generates -sources.jar files next to Gradle transformed dependency jars"

    val tmpRoot = layout.buildDirectory.dir("tmp/transformed-sources")

    inputs.property("withSourcesModules", providers.provider {
      state.modules.map { "${it.group}:${it.name}:${it.version}" }.sorted()
    })

    doLast {
      val userHome = gradle.gradleUserHomeDir
      val taskTmpRoot = tmpRoot.get().asFile

      state.modules.forEach { module ->
        val transformedJars = findTransformedJars(userHome, module.transformedJarName())
        if (transformedJars.isEmpty()) {
          logger.info("No transformed jar found for ${module.group}:${module.name}:${module.version}")
          return@forEach
        }

        transformedJars.forEach { transformedJar ->
          val sourcesJar = File(transformedJar.parentFile, "${transformedJar.nameWithoutExtension}-sources.jar")
          if (sourcesJar.isFile) return@forEach

          val decompileOutputDir = taskTmpRoot.resolve(module.name).resolve(transformedJar.nameWithoutExtension)
          delete(decompileOutputDir)
          decompileOutputDir.mkdirs()

          javaexec {
            classpath = vineflowerTool
            mainClass.set("org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler")
            args(
              "-log=ERROR",
              transformedJar.absolutePath,
              decompileOutputDir.absolutePath
            )
          }

          packDirectoryAsJar(decompileOutputDir.toPath(), sourcesJar.toPath())
          logger.lifecycle("Generated sources jar: ${sourcesJar.absolutePath}")
        }
      }
    }
  }

  tasks.withType(JavaCompile::class.java).configureEach {
    dependsOn(generateTask)
  }

  tasks.matching { it.name.startsWith("compile") && it.name.endsWith("Kotlin") }.configureEach {
    dependsOn(generateTask)
  }
}

private fun findTransformedJars(gradleUserHome: File, jarName: String): List<File> {
  val cachesPath = gradleUserHome.toPath().resolve("caches")
  if (!Files.isDirectory(cachesPath)) return emptyList()

  Files.find(cachesPath, 10, { path, attrs ->
    attrs.isRegularFile &&
      path.fileName.toString() == jarName &&
      path.parent?.fileName?.toString() == "transformed" &&
      isTransformedCachePath(path)
  }).use { stream ->
    return stream.map(Path::toFile).toList()
  }
}

private fun isTransformedCachePath(path: Path): Boolean {
  val normalized = path.invariantSeparatorsPathString
  return normalized.contains("/caches/transforms/") ||
    normalized.contains("/caches/transformed/") ||
    normalized.contains("/caches/") && normalized.contains("/transforms/")
}

private fun packDirectoryAsJar(sourceDir: Path, outputJar: Path) {
  Files.createDirectories(outputJar.parent)

  JarOutputStream(BufferedOutputStream(FileOutputStream(outputJar.toFile()))).use { jarOut ->
    Files.walk(sourceDir).use { paths ->
      paths.filter { Files.isRegularFile(it) }
        .forEach { filePath ->
          val entryName = sourceDir.relativize(filePath).invariantSeparatorsPathString
          jarOut.putNextEntry(JarEntry(entryName))
          FileInputStream(filePath.toFile()).use { it.copyTo(jarOut) }
          jarOut.closeEntry()
        }
    }
  }
}


