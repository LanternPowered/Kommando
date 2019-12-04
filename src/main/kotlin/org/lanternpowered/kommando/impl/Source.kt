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

@Suppress("FunctionName")
internal fun <T> SourceImpl(): SourceImpl<T, T> {
  return object : SourceImpl<T, T> {
    override fun apply(source: T) = source
  }
}

internal interface SourceImpl<T, V> : Source<V> {

  fun apply(source: T): V

  override fun validate(fn: ValidationContext.(source: V) -> Unit): Source<V> {
    return convert {
      SourceValidationContext.fn(it)
      it
    }
  }

  override fun <R> convert(fn: ValidationContext.(source: V) -> R): Source<R> {
    val first = this
    return object : SourceImpl<T, R> {
      override fun apply(source: T): R {
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
