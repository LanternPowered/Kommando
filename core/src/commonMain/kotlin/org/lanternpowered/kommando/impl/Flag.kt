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

import org.lanternpowered.kommando.Flag
import org.lanternpowered.kommando.argument.Argument as ParserArgument
import org.lanternpowered.kommando.argument.ArgumentParseContext
import org.lanternpowered.kommando.argument.ArgumentUsage
import org.lanternpowered.kommando.argument.ParseResult
import org.lanternpowered.kommando.suggestion.Suggestion

internal abstract class FlagImpl<T, S>(val flagNames: FlagNamesImpl) : ParserArgument<T, S> {

  abstract fun getDefaultValue(): T

  internal class Argument<T, S>(
      val argument: ParserArgument<T, S>, private val defaultValue: (() -> T)?, names: FlagNamesImpl
  ) : FlagImpl<T, S>(names), Flag<T, S> {

    override val usage: ArgumentUsage = ArgumentUsage("flags")

    override fun parse(context: ArgumentParseContext<S>) = this.argument.parse(context)
    override fun suggest(context: ArgumentParseContext<S>) = this.argument.suggest(context)
    @Suppress("UNCHECKED_CAST")
    override fun getDefaultValue(): T = this.defaultValue?.invoke() ?: null as T

    override fun repeatable(): Flag.Repeatable<T, S> {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
  }

  class TrueIfPresent<S>(names: FlagNamesImpl) : FlagImpl<Boolean, S>(names), Flag<Boolean, S> {

    override val usage: ArgumentUsage = ArgumentUsage("flags")

    override fun parse(context: ArgumentParseContext<S>) = ParseResult(true)
    override fun suggest(context: ArgumentParseContext<S>) = listOf<Suggestion>()
    override fun getDefaultValue() = false

    override fun repeatable(): Flag.Repeatable<Boolean, S> {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
  }
}

internal class FlagProperty<T>(val flag: FlagImpl<T, *>) : ValueProperty<T>()
