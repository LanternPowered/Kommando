/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando.argument

/**
 * Represents an argument name.
 */
data class ArgumentUsage(
    val name: String,
    val optional: Boolean = false
) {

  override fun toString(): String {
    if (!optional)
      return name
    return "[$name]"
  }
}