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

import org.lanternpowered.kommando.argument.Argument
import org.lanternpowered.kommando.suggestion.Suggestion

internal class FoldedArgument<T, S>(
    val argument: Argument<out T, in S>,
    val property: ValueProperty<T>,
    val name: String? = null
) : FoldedTreeElement<S> {

  override fun unfold(builder: TreeBuilder) {
    // TODO: Proper default name
    builder.argument(this.name ?: "def", this.argument)
  }

  override fun execute(context: CommandExecutionContext<S>) {
    val parseResult = this.argument.parse(context)
    if (parseResult.potentialError != null)
      context.pushPotentialError(parseResult.potentialError)
    this.property.setValue(parseResult.value)
  }

  override fun cleanup() {
    this.property.clearValue()
  }

  override fun suggest(context: CommandExecutionContext<S>): List<Suggestion> {
    return this.argument.suggest(context)
  }
}
