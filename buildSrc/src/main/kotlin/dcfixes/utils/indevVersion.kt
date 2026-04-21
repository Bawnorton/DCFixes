package dcfixes.utils

import org.gradle.api.Project
import java.io.File

fun Project.bumpIndevVersion(gradlePropertiesFile: File): Pair<String, String>? {
    val versionRegex = Regex("^(.*-indev\\.)(\\d+)$")
    val lines = gradlePropertiesFile.readLines()
    val versionLineIndex = lines.indexOfFirst { it.startsWith("mod.version=") }

    if (versionLineIndex == -1) {
        logger.warn("Skipping indev version bump: mod.version was not found in ${gradlePropertiesFile.path}")
        return null
    }

    val currentVersion = lines[versionLineIndex].substringAfter("mod.version=")
    val match = versionRegex.matchEntire(currentVersion)

    if (match == null) {
        logger.warn("Skipping indev version bump: mod.version '$currentVersion' is not in '<base>-indev.<number>' format")
        return null
    }

    val nextVersion = "${match.groupValues[1]}${match.groupValues[2].toInt() + 1}"
    val updatedLines = lines.toMutableList()
    updatedLines[versionLineIndex] = "mod.version=$nextVersion"

    val newContents = updatedLines.joinToString(separator = "\n", postfix = "\n")
    gradlePropertiesFile.writeText(newContents)
    return currentVersion to nextVersion
}

fun Project.configureAutomaticIndevVersionBumpOnBuild(
    gradlePropertiesFile: File = rootProject.file("gradle.properties")
) {
    tasks.named("build").configure {
        doLast {
            if (providers.gradleProperty("skipVersionBump").map { it.toBoolean() }.orElse(false).get()) {
                logger.lifecycle("Skipping indev version bump because -PskipVersionBump=true")
                return@doLast
            }

            val bumpedVersions = bumpIndevVersion(gradlePropertiesFile)
            if (bumpedVersions != null) {
                val (previousVersion, nextVersion) = bumpedVersions
                logger.lifecycle("Bumped mod.version from $previousVersion to $nextVersion")
            }
        }
    }
}

