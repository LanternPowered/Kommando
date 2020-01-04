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
 * Represents an option.
 */
@CommandDsl
interface Option<T, S> {

  /**
   * Converts this option to a repeatable option.
   */
  fun repeatable(): Repeatable<T, S>

  /**
   * Represents a repeatable option.
   */
  @CommandDsl
  interface Repeatable<T, S>

  /**
   * Represents an option that has a fallback
   * value if the option isn't specified.
   */
  @CommandDsl
  interface Defaulted<T, S>
}

/**
 * Represents options where the result value
 * will be mapped to the same type. This can
 * be useful for filtering or conversions.
 */
@CommandDsl
interface MappedOptions<T, S>

@CommandDsl
interface MappedOptionsBuilder<T, S> {

  /**
   * Creates a new argument.
   */
  fun argument(builder: ArgumentBuilder<T, S>.() -> ArgumentBuilder.ConvertFunction): BuiltArgument<T, S>

  @CommandDsl
  interface Option<T, S> {

    /**
     * Converts this option to a repeatable option.
     */
    fun repeatable(): Repeatable<T, S>

    /**
     * Represents a repeatable option.
     */
    @CommandDsl
    interface Repeatable<T, S>
  }

  /**
   * Adds the argument as an option.
   *
   * @param name The primary name of the option
   * @param more The secondary names of the option
   */
  fun Argument<T, S>.option(name: String, vararg more: String): Option<T, S>

  /**
   * Adds the argument as an option.
   *
   * @param name The primary name of the option
   * @param more The secondary names of the option
   */
  fun NamedArgument<T, S>.option(name: String, vararg more: String): Option<T, S>

  /**
   * Creates an option from the argument.
   *
   * @param name The primary name of the option
   * @param more The secondary names of the option
   */
  fun <S> BuiltArgument<T, S>.option(name: String, vararg more: String): Option<T, S>

  /**
   * Names the argument with the specified name.
   */
  fun <T, S> Argument<T, S>.named(name: String): NamedArgument<T, S>
}

@CommandDsl
interface OptionAwareBuilder<S> : ArgumentBuilderAwareBuilder<S> {

  /**
   * Builds a options object where all the arguments
   * are converted to the same result type.
   *
   * @param builder The builder function
   * @return The mapped options
   */
  fun <T> options(builder: MappedOptionsBuilder<T, S>.() -> Unit): MappedOptions<T, S>

  /**
   * Registers a new option to the command builder.
   *
   * Accessing the option value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> MappedOptions<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, List<T>>

  /**
   * Registers a new option to the command builder.
   *
   * Accessing the option value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> Option<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T?>

  /**
   * Registers a new option to the command builder.
   *
   * Accessing the option value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> Option.Defaulted<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T>

  /**
   * Registers a new repeatable option to the command builder. The name of the property will
   * be used as base name for the option.
   *
   * Accessing the option value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> Option.Repeatable<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, List<T>>

  /**
   * Makes the option return a default value if the option isn't specified.
   */
  fun <T, S> Option<T, S>.default(defaultValue: T): Option.Defaulted<T, S>

  /**
   * Makes the option return a default value if the option isn't specified.
   */
  fun <T, S> Option<T, S>.defaultBy(defaultValue: () -> T): Option.Defaulted<T, S>

  /**
   * Creates an option from the argument.
   *
   * @param name The primary name of the option
   * @param more The secondary names of the option
   */
  fun <T, S> Argument<T, S>.option(name: String, vararg more: String): Option<T, S>

  /**
   * Creates an option from the argument.
   *
   * @param name The primary name of the option
   * @param more The secondary names of the option
   */
  fun <T, S> BuiltArgument<T, S>.option(name: String, vararg more: String): Option<T, S>

  /**
   * Creates an option from the argument.
   *
   * @param name The primary name of the option
   * @param more The secondary names of the option
   */
  fun <T, S> NamedArgument<T, S>.option(name: String, vararg more: String): Option<T, S>
}
