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
import org.lanternpowered.kommando.suggestion.Suggestion

interface ArgumentParseContext<out S> : CommandContext<S>, ArgumentReader, ValidationContext {

  fun <T> result(value: T, potentialError: Message? = null) = ParseResult(value, potentialError)

  fun <T> Argument<T, in S>.parse(): ParseResult<T>

  /**
   * Tries to parse the argument, returns true if it was successful
   * and false if a failure. In case of a failure, the reading cursor
   * will be reset to the state before parsing.
   */
  fun <T> Argument<T, in S>.tryParseOrReset(): Boolean

  /**
   * Tries to parse the argument, returns true if it was successful
   * and false if a failure. Regardless of success or failure, the reading
   * cursor state will be reset after parsing.
   */
  fun <T> Argument<T, in S>.tryParseAndReset(): Boolean

  fun <T> Argument<T, in S>.suggest(): List<Suggestion>

  fun suggestion(range: IntRange, value: String, tooltip: Message? = null): Suggestion

  fun suggestion(value: String, tooltip: Message? = null): Suggestion
}
