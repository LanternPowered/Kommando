plugins {
    kotlin("multiplatform") version "1.3.61"
    id("net.minecrell.licenser") version "0.4.1"
}

allprojects {
  group = "org.lanternpowered"
  version = "1.0-SNAPSHOT"

  repositories {
    mavenCentral()
  }
}

subprojects {
  apply(plugin = "org.jetbrains.kotlin.multiplatform")
  apply(plugin = "net.minecrell.licenser")

  kotlin {
    jvm()
    // TODO: Figure out why js isn't working; all stdlib methods
    // TODO: are throwing errors that they are unresolved.
    // js()
    mingwX64()

    sourceSets {
      val commonMain by getting {
        dependencies {
          implementation(kotlin("stdlib"))
        }
      }

      all {
        languageSettings.apply {
          languageVersion = "1.3"
          apiVersion = "1.3"
          progressiveMode = true

          enableLanguageFeature("InlineClasses")
          enableLanguageFeature("NewInference")
          enableLanguageFeature("NonParenthesizedAnnotationsOnFunctionalTypes")

          useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
          useExperimentalAnnotation("kotlin.contracts.ExperimentalContracts")
          useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
          useExperimentalAnnotation("kotlin.experimental.ExperimentalTypeInference")
        }
      }
    }
  }

  license {
    header = rootProject.file("HEADER.txt")
    newLine = false
    ignoreFailures = false

    // Map the kotlin source sets to normal source sets
    // so that they work in the license plugin.
    sourceSets {
      for ((name, kotlinSourceSet) in kotlin.sourceSets.asMap) {
        create(project.name + "_" + name) {
          allSource.source(kotlinSourceSet.kotlin)
        }
      }
    }

    include("**/*.kt")

    ext {
      set("name", rootProject.name)
      set("url", "https://www.lanternpowered.org")
      set("organization", "LanternPowered")
    }
  }
}