/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("UNCHECKED_CAST")

package org.lanternpowered.kommando.argument

import org.lanternpowered.kommando.suggestion.Suggestion

/**
 * Constructs a argument that parses the argument optionally.
 */
fun <T, S> Argument<T, S>.optional(): OptionalArgument<T, S> =
    (this as? OptionalArgument<T, S>)?.argument?.optional()
        ?: OptionalArgument(this)

/**
 * An argument that parses arguments optionally.
 *
 * @property argument The argument that is being parsed optionally
 */
class OptionalArgument<T, S> internal constructor(
    val argument: Argument<T, S>
) : Argument<T?, S> {

  override val usage: ArgumentUsage = argument.usage.copy(optional = true)

  override fun suggest(context: ArgumentParseContext<S>) = context.run {
    argument.suggest()
  }

  override fun parse(context: ArgumentParseContext<S>): ParseResult<T?> = context.run {
    try {
      argument.parse().asNullable()
    } catch (ex: ArgumentParseException) {
      result(null, ex.error)
    }
  }
}

/**
 * Constructs en argument that falls back to a default value if the
 * backing argument is parsed as null.
 */
fun <T, S> Argument<T?, S>.default(defaultValue: T): DefaultedOptionalArgument<T, S> = defaultBy { defaultValue }

/**
 * Constructs en argument that falls back to a default value if the
 * backing argument is parsed as null.
 */
fun <T, S> Argument<T?, S>.defaultBy(defaultValue: ArgumentParseContext<S>.() -> T): DefaultedOptionalArgument<T, S> =
    (this as? DefaultedOptionalArgument<T, S>)?.argument?.defaultBy(defaultValue)
        ?: DefaultedOptionalArgument(this, defaultValue)

/**
 * An argument that falls back to a default value if the
 * backing argument is parsed as null.
 *
 * @property argument The argument that is being parsed optionally
 * @property default The default value that will be defaulted to
 */
class DefaultedOptionalArgument<T, S> internal constructor(
    val argument: Argument<T?, S>,
    val default: ArgumentParseContext<S>.() -> T
) : Argument<T, S> {

  override val usage: ArgumentUsage
    get() = argument.usage

  override fun parse(context: ArgumentParseContext<S>): ParseResult<T> = context.run {
    argument.parse().map { value -> value ?: default() }
  }

  override fun suggest(context: ArgumentParseContext<S>): List<Suggestion> = context.run {
    argument.suggest()
  }
}
