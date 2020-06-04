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

import org.lanternpowered.kommando.ArgumentBuilder
import org.lanternpowered.kommando.BaseCommandBuilder
import org.lanternpowered.kommando.BoundOption
import org.lanternpowered.kommando.BuiltArgument
import org.lanternpowered.kommando.Command
import org.lanternpowered.kommando.CommandBuilder
import org.lanternpowered.kommando.CommandContext
import org.lanternpowered.kommando.Flag
import org.lanternpowered.kommando.NamedArgument
import org.lanternpowered.kommando.ExecutionContext
import org.lanternpowered.kommando.Group
import org.lanternpowered.kommando.MappedOptions
import org.lanternpowered.kommando.MappedOptionsBuilder
import org.lanternpowered.kommando.Option
import org.lanternpowered.kommando.Path
import org.lanternpowered.kommando.Source
import org.lanternpowered.kommando.argument.Argument
import org.lanternpowered.kommando.tree.CommandTree
import org.lanternpowered.kommando.tree.TreeElement
import org.lanternpowered.kommando.tree.TreeFlag
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private val nameRegex = "^[A-Za-z0-9][A-Za-z0-9-_]*$".toRegex()

internal fun checkName(name: String) {
  if (nameRegex.matchEntire(name) != null)
    return
  throw IllegalArgumentException("Invalid name: $name, only the following characters are " +
      "supported [A-Z a-z 0-9 - _], the first character can only be one of [A-Z a-z 0-9]")
}

internal class CommandImpl<S> : Command<S> {

  override fun execute(context: CommandContext<S>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getCompletionSuggestions(context: CommandContext<S>): List<String> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}

internal class CommandBuilderImpl<S> : BaseCommandBuilderImpl<S>(), CommandBuilder<S> {

  override fun areArgumentRegistrationsAllowed(): Boolean {
    return super.areArgumentRegistrationsAllowed()
  }

  override fun execute(fn: ExecutionContext.() -> Unit) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}

internal open class BaseCommandBuilderImpl<S> : AbstractPathAwareBuilder<S>(),
    BaseCommandBuilder<S> {

  private val sourceProperties = mutableListOf<SourceProperty<S, *>>()
  private val arguments = mutableListOf<ArgumentProperty<*>>()
  private val commands = mutableListOf<Command<S>>()
  private val options = mutableListOf<FlagProperty<*>>()

  override fun <A, B> A.to(b: B) = throw UnsupportedOperationException()

  fun build(): Command<S> {
    TODO()
  }

  /**
   * Gets whether new argument/option/flag registrations are still allowed.
   */
  protected open fun areArgumentRegistrationsAllowed(): Boolean {
    return this.commands.isEmpty()
  }

  /**
   * Checks whether new argument/option/flag are still allowed.
   */
  private fun checkAllowArgumentRegistrations() {
    check(areArgumentRegistrationsAllowed()) {
      "It's no longer allowed to register the source, arguments, options or flags. This must be done before defining " +
          "sub-commands or the executor." }
  }

  override fun <N> Source<N>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, N> {
    this@BaseCommandBuilderImpl.checkAllowArgumentRegistrations()
    @Suppress("UNCHECKED_CAST")
    val property = SourceProperty(this as SourceImpl<S, N>)
    this@BaseCommandBuilderImpl.sourceProperties += property
    return property
  }

  override fun <T> Argument<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T> {
    this@BaseCommandBuilderImpl.checkAllowArgumentRegistrations()
    val property = ArgumentProperty(this, null)
    this@BaseCommandBuilderImpl.arguments += property
    return property
  }

  override fun <T> NamedArgument<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T> {
    this@BaseCommandBuilderImpl.checkAllowArgumentRegistrations()
    this as NamedArgumentImpl<T, in S>
    val property = ArgumentProperty(this.argument, this.name)
    this@BaseCommandBuilderImpl.arguments += property
    return property
  }

  override fun <T> Flag<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T> {
    this@BaseCommandBuilderImpl.checkAllowArgumentRegistrations()
    @Suppress("UNCHECKED_CAST")
    this as FlagImpl<T, in S>
    val property = FlagProperty(this)
    this@BaseCommandBuilderImpl.options += property
    return property
  }

  private fun parseFlagNames(name: String, more: List<String>): FlagNamesImpl {
    val names = listOf(name) + more.toList()
    val shortNames = mutableListOf<Char>()
    val longNames = mutableListOf<String>()
    names.forEach {
      when {
        // Long flag
        it.startsWith("--") -> {
          val value = it.substring(0)
          checkName(value)
          longNames += value
        }
        // Short flag
        it.startsWith("-") -> {
          val value = it.substring(0)
          check(value.length == 1) { "A short flag can only be a single character" }
          checkName(value)
          shortNames += value[0]
        }
        // Invalid
        else -> error("Invalid flag name format, expected --name or -n, but found $it")
      }
    }
    return FlagNamesImpl(longNames, shortNames)
  }

  override fun <T> argument(builder: ArgumentBuilder<T, S>.() -> Unit): BuiltArgument<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun Path.beforeArguments(fn: CommandBuilder<S>.() -> Unit) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun String.beforeArguments(fn: CommandBuilder<S>.() -> Unit) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> Flag<T, S>.default(defaultValue: T): Flag.Defaulted<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> Flag<T, S>.defaultBy(defaultValue: () -> T): Flag.Defaulted<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> Argument<T, S>.flag(name: String, vararg more: String): Flag<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun flag(name: String, vararg more: String): Flag.Defaulted<Boolean, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun Group.invoke(fn: BaseCommandBuilder<S>.() -> Unit) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun Path.invoke(fn: CommandBuilder<S>.() -> Unit): BaseCommandBuilder.CommandBuilderWithPath {
    val builder = CommandBuilderImpl<S>()
    builder.fn()
    val builderWithPath = CommandBuilderWithPath<S>(this, /*builder*/TODO())
    children += builderWithPath
    return builderWithPath
  }

  override fun String.invoke(fn: CommandBuilder<S>.() -> Unit) = this.path.invoke(fn)

  override fun <T, S> Argument<T, S>.named(name: String) = NamedArgumentImpl(name, this)

  override fun Path.or(other: BaseCommandBuilder.CommandBuilderWithPath): BaseCommandBuilder.CommandBuilderWithPath {
    other as CommandBuilderWithPath<*>
    other.path = this.or(other.path)
    return other
  }

  override fun String.or(other: BaseCommandBuilder.CommandBuilderWithPath) = this.path.or(other)

  override fun Path.then(other: BaseCommandBuilder.CommandBuilderWithPath): BaseCommandBuilder.CommandBuilderWithPath {
    other as CommandBuilderWithPath<*>
    other.path = this.then(other.path)
    return other
  }

  override fun String.then(other: BaseCommandBuilder.CommandBuilderWithPath) = this.path.then(other)

  override fun Path.then(other: Command<in S>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun String.then(other: Command<in S>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> BoundOption.Repeatable<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, List<T>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> BoundOption<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T?> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> BuiltArgument<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> Flag.Defaulted<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> Flag.Repeatable<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, List<T>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> MappedOptions<T, in S>.provideDelegate(
      thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, List<T>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> Option.with(argument: Argument<T, S>)
      = BoundOptionImpl((this as OptionImpl).path, argument)

  override fun <T, S> Option.Repeatable.with(argument: Argument<T, S>)
      = RepeatableBoundOptionImpl((this as RepeatableOptionImpl).path, argument)

  override fun <T, S> Option.Repeatable.with(argument: BuiltArgument<T, S>): BoundOption.Repeatable<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> Option.Repeatable.with(argument: NamedArgument<T, S>): BoundOption.Repeatable<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun String.option() = OptionImpl(this.path)
  override fun Path.option() = OptionImpl(this)

  override fun <T> options(builder: MappedOptionsBuilder<T, S>.() -> Unit): MappedOptions<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> Option.with(argument: BuiltArgument<T, S>): BoundOption<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> Option.with(argument: NamedArgument<T, S>): BoundOption<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  /*
  override fun flag(name: String, vararg more: String): Flag<Boolean, S> {
    return FlagImpl.TrueIfPresent(parseFlagNames(name, more.asList()))
  }

  override fun <T, S> Argument<T, S>.flag(name: String, vararg more: String): Flag<T?, S> {
    @Suppress("UNCHECKED_CAST")
    return FlagImpl.Argument(this as Argument<T?, S>,
        null, this@BaseCommandBuilderImpl.parseFlagNames(name, more.asList()))
  }

  override fun <T, S> Flag<T?, S>.default(defaultValue: T): Flag<T, S> {
    this as FlagImpl.Argument<T?, S>
    @Suppress("UNCHECKED_CAST")
    return FlagImpl.Argument(this.argument as Argument<T, S>, { defaultValue }, this.names)
  }

  override fun <T, S> Flag<T?, S>.defaultBy(defaultValue: () -> T): Flag<T, S> {
    this as FlagImpl.Argument<T?, S>
    @Suppress("UNCHECKED_CAST")
    return FlagImpl.Argument(this.argument as Argument<T, S>, defaultValue, this.names)
  }

  override fun <T, S> Argument<T, S>.named(name: String): NamedArgument<T, S> = NamedArgumentImpl(name, this)

  override fun subcommand(name: String, aliases: List<String>, fn: CommandBuilder<S>.() -> Unit) {
    val builder = CommandBuilderImpl<S>()
    builder.fn()
    this.commands += BoundCommand(builder, listOf(name) + aliases)
  }

  override fun subcommand(name: String, aliases: List<String>, command: Command<in S>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun subcommand(name: String, aliases: List<String>): ExecutableCommandBuilder {
    return object : ExecutableCommandBuilder {
      override fun execute(fn: ExecutionContext.() -> Unit) {
        this@BaseCommandBuilderImpl.subcommand(name, aliases) {
          execute(fn)
        }
      }
    }
  }

  override fun groupBefore(fn: BaseCommandBuilder<S>.() -> Unit) {
    val builder = BaseCommandBuilderImpl<S>()
    builder.fn()
    // TODO
  }

  override fun groupAfter(fn: BaseCommandBuilder<S>.() -> Unit) {
    val builder = BaseCommandBuilderImpl<S>()
    builder.fn()
    // TODO
  }*/

  private class CommandBuilderWithPath<S>(
      override var path: Path,
      override val element: FoldedTreeElement<S>
  ) : BaseCommandBuilder.CommandBuilderWithPath, ObjectWithPath<S>

  override fun Option.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, Boolean> {
    TODO("Not yet implemented")
  }

  override fun Option.Repeatable.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, Int> {
    TODO("Not yet implemented")
  }
}

internal abstract class AbstractTeeElement<S>(
    override val children: List<TreeElement<S>>,
    override val flags: List<TreeFlag<S>>,
    val executor: (ExecutionContext.() -> Unit)?
) : TreeElement<S> {

  override val executable: Boolean
    get() = this.executor != null

  fun parse() {

  }
}

internal class TreeArgumentElement<T, S>(
    children: List<TreeElement<S>>,
    flags: List<TreeFlag<S>>,
    override val argument: Argument<T, S>,
    val argumentProperty: ArgumentProperty<T>,
    executor: (ExecutionContext.() -> Unit)?
) : AbstractTeeElement<S>(children, flags, executor), TreeElement.Argument<S>

internal class TreeRootElement<S>(
    children: List<TreeElement<S>>,
    flags: List<TreeFlag<S>>,
    executor: (ExecutionContext.() -> Unit)?
) : AbstractTeeElement<S>(children, flags, executor), TreeElement.Root<S>

internal class CommandTreeImpl<S>(override val rootElement: TreeElement.Root<S>) : CommandTree<S> {

  override fun execute(context: CommandContext<S>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getCompletionSuggestions(context: CommandContext<S>): List<String> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}
