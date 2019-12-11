/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando.brigadier

import org.lanternpowered.kommando.util.ToStringHelper

/**
 * Represents a node of a brigadier command tree.
 */
sealed class CommandNode {

  private val mutableChildren = mutableMapOf<String, CommandNode>()

  /**
   * The children of this command node.
   */
  val children: Map<String, CommandNode>
    get() = this.mutableChildren

  protected open fun toStringHelper(): ToStringHelper = ToStringHelper(this)
      .add("children", "[" + this.children.map { (name, value) -> "$name: $value" }.joinToString(", ") + "]")

  override fun toString() = toStringHelper().toString()

  /**
   * Represents a literal node in a brigadier command tree.
   */
  class Literal(val literal: String) : CommandNode() {

    override fun toStringHelper() = super.toStringHelper()
        .addFirst("literal", this.literal)
  }

  /**
   * Represents a node that redirects to another one.
   */
  class Redirect(val redirect: CommandNode) : CommandNode() {

    override fun toStringHelper() = super.toStringHelper()
        .addFirst("redirect", this.redirect)
  }

  /**
   * Represents a root command node.
   */
  class Root() : CommandNode()

  /**
   * Represents an argument command node.
   */
  class Argument(
      val name: String,
      val parser: BrigadierParser,
      val suggester: BrigadierSuggester?
  ) : CommandNode() {

    override fun toStringHelper() = super.toStringHelper()
        .addFirst("suggester", this.suggester?.id)
        .addFirst("parser", this.parser.id)
        .addFirst("name", this.name)
  }
}
