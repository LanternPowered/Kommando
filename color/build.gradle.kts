plugins {
  kotlin("multiplatform")
}

kotlin.sourceSets {
  val commonMain by getting {
    dependencies {
      implementation(project(":kommando-core"))
    }
  }
}
