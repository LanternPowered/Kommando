/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando.impl

import org.lanternpowered.kommando.suggestion.Suggestion

internal interface FoldedTreeElement<S> {

  /**
   * Parses the arguments and executes the command.
   */
  fun execute(context: CommandExecutionContext<S>)

  /**
   * Cleanup the element.
   */
  fun cleanup()

  /**
   * Gets suggestions for the current command context.
   */
  fun suggest(context: CommandExecutionContext<S>): List<Suggestion>

  /**
   * Unfolds this tree element into the given builder.
   */
  fun unfold(builder: TreeBuilder)
}
