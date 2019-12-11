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
import org.lanternpowered.kommando.impl.ArgumentBuilderImpl

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
  fun suggest(context: ArgumentParseContext<S>): List<String> = emptyList()

  /**
   * Generates a proper name for the given argument based on the base name.
   */
  fun transformName(baseName: String): String = baseName
}

/**
 * Constructs a new [Argument].
 */
// TODO: Builder inference, once https://youtrack.jetbrains.com/issue/KT-35306 is fixed
fun <T, S> argument(fn: ArgumentBuilder<T, S>.() -> Unit): Argument<T, S> {
  return ArgumentBuilderImpl<T, S>().apply(fn).build()
}

@CommandDsl
interface ArgumentBuilder<T, S> {

  fun parse(fn: ArgumentParseContext<S>.() -> ParseResult<T>)

  fun suggest(fn: ArgumentParseContext<S>.() -> List<String>)

  fun name(fn: BaseName.() -> String)
}

inline class BaseName(val baseName: String)
