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
   * Parses the argument.
   */
  fun parse(context: ArgumentParseContext<S>): ParseResult<T>

  /**
   * Generates suggestions for the current argument that is being parsed.
   */
  fun suggest(context: ArgumentParseContext<S>): List<Suggestion> = emptyList()

  /**
   * Generates a proper name for the given argument based on the base name.
   */
  fun transformName(baseName: String): String = baseName
}
