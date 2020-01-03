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
 * Represents a flag.
 */
interface TreeFlag<S> {

  /**
   * The names of the flag.
   */
  val names: FlagNames

  /**
   * Represents a flag that is backed by an argument.
   */
  interface Argument<S> : TreeFlag<S> {

    /**
     * The argument that will be parsed.
     */
    val argument: ParserArgument<*, S>
  }

  /**
   * Represents a flag where its value will be true if the
   * flag is specified and false otherwise.
   */
  interface TrueIfPresent<S> : TreeFlag<S>
}
