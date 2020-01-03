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

import org.lanternpowered.kommando.argument.Argument as ParserArgument

/**
 * Represents an element in a command tree.
 */
interface TreeElement<S> {

  /**
   * The children elements of this element, if any.
   */
  val children: List<TreeElement<S>>

  /**
   * The flags that are available in the tree
   * structure up to this element.
   */
  val flags: List<TreeFlag<S>>

  /**
   * Whether the tree is up to this element executable. This doesn't
   * account for possible optional arguments. This only means that a
   * executor was applied to the tree element, usually the leaves of
   * the command tree.
   */
  val executable: Boolean

  /**
   * Represents the root element of a tree.
   */
  interface Root<S> : TreeElement<S>

  /**
   * Represents a literal element, this is used by sub-commands.
   */
  interface Literal<S> : TreeElement<S> {

    /**
     * The literal value.
     */
    val literal: String
  }

  /**
   * Represents a argument element.
   */
  interface Argument<S> : TreeElement<S> {

    /**
     * The argument that will be parsed.
     */
    val argument: ParserArgument<*, S>
  }
}
