rootProject.name = "Kommando"

listOf("core", "brigadier", "color", "arrow").forEach {
  include(it)
  project(":$it").name = "kommando-$it"
}
