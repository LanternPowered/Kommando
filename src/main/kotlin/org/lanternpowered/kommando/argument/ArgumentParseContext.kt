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

import org.lanternpowered.kommando.CommandContext
import org.lanternpowered.kommando.Message
import org.lanternpowered.kommando.ValidationContext

interface ArgumentParseContext<S> : CommandContext<S>, ValidationContext {

  fun <T> result(value: T, potentialError: Message? = null) = ParseResult(value, potentialError)

  fun <T> Argument<T, in S>.parse(): ParseResult<T>

  /**
   * Tries to parse the argument, returns true if it was successful
   * and false if a failure. In case of a failure, the reading cursor
   * will be reset to the state before parsing.
   */
  fun <T> Argument<T, in S>.tryParse(): Boolean

  fun <T> Argument<T, in S>.suggest(): List<String>

  /**
   * The cursor position of the reader.
   */
  var cursor: Int

  /**
   * Moves the cursor to the next
   * non-whitespace character.
   */
  fun skipWhitespaces()

  /**
   * Parses the next argument as a [String].
   */
  fun parseString(): String

  /**
   * Parses the next argument as a unquoted [String].
   */
  fun parseUnquotedString(): String

  fun parseChar(): Char

  fun parseInt(): Int

  fun parseDouble(): Double

  fun parseFloat(): Float

  fun parseBoolean(): Boolean

  fun parseLong(): Long
}
