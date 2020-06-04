/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando.argument

import org.lanternpowered.kommando.CommandDsl
import org.lanternpowered.kommando.suggestion.Suggestion

/**
 * Represents an argument that can be parsed.
 */
@CommandDsl
interface Argument<T, S> {

  /**
   * The usage of the argument.
   */
  val usage: ArgumentUsage

  /**
   * Parses the argument.
   */
  fun parse(context: ArgumentParseContext<S>): ParseResult<T>

  /**
   * Generates suggestions for the current argument that is being parsed.
   */
  fun suggest(context: ArgumentParseContext<S>): List<Suggestion> = emptyList()
}

/**
 * Represents a simple argument with a default name if one isn't specified.
 */
abstract class SimpleArgument<T, S>(
    name: String
) : Argument<T, S> {

  override val usage: ArgumentUsage = ArgumentUsage("<$name>")
}
