/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("FunctionName")

package org.lanternpowered.kommando.impl

import org.lanternpowered.kommando.Path
import org.lanternpowered.kommando.util.ToStringHelper

internal fun or(first: Path, second: Path): Path {
  val paths = mutableListOf<Path>()
  fun add(path: Path) = if (path is OrPath) {
    paths.addAll(path.paths)
  } else {
    paths.add(path)
  }
  add(first)
  add(second)
  return OrPath(paths)
}

internal fun then(current: Path, then: Path): Path {
  return when {
    then is OtherwisePath -> current
    current is OtherwisePath -> then
    current is BeforeArguments -> then(current.path, then)
    else -> ThenPath(current, then)
  }
}

internal fun beforeArguments(path: Path): Path {
  if (path is BeforeArguments) {
    return path
  }
  return BeforeArguments(path)
}

internal fun stripBeforeArguments(path: Path): Path {
  return when (path) {
    is BeforeArguments -> path.path
    is OrPath -> OrPath(path.paths.map { stripBeforeArguments(it) })
    is ThenPath -> ThenPath(stripBeforeArguments(path.current), stripBeforeArguments(path.then))
    else -> path
  }
}

internal data class ValuePath(val value: String) : Path

internal data class OrPath(val paths: List<Path>) : Path {

  override fun toString() = ToStringHelper(this)
      .add("paths", this.paths.joinToString(separator = ",", prefix = "[", postfix = "]"))
      .toString()
}

internal data class ThenPath(val current: Path, val then: Path) : Path

internal data class BeforeArguments(val path: Path) : Path

internal object OtherwisePath : Path
