plugins {
  `kotlin-dsl`
  kotlin("jvm") version "2.2.0"
}

repositories {
  mavenCentral()
  maven("https://maven.minecraftforge.net")
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("net.minecraftforge:srgutils:0.4.11")
}