/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando.tree

/**
 * Represents the names of a flag.
*/
interface FlagNames {

  /**
   * The long names of the flag; e.g. --flag (excl. --)
   */
  val long: List<String>

  /**
   * The short names of the flag; e.g. -f (excl. -)
   */
  val short: List<Char>
}
