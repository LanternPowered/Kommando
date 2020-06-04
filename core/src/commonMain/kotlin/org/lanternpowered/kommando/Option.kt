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
interface Option {

  /**
   * Converts this option to a repeatable option.
   */
  fun repeatable(): Repeatable

  /**
   * Represents a repeatable option.
   */
  @CommandDsl
  interface Repeatable
}

@CommandDsl
interface BoundOption<T, S> {

  /**
   * Represents a repeatable bound option.
   */
  @CommandDsl
  interface Repeatable<T, S>
}

/**
 * Represents options where the result value
 * will be mapped to the same type. This can
 * be useful for filtering or conversions.
 */
@CommandDsl
interface MappedOptions<T, S>

@CommandDsl
interface MappedOptionsBuilder<T, S> : BaseBuilder {

  operator fun String.invoke(builder: ArgumentBuilder<T, S>.() -> Unit)

  operator fun Path.invoke(builder: ArgumentBuilder<T, S>.() -> Unit)

  fun String.repeatable(): Option.Repeatable

  infix fun Option.with(builder: ArgumentBuilder<T, S>.() -> Unit)

  infix fun Option.Repeatable.with(builder: ArgumentBuilder<T, S>.() -> Unit)

  infix fun Option.with(argument: Argument<out T, in S>)

  infix fun Option.with(argument: BuiltArgument<out T, in S>)

  infix fun Option.Repeatable.with(argument: Argument<out T, in S>)

  infix fun Option.Repeatable.with(argument: BuiltArgument<out T, in S>)

  infix fun String.with(argument: Argument<out T, in S>)

  infix fun String.with(argument: BuiltArgument<out T, in S>)

  infix fun Path.with(argument: Argument<out T, in S>)

  infix fun Path.with(argument: BuiltArgument<out T, in S>)
}

@CommandDsl
interface OptionAwareBuilder<S> : ArgumentBuilderAwareBuilder<S>, PathAwareBuilder {

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
  operator fun <T> BoundOption<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T?>

  /**
   * Registers a new repeatable option to the command builder. The name of the property will
   * be used as base name for the option.
   *
   * Accessing the option value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> BoundOption.Repeatable<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, List<T>>

  /**
   * Registers a new option to the command builder.
   *
   * Accessing the option value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun Option.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, Boolean>

  /**
   * Registers a new option to the command builder.
   *
   * Accessing the option value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun Option.Repeatable.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, Int>

  /**
   * Creates a new option.
   */
  fun String.option(): Option

  /**
   * Creates a new option.
   */
  fun Path.option(): Option

  /**
   * Represents an option that is bound to an argument.
   */
  infix fun <T, S> Option.with(argument: Argument<T, S>): BoundOption<T, S>

  /**
   * Represents an option that is bound to an argument.
   */
  infix fun <T, S> Option.with(argument: BuiltArgument<T, S>): BoundOption<T, S>

  /**
   * Represents an option that is bound to an argument.
   */
  infix fun <T, S> Option.Repeatable.with(argument: Argument<T, S>): BoundOption.Repeatable<T, S>

  /**
   * Represents an option that is bound to an argument.
   */
  infix fun <T, S> Option.Repeatable.with(argument: BuiltArgument<T, S>): BoundOption.Repeatable<T, S>
}
