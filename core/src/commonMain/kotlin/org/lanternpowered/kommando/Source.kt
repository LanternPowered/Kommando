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

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

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
interface SourceAwareBuilder<S> : BaseBuilder {

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
}
