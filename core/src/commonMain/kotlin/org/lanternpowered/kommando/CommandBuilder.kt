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
import org.lanternpowered.kommando.impl.CommandBuilderImpl
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Constructs a new command.
 */
fun <S> command(fn: CommandBuilder<S>.() -> Unit): Command<S>
    = CommandBuilderImpl<S>().apply(fn).build()

@CommandDsl
interface NamedArgument<T, S>

@CommandDsl
interface BuiltArgument<T, S>

/**
 * Represents a flag.
 */
@CommandDsl
interface Flag<T, S>

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

  @CommandDsl
  interface Defaulted<T, S>
}

@CommandDsl
interface Source<S> {

  /**
   * Validates the source.
   */
  fun validate(fn: ValidationContext.(source: S) -> Unit): Source<S>

  /**
   * Converts the source.
   */
  fun <R> convert(fn: ValidationContext.(source: S) -> R): Source<R>
}

@CommandDsl
interface CommandBuilder<S> : BaseCommandBuilder<S>, ExecutableCommandBuilder

@CommandDsl
interface BaseCommandBuilder<S> : ArgumentAwareBuilder<S>, OptionAwareBuilder<S>, FlagAwareBuilder<S> {

  /**
   * Gets the property that represents the
   * source of the command execution.
   */
  fun source(): Source<S>

  /**
   * Gets a delegate to access the source.
   */
  operator fun <S> Source<S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, S>

  /**
   * Builds and registers a sub-command.
   */
  operator fun String.invoke(fn: CommandBuilder<S>.() -> Unit) {
    subcommand(this, fn)
  }

  /**
   * Builds and registers a sub-command.
   */
  infix fun String.execute(fn: ExecutionContext.() -> Unit) {
    subcommand(this) {
      execute(fn)
    }
  }

  /**
   * Builds and registers a sub-command.
   */
  fun subcommand(name: String, aliases: List<String>): ExecutableCommandBuilder

  /**
   * Builds and registers a sub-command.
   */
  fun subcommand(name: String): ExecutableCommandBuilder
      = subcommand(name, listOf())

  /**
   * Builds and registers a sub-command.
   */
  fun subcommand(name: String, vararg aliases: String): ExecutableCommandBuilder
      = subcommand(name, aliases.toList())

  /**
   * Builds and registers a sub-command.
   */
  fun subcommand(name: String, fn: CommandBuilder<S>.() -> Unit) {
    subcommand(name, listOf(), fn)
  }

  /**
   * Builds and registers a sub-command.
   */
  fun subcommand(name: String, aliases: List<String>, fn: CommandBuilder<S>.() -> Unit)

  /**
   * Builds and registers a sub-command.
   */
  fun subcommand(name: String, vararg aliases: String, fn: CommandBuilder<S>.() -> Unit) {
    subcommand(name, aliases.toList(), fn)
  }

  /**
   * Adds a sub-command.
   *
   * @param name The name of the sub-command
   * @param aliases The aliases of the sub-command
   * @param command The sub-command
   */
  fun subcommand(name: String, aliases: List<String>, command: Command<in S>)

  /**
   * Adds a sub-command.
   *
   * @param name The name of the child command
   * @param command The sub-command
   */
  fun subcommand(name: String, command: Command<in S>) {
    subcommand(name, listOf(), command)
  }

  /**
   * Adds a sub-command.
   *
   * @param name The name
   * @param alias The alias
   * @param command The sub-command
   */
  fun subcommand(name: String, alias: String, command: Command<in S>) {
    subcommand(name, listOf(alias), command)
  }

  /**
   * Adds a sub-command.
   *
   * @param name The name
   * @param alias1 The first alias
   * @param alias2 The second alias
   * @param command The sub-command
   */
  fun subcommand(name: String, alias1: String, alias2: String, command: Command<in S>) {
    subcommand(name, listOf(alias1, alias2), command)
  }

  /**
   * Adds a sub-command.
   *
   * @param name The name
   * @param alias1 The first alias
   * @param alias2 The second alias
   * @param alias3 The third alias
   * @param command The sub-command
   */
  fun subcommand(name: String, alias1: String, alias2: String, alias3: String, command: Command<in S>) {
    subcommand(name, listOf(alias1, alias2, alias3), command)
  }

  /**
   * Adds a sub-command.
   *
   * @param name The name
   * @param alias1 The first alias
   * @param alias2 The second alias
   * @param alias3 The third alias
   * @param alias4 The fourth alias
   * @param command The sub-command
   */
  fun subcommand(name: String, alias1: String, alias2: String, alias3: String, alias4: String, command: Command<in S>) {
    subcommand(name, listOf(alias1, alias2, alias3, alias4), command)
  }

  /**
   * Adds a sub-command.
   *
   * @param name The name
   * @param alias1 The first alias
   * @param alias2 The second alias
   * @param alias3 The third alias
   * @param alias4 The fourth alias
   * @param alias5 The fifth alias
   * @param command The sub-command
   */
  fun subcommand(name: String, alias1: String, alias2: String, alias3: String, alias4: String, alias5: String, command: Command<in S>) {
    subcommand(name, listOf(alias1, alias2, alias3, alias4, alias5), command)
  }

  /**
   * This function is useful is certain sub-commands require some
   * arguments after the name of the sub-command.
   * For example: '/name a|b <my-argument>' where a and b
   * are sub-commands.
   */
  fun groupBefore(fn: BaseCommandBuilder<S>.() -> Unit)

  /**
   * This function is useful is certain sub-commands require some
   * arguments before the name of the sub-command.
   * For example: '/name <my-argument> a|b' where a and b
   * are sub-commands.
   */
  fun groupAfter(fn: BaseCommandBuilder<S>.() -> Unit)
}

@CommandDsl
interface ExecutableCommandBuilder {

  /**
   * Sets the executor for the current command, doesn't affect sub-commands.
   */
  infix fun execute(fn: ExecutionContext.() -> Unit)
}

@CommandDsl
interface FlagAwareBuilder<S> {

  /**
   * Registers a new flag to the command builder. The name of the property will
   * be used as base name for the flag.
   *
   * Accessing the argument value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> Flag<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T>

  /**
   * Creates a new flag.
   */
  fun flag(name: String, vararg more: String): Flag<Boolean, S>

  /**
   * Converts the argument into an flag.
   *
   * @param name The primary name of the flag
   * @param more The secondary names of the flag
   */
  fun <T, S> Argument<T, S>.flag(name: String, vararg more: String): Flag<T?, S>

  /**
   * Makes the flag return a default value if the flag isn't specified.
   */
  fun <T, S> Flag<T?, S>.default(defaultValue: T): Flag<T, S>

  /**
   * Makes the flag return a default value if the flag isn't specified.
   */
  fun <T, S> Flag<T?, S>.defaultBy(defaultValue: () -> T): Flag<T, S>
}

@CommandDsl
interface OptionAwareBuilder<S> {

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
   * Creates a new option.
   *
   * @param name The primary name of the option
   * @param more The secondary names of the option
   * @param builder The builder function of the backing arguments
   */
  fun <T> option(name: String, vararg more: String,
      @BuilderInference builder: ArgumentBuilder<T, S>.() -> ArgumentBuilder.ConvertFunction
  ): Option<T, S>

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
  fun <T, S> NamedArgument<T, S>.option(name: String, vararg more: String): Option<T, S>
}

@CommandDsl
interface ArgumentAwareBaseBuilder<S> {

  /**
   * Registers a new argument to the command builder. The name of the property will
   * be used as base name for the argument.
   *
   * Accessing the argument value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> Argument<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T>

  /**
   * Registers a new named argument to the command builder.
   *
   * Accessing the argument value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> NamedArgument<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T>

  /**
   * Registers a new named argument to the command builder.
   *
   * Accessing the argument value outside the execution of the command will result
   * in an [IllegalStateException].
   */
  operator fun <T> BuiltArgument<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T>

  /**
   * Names the argument with the specified name.
   */
  fun <T, S> Argument<T, S>.named(name: String): NamedArgument<T, S>
}

@CommandDsl
interface ArgumentAwareBuilder<S> : ArgumentAwareBaseBuilder<S> {

  /**
   * Creates a new argument.
   */
  fun <T> argument(
      @BuilderInference builder: ArgumentBuilder<T, S>.() -> ArgumentBuilder.ConvertFunction
  ): BuiltArgument<T, S>
}

/**
 * The builder for arguments based on other arguments.
 */
@CommandDsl
interface ArgumentBuilder<T, S> : ArgumentAwareBaseBuilder<S> {

  /**
   * Specifies the conversion function where multiple
   * argument values can be converted into a new result.
   */
  fun convert(fn: () -> T): ConvertFunction

  /**
   * Represents the convert function.
   */
  interface ConvertFunction
}
