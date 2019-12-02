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
import org.lanternpowered.kommando.LiteralMessage
import org.lanternpowered.kommando.Message

interface ArgumentParseContext<S> : CommandContext<S>, ArgumentValidationContext {

  fun <T> success(value: T, potentialError: Message? = null) = ParseResult.Success(value, potentialError)

  fun <T> error(error: Message): ParseResult.Error<T> = ParseResult.Error(error)

  fun <T> error(error: String): ParseResult.Error<T> = error(LiteralMessage(error))

  fun <T> Argument<T, in S>.parse(): ParseResult<T>

  fun <T> Argument<T, S>.suggest(): List<String>

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
