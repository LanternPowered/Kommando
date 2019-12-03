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

import org.lanternpowered.kommando.Command
import org.lanternpowered.kommando.CommandBuilder
import org.lanternpowered.kommando.Flag
import org.lanternpowered.kommando.NamedArgument
import org.lanternpowered.kommando.NamedFlag
import org.lanternpowered.kommando.NamedOption
import org.lanternpowered.kommando.NullContext
import org.lanternpowered.kommando.Option
import org.lanternpowered.kommando.Source
import org.lanternpowered.kommando.argument.Argument
import org.lanternpowered.kommando.argument.ArgumentBuilder
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class CommandBuilderImpl<S> : CommandBuilder<S> {

  fun build(): Command<S> {
    TODO()
  }

  override fun source(): Source<S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <S> Source<S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun Flag.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun NamedFlag.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, Boolean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> Argument<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> NamedArgument<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> Option<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> NamedOption<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun flag(): Flag {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun Flag.name(name: String, vararg aliases: String): NamedFlag {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> Argument<T, S>.option(): Option<T?, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> Option<T?, S>.default(defaultValue: T): Option<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> Option<T, S>.name(name: String, vararg aliases: String): NamedOption<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> Argument<T, S>.name(name: String): NamedArgument<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> argument(fn: ArgumentBuilder<T, S>.() -> Unit): Argument<T, S> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun execute(fn: NullContext.() -> Unit) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun subcommand(name: String, aliases: List<String>, fn: CommandBuilder<S>.() -> Unit) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun subcommand(name: String, aliases: List<String>, command: Command<in S>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}
