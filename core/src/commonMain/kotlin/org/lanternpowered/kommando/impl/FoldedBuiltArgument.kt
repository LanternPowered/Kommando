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

import org.lanternpowered.kommando.CommandException
import org.lanternpowered.kommando.LiteralMessage
import org.lanternpowered.kommando.ValidationContext
import org.lanternpowered.kommando.suggestion.Suggestion

internal class FoldedBuiltArgument<T, S>(
    builder: ArgumentBaseBuilderImpl<T, S>,
    private val property: ValueProperty<T>,
    private val converter: (ValidationContext.() -> T)? = (builder as? ArgumentBuilderImpl<T,S>)?.converter
) : FoldedTreeElement<S> {

  private val rootElements
      = builder.children.filter { it.path == null }.map { it.element }

  private val children
      = builder.children.filter { it.path != null }

  override fun unfold(builder: TreeBuilder) {
    for (element in this.rootElements)
      element.unfold(builder)
    for (child in this.children)
      child.element.unfold(builder.literals(child.path ?: throw IllegalStateException()))
  }

  override fun execute(context: CommandExecutionContext<S>) {
    for (element in this.rootElements)
      element.execute(context)
    var potentialError: CommandException? = null
    for (child in this.children) {
      val cursor = context.cursor
      try {
        child.element.execute(context)
        // Child element was successful, no
        // other path must be perceived
        return
      } catch (ex: CommandException) {
        if (potentialError != null)
          potentialError = ex
        // Something went wrong, so cleanup.
        child.element.cleanup()
        // And reset cursor
        context.cursor = cursor
      }
    }
    val converter = this.converter
    if (converter != null) {
      val value = converter(context)
      this.property.setValue(value)
      // A successful end path was reached,
      // the value was converted
      return
    }
    if (potentialError != null)
      throw potentialError
    throw CommandException(LiteralMessage("Invalid path")) // TODO: Better message
  }

  override fun cleanup() {
    this.property.clearValue()
    this.rootElements.forEach { it.cleanup() }
    this.children.forEach { it.element.cleanup() }
  }

  override fun suggest(context: CommandExecutionContext<S>): List<Suggestion> {
    TODO()
  }
}
