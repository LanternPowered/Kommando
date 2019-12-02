/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando

interface CommandContext<S> {

  /**
   * The source that caused the command to execute.
   */
  val source: S

  /**
   * The raw input of the command.
   */
  val input: String
}
