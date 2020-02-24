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

interface Command<S> {

  /**
   * Gets completion suggestions for the given [CommandContext].
   */
  fun getCompletionSuggestions(context: CommandContext<S>): List<String>

  /**
   * Executes the command for the given [CommandContext].
   */
  fun execute(context: CommandContext<S>)
}
