rootProject.name = "Kommando"

listOf("core", "color", "arrow").forEach {
  include(it)
  project(":$it").name = "kommando-$it"
}
