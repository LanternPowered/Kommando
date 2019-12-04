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

import org.lanternpowered.kommando.CommandException
import org.lanternpowered.kommando.Source
import org.lanternpowered.kommando.ValidationContext
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class SourceProperty<O, S>(private val sourceImpl: SourceImpl<O, S>) : ReadOnlyProperty<Any?, S> {

  /**
   * Represents that the source isn't initialized yet.
   */
  private object UninitializedSource

  private var source: Any? = UninitializedSource

  /**
   * Sets the source. Applies wny kind of validations or conversions.
   */
  fun setSource(source: O) {
    check(this.source === UninitializedSource) { "The source is already initialized." }
    this.source = this.sourceImpl.apply(source)
  }

  /**
   * Clears the source.
   */
  fun clearSource() {
    this.source = UninitializedSource
  }

  override fun getValue(thisRef: Any?, property: KProperty<*>): S {
    check(this.source !== UninitializedSource) { "The source isn't initialized yet." }
    @Suppress("UNCHECKED_CAST")
    return this.source as S
  }
}

@Suppress("FunctionName")
internal fun <S> SourceImpl(): SourceImpl<S, S> {
  return object : SourceImpl<S, S> {
    override fun apply(source: S) = source
  }
}

internal interface SourceImpl<O, S> : Source<S> {

  fun apply(source: O): S

  override fun validate(fn: ValidationContext.(source: S) -> Unit): Source<S> {
    return convert {
      SourceValidationContext.fn(it)
      it
    }
  }

  override fun <R> convert(fn: ValidationContext.(source: S) -> R): Source<R> {
    val first = this
    return object : SourceImpl<O, R> {
      override fun apply(source: O): R {
        return SourceValidationContext.fn(first.apply(source))
      }
    }
  }
}

internal object SourceValidationContext : ValidationContext {

  override fun error(message: Any): Nothing {
    throw CommandException(messageOf(message))
  }

  override fun check(state: Boolean, message: () -> Any) {
    if (!state) {
      error(message())
    }
  }

  override fun check(state: Boolean) {
    if (!state) {
      error("Invalid state")
    }
  }
}
