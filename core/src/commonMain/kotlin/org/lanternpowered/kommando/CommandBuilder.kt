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
interface BuiltArgument<T, S>

@CommandDsl
interface CommandBuilder<S> : BaseCommandBuilder<S> {

  /**
   * Sets the executor for the current command, doesn't affect sub-commands.
   */
  infix fun execute(fn: ExecutionContext.() -> Unit)
}

@CommandDsl
interface BaseBuilder {

  /**
   * @suppress
   */
  @Deprecated(message = "Not allowed.", level = DeprecationLevel.ERROR)
  infix fun <A, B> A.to(b: B)
}

@CommandDsl
interface BaseCommandBuilder<S> : SourceAwareBuilder<S>, ArgumentAwareBuilder<S>,
    ArgumentBuilderAwareBuilder<S>, OptionAwareBuilder<S>, FlagAwareBuilder<S>, PathAwareBuilder {

  infix fun String.then(other: Command<in S>)

  infix fun String.or(other: CommandBuilderWithPath): CommandBuilderWithPath

  infix fun String.then(other: CommandBuilderWithPath): CommandBuilderWithPath

  infix fun Path.then(other: Command<in S>)

  infix fun Path.or(other: CommandBuilderWithPath): CommandBuilderWithPath

  infix fun Path.then(other: CommandBuilderWithPath): CommandBuilderWithPath

  fun Path.beforeArguments(fn: CommandBuilder<S>.() -> Unit)

  fun String.beforeArguments(fn: CommandBuilder<S>.() -> Unit)

  /**
   * Builds and registers a sub-command.
   */
  operator fun String.invoke(fn: CommandBuilder<S>.() -> Unit): CommandBuilderWithPath

  operator fun Path.invoke(fn: CommandBuilder<S>.() -> Unit): CommandBuilderWithPath

  /**
   * Builds and registers a sub-command.
   */
  infix fun String.execute(fn: ExecutionContext.() -> Unit) {
    this {
      execute(fn)
    }
  }

  /**
   * Builds and registers a sub-command.
   */
  infix fun Path.execute(fn: ExecutionContext.() -> Unit) {
    this {
      execute(fn)
    }
  }

  /**
   * Represents a command builder that is bound to a path.
   */
  interface CommandBuilderWithPath

  /**
   * Apply changes to the builder within a group.
   */
  operator fun Group.invoke(fn: BaseCommandBuilder<S>.() -> Unit)
}

interface Group

interface Path

interface PathAwareBuilder {

  /**
   * Represents a group. This object can be used
   * to group certain actions. For example if two
   * sub-commands use the same arguments:
   * '/name <int> <int> a|b'
   */
  val group: Group

  /**
   * By default will arguments be parsed before
   * literal values. The returned group object
   * reverses this. For example:
   * '/name a|b <int> <int>'
   * (originally: '/name <int> <int> a|b')
   */
  val Group.beforeArguments: Group

  /**
   * Converts the [String] into a [Path]
   */
  val String.path: Path

  infix fun String.or(other: String): Path

  infix fun String.or(other: Path): Path

  infix fun String.then(other: Path): Path

  infix fun String.then(other: String): Path

  infix fun Path.or(other: String): Path

  infix fun Path.or(other: Path): Path

  infix fun Path.then(other: String): Path

  infix fun Path.then(other: Path): Path

  val Path.beforeArguments: Path

  val String.beforeArguments: Path

  val otherwise: Path
}

@CommandDsl
interface ArgumentAwareBuilder<S> : BaseBuilder {

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
  operator fun <T> BuiltArgument<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T>
}

@CommandDsl
interface ArgumentBuilderAwareBuilder<S> : BaseBuilder {

  /**
   * Creates a new argument.
   */
  fun <T> argument(
      @BuilderInference builder: ArgumentBuilder<T, S>.() -> Unit
  ): BuiltArgument<T, S>
}
