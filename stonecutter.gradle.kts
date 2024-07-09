plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "1.18.2-forge" /* [SC] DO NOT EDIT */

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) { 
    group = "project"
    ofTask("build")
}