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

import org.lanternpowered.kommando.argument.Argument
import org.lanternpowered.kommando.argument.ArgumentBuilder
import org.lanternpowered.kommando.argument.ArgumentParseContext
import org.lanternpowered.kommando.argument.BaseName
import org.lanternpowered.kommando.argument.ParseResult

internal class ArgumentProperty<T>(
    val argument: Argument<T, *>,
    val name: String?
) : ValueProperty<T>()

internal class ArgumentImpl<T, S>(
    private val parse: ArgumentParseContext<S>.() -> ParseResult<T>,
    private val suggest: ArgumentParseContext<S>.() -> List<String>
) : Argument<T, S> {

  override fun parse(context: ArgumentParseContext<S>) = context.parse()
  override fun suggest(context: ArgumentParseContext<S>) = context.suggest()
}

internal class ArgumentBuilderImpl<T, S> : ArgumentBuilder<T, S> {

  private var parse: (ArgumentParseContext<S>.() -> ParseResult<T>)? = null
  private var suggest: (ArgumentParseContext<S>.() -> List<String>)? = null

  fun build(): Argument<T, S> {
    val parse = checkNotNull(this.parse) { "The parse function must be set." }
    val suggest = this.suggest ?: { listOf() }
    return ArgumentImpl(parse, suggest)
  }

  override fun parse(fn: ArgumentParseContext<S>.() -> ParseResult<T>) {
    this.parse = fn
  }

  override fun suggest(fn: ArgumentParseContext<S>.() -> List<String>) {
    this.suggest = fn
  }

  override fun name(fn: BaseName.() -> String) {
    // TODO
  }
}
