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

import org.lanternpowered.kommando.Path
import org.lanternpowered.kommando.argument.Argument as ArgumentParser

interface TreeBuilder {

  /**
   * Registers a path of literal values.
   */
  fun literals(path: Path): TreeBuilder

  /**
   * Registers a literal value.
   */
  fun literal(literal: String): TreeBuilder

  /**
   * Registers an argument.
   */
  fun <T, S> argument(name: String, argument: ArgumentParser<T, S>): TreeBuilder

  /**
   * Redirect to another element in the command tree.
   */
  fun redirectTo(builder: TreeBuilder)

  /**
   * Registers an argument. This basically re-registers
   * the argument under a different path. If the builder
   * already exists on the current path, nothing will change
   * an itself will be returned.
   */
  fun argument(argument: Argument): TreeBuilder

  /**
   * Represents a tree builder element that an argument is bound to.
   */
  interface Argument : TreeBuilder {

    /**
     * The name of the argument.
     */
    val name: String

    /**
     * The parser.
     */
    val parser: ArgumentParser<*,*>
  }
}
