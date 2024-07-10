plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.19.2-forge" /* [SC] DO NOT EDIT */

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) { 
    group = "project"
    ofTask("build")
}

stonecutter registerChiseled tasks.register("chiseledBuildAndCollect", stonecutter.chiseled) {
    group = "project"
    ofTask("buildAndCollect")
}

stonecutter registerChiseled tasks.register("chiseledPublishMods", stonecutter.chiseled) {
    group = "project"
    ofTask("publishMods")
}

stonecutter configureEach {
    val isDev = "dev" to !gradle.startParameter.taskNames.stream().anyMatch { it.lowercase().contains("build") || it.lowercase().contains("publish") }
    consts(isDev)
}
