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

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A base classes for local choices objects in the builder.
 */
@Suppress("ClassName")
abstract class options : Argument<Any, Any> {

  override fun parse(context: ArgumentParseContext<Any>): ParseResult<Any> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun suggest(context: ArgumentParseContext<Any>): List<String> {
    return super.suggest(context)
  }

  protected operator fun <T, S> Argument<T, S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T?> {
    TODO()
  }

  protected operator fun <T, S> RepeatableOption<T, S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, List<T>> {
    TODO()
  }

  protected operator fun <T, S> NamedOption<T, S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T?> {
    TODO()
  }

  protected operator fun <T, S> NamedRepeatableOption<T, S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, List<T>> {
    TODO()
  }

  /**
   * Marks the option as repeatable, which means that it
   * can be specified multiple times.
   */
  fun <T, S> Argument<T, S>.repeatable() = this@options.RepeatableOption(this)

  /**
   * Represents an option that can be repeated multiple times.
   */
  inner class RepeatableOption<T, S>(val argument: Argument<T, S>)

  /**
   * Uses the specified name for the given value.
   */
  infix fun <T, S> Argument<T, S>.named(name: String) = this@options.NamedOption(name, this)

  /**
   * Represents an option that has been named.
   */
  inner class NamedOption<T, S>(val name: String, val argument: Argument<T, S>)

  /**
   * Uses the specified name for the given value.
   */
  infix fun <T, S> RepeatableOption<T, S>.named(name: String) = this@options.NamedRepeatableOption(name, this)

  /**
   * Represents a repeatable option that has been named.
   */
  inner class NamedRepeatableOption<T, S>(val name: String, val repeatableOption: RepeatableOption<T, S>)
}
