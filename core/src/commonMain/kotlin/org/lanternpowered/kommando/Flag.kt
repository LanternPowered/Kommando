/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando

import org.lanternpowered.kommando.argument.Argument
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Represents a flag.
 */
@CommandDsl
interface Flag<T, S> {

  /**
   * Converts this flag to a repeatable flag.
   */
  fun repeatable(): Repeatable<T, S>

  /**
   * Represents a repeatable flag.
   */
  @CommandDsl
  interface Repeatable<T, S>

  @CommandDsl
  interface Defaulted<T, S>
}

@CommandDsl
interface FlagAwareBuilder<S> {

  /**
   * Registers a new flag to the command builder.
   *
   * Accessing the argument value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> Flag<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T?>

  /**
   * Registers a new flag to the command builder.
   *
   * Accessing the argument value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> Flag.Defaulted<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T>

  /**
   * Registers a new flag to the command builder.
   *
   * Accessing the argument value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> Flag.Repeatable<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, List<T>>

  /**
   * Creates a new flag.
   */
  fun flag(name: String, vararg more: String): Flag.Defaulted<Boolean, S>

  /**
   * Converts the argument into an flag.
   *
   * @param name The primary name of the flag
   * @param more The secondary names of the flag
   */
  fun <T, S> Argument<T, S>.flag(name: String, vararg more: String): Flag<T, S>

  /**
   * Makes the flag return a default value if the flag isn't specified.
   */
  fun <T, S> Flag<T, S>.default(defaultValue: T): Flag.Defaulted<T, S>

  /**
   * Makes the flag return a default value if the flag isn't specified.
   */
  fun <T, S> Flag<T, S>.defaultBy(defaultValue: () -> T): Flag.Defaulted<T, S>
}
