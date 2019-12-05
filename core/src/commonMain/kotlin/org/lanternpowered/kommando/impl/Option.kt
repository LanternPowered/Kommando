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
import org.lanternpowered.kommando.Option
import org.lanternpowered.kommando.argument.Argument
import org.lanternpowered.kommando.argument.ArgumentParseContext
import org.lanternpowered.kommando.argument.ParseResult

internal class OptionNames(
    val long: List<String>,
    val short: List<Char>
)

internal abstract class BaseOption<T, S>(val names: OptionNames) : Argument<T, S> {

  abstract fun getDefaultValue(): T
}

internal class BaseOptionProperty<T>(val baseOption: BaseOption<T, *>) : ValueProperty<T>()

internal class OptionImpl<T, S>(
    val argument: Argument<T, S>, private val defaultValue: (() -> T)?, names: OptionNames
) : BaseOption<T, S>(names), Option<T, S> {

  override fun parse(context: ArgumentParseContext<S>) = this.argument.parse(context)
  override fun suggest(context: ArgumentParseContext<S>) = this.argument.suggest(context)
  @Suppress("UNCHECKED_CAST")
  override fun getDefaultValue(): T = this.defaultValue?.invoke() ?: null as T
}

internal class FlagImpl<S>(names: OptionNames) : BaseOption<Boolean, S>(names), Flag {

  override fun parse(context: ArgumentParseContext<S>) = ParseResult(true)
  override fun suggest(context: ArgumentParseContext<S>) = listOf<String>()
  override fun getDefaultValue() = false
}
