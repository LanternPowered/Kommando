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

internal class FoldedSource<S>(val property: SourceProperty<S, *>) : FoldedTreeElement<S> {

  override fun unfold(builder: TreeBuilder) {
    // Doesn't need to be included in the tree
  }

  override fun execute(context: CommandExecutionContext<S>) {
    this.property.setSource(context.source)
  }

  override fun cleanup() {
    this.property.clearSource()
  }

  override fun suggest(context: CommandExecutionContext<S>) = emptyList<Suggestion>()
}
