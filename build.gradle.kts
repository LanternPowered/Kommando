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
    js()
    // TODO: Figure out why native fails now and report a bug,
    //  did the classes inheritance structure become too complex?
    // mingwX64()

    sourceSets {
      val commonMain by getting {
        dependencies {
          implementation(kotlin("stdlib-common"))
        }
      }

      val commonTest by getting {
        dependencies {
          implementation(kotlin("test-common"))
          implementation(kotlin("test-annotations-common"))
        }
      }

      val jvmMain by getting {
        dependencies {
          implementation(kotlin("stdlib"))
        }
      }

      val jvmTest by getting {
        dependencies {
          implementation(kotlin("test-junit"))
        }
      }

      val jsMain by getting {
        dependencies {
          implementation(kotlin("stdlib-js"))
        }
      }

      val jsTest by getting {
        dependencies {
          implementation(kotlin("test-js"))
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
