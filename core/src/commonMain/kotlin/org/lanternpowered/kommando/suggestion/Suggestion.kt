/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando.suggestion

import org.lanternpowered.kommando.Message
import org.lanternpowered.kommando.impl.suggestion.SuggestionImpl

/**
 * Constructs a new [Suggestion].
 */
fun suggestion(range: IntRange, value: String, tooltip: Message? = null): Suggestion {
  return SuggestionImpl(range, value, tooltip)
}

/**
 * Represents a suggestion.
 */
interface Suggestion {

  /**
   * The range of character indexes that are
   * affected by the suggestion.
   */
  val range: IntRange

  /**
   * The value that will be suggested.
   */
  val value: String

  /**
   * The tooltip of the suggested value, if any.
   */
  val tooltip: Message?
}
