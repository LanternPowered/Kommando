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

/**
 * Constructs a argument that parses the argument optionally.
 */
fun <T, S> Argument<T, S>.optional(): Argument<T?, S> = argument {
  val argument = this@optional
  parse {
    try {
      argument.parse().asNullable()
    } catch (ex: ArgumentParseException) {
      result(null, ex.error)
    }
  }
  suggest {
    argument.suggest()
  }
}

fun <T, S> Argument<T?, S>.defaultBy(defaultValue: ArgumentParseContext<S>.() -> T): Argument<T, S> = argument {
  val argument = this@defaultBy
  parse {
    argument.parse().map { value -> value ?: defaultValue() }
  }
  suggest {
    argument.suggest()
  }
}

fun <T, S> Argument<T?, S>.default(defaultValue: T): Argument<T, S> = defaultBy { defaultValue }
