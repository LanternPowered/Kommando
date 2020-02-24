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

import org.lanternpowered.kommando.Message
import org.lanternpowered.kommando.argument.Argument
import org.lanternpowered.kommando.argument.ArgumentParseContext
import org.lanternpowered.kommando.suggestion.Suggestion

internal class CommandExecutionContext<out S>(
    override val source: S,
    override val input: String
) : ArgumentReaderImpl(input), ArgumentParseContext<S> {

  fun pushPotentialError(ex: Exception) {
  }

  fun pushPotentialError(ex: Message) {
  }

  override fun <T> Argument<T, in S>.parse() = this.parse(this@CommandExecutionContext)

  override fun <T> Argument<T, in S>.tryParseOrReset(): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> Argument<T, in S>.tryParseAndReset(): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> Argument<T, in S>.suggest() = this.suggest(this@CommandExecutionContext)

  override fun suggestion(range: IntRange, value: String, tooltip: Message?): Suggestion {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun suggestion(value: String, tooltip: Message?): Suggestion {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}
