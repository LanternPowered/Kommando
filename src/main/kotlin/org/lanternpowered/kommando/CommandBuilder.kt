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
import org.lanternpowered.kommando.argument.ArgumentBuilder
import org.lanternpowered.kommando.impl.CommandBuilderImpl
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Constructs a new command.
 */
fun <S> commandOf(fn: CommandBuilder<S>.() -> Unit): Command<S> {
  return CommandBuilderImpl<S>().apply(fn).build()
}

interface Named {

  /**
   * The name.
   */
  val name: String
}

@CommandDsl
interface NamedArgument<T, S> : Named {

  /**
   * The argument.
   */
  val argument: Argument<T, S>
}

@CommandDsl
interface NamedOption<T, S> : Named {

  /**
   * The option.
   */
  val option: Option<T, S>
}

@CommandDsl
interface Option<T, S> {

  /**
   * The argument.
   */
  val argument: Argument<T, S>
}

@CommandDsl
interface Flag

@CommandDsl
interface NamedFlag : Named {

  /**
   * The flag.
   */
  val flag: Flag
}

@CommandDsl
interface CommandBuilder<S> {

  /**
   * Gets the property that represents the
   * source of the command execution.
   */
  fun source(): ReadOnlyProperty<Any?, S>

  /**
   * Registers a new flag to the command builder. The name of the property will
   * be used as base name for the flag.
   *
   * Accessing the argument value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun Flag.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, Boolean>

  /**
   * Registers a new flag to the command builder.
   *
   * Accessing the argument value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun NamedFlag.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, Boolean>

  /**
   * Registers a new argument to the command builder. The name of the property will
   * be used as base name for the argument.
   *
   * Accessing the argument value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> Argument<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T>

  /**
   * Registers a new named argument to the command builder.
   *
   * Accessing the argument value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> NamedArgument<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T>

  /**
   * Registers a new option to the command builder. The name of the property will
   * be used as base name for the argument.
   *
   * Accessing the argument value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> Option<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T>

  /**
   * Registers a new named option to the command builder.
   *
   * Accessing the argument value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> NamedOption<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T>

  /**
   * Creates a new flag.
   */
  fun flag(): Flag

  /**
   * Names the flag with the specified name.
   */
  fun Flag.name(name: String, vararg aliases: String): NamedFlag

  /**
   * Converts the argument into an option.
   */
  fun <T, S> Argument<T, S>.option(): Option<T?, S>

  /**
   * Makes the option return a default value if the option isn't specified.
   */
  fun <T, S> Option<T?, S>.default(defaultValue: T): Option<T, S>

  /**
   * Names the option with the specified name.
   */
  fun <T, S> Option<T, S>.name(name: String, vararg aliases: String): NamedOption<T, S>

  /**
   * Names the argument with the specified name.
   */
  fun <T, S> Argument<T, S>.name(name: String): NamedArgument<T, S>

  /**
   * Constructs a new [Argument].
   */
  fun <T> argumentOf(fn: ArgumentBuilder<T, S>.() -> Unit): Argument<T, S>

  /**
   * Sets the executor for the current command, doesn't affect sub-commands.
   */
  fun execute(fn: CommandExecutionContext.() -> Unit)

  /**
   * Builds and registers a sub-command.
   */
  operator fun String.invoke(fn: SubCommandBuilder<S>.() -> Unit) {
    child(this, fn)
  }

  /**
   * Builds and registers a sub-command.
   */
  fun child(name: String, fn: SubCommandBuilder<S>.() -> Unit)

  /**
   * Adds a child command.
   *
   * @param name The name of the child command
   * @param command The sub-command
   */
  fun child(name: String, command: Command<in S>, fn: SubCommandSettingsBuilder.() -> Unit = {})
}

@CommandDsl
interface CommandExecutionContext

interface SubCommandBuilder<S> : CommandBuilder<S>, SubCommandSettingsBuilder

@CommandDsl
interface SubCommandSettingsBuilder {

  fun alias(name: String)
}
