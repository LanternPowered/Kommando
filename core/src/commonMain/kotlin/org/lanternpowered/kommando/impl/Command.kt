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
import org.lanternpowered.kommando.BuiltArgument
import org.lanternpowered.kommando.Command
import org.lanternpowered.kommando.CommandBuilder
import org.lanternpowered.kommando.CommandContext
import org.lanternpowered.kommando.ExecutableCommandBuilder
import org.lanternpowered.kommando.Flag
import org.lanternpowered.kommando.NamedArgument
import org.lanternpowered.kommando.ExecutionContext
import org.lanternpowered.kommando.Option
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

internal class CommandBuilderImpl<S> : BaseCommandBuilderImpl<S>(), CommandBuilder<S> {

  override fun areArgumentRegistrationsAllowed(): Boolean {
    return super.areArgumentRegistrationsAllowed()
  }

  override fun execute(fn: ExecutionContext.() -> Unit) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}

private class NamedArgumentImpl<T, S>(
    val name: String,
    val argument: Argument<T, S>
) : NamedArgument<T, S>

internal open class BaseCommandBuilderImpl<S> : BaseCommandBuilder<S> {

  internal class BoundCommand<S>(
      val builder: CommandBuilderImpl<S>,
      val aliases: List<String>
  )

  private val sourceProperties = mutableListOf<SourceProperty<S, *>>()
  private val arguments = mutableListOf<ArgumentProperty<*>>()
  private val commands = mutableListOf<BoundCommand<S>>()
  private val options = mutableListOf<FlagProperty<*>>()

  fun build(): Command<S> {
    TODO()
  }

  override fun source(): Source<S> = SourceImpl()

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
  }

  override fun <T> Option<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T?> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> Option.Defaulted<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> Option.Repeatable<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, List<T>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> Option<T, S>.default(defaultValue: T): Option.Defaulted<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> Option<T, S>.defaultBy(defaultValue: () -> T): Option.Defaulted<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> option(name: String, vararg more: String, builder: ArgumentBuilder<T, S>.() -> ArgumentBuilder.ConvertFunction): Option<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> Argument<T, S>.option(name: String, vararg more: String): Option<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> NamedArgument<T, S>.option(name: String, vararg more: String): Option<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> BuiltArgument<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> argument(builder: ArgumentBuilder<T, S>.() -> ArgumentBuilder.ConvertFunction): BuiltArgument<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
}
