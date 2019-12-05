plugins {
  kotlin("multiplatform")
}

kotlin.sourceSets {
  val commonMain by getting {
    dependencies {
      implementation(project(":core"))
    }
  }
  val jvmMain by getting {
    dependencies {
      implementation("io.arrow-kt:arrow-core-data:0.10.3")
    }
  }
}
