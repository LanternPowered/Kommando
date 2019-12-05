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

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal open class ValueProperty<T> : ReadOnlyProperty<Any?, T> {

  /**
   * Represents that the argument value isn't initialized yet.
   */
  private object UninitializedValue

  private var value: Any? = UninitializedValue

  fun setValue(value: T) {
    check(this.value === UninitializedValue) { "The value is already initialized." }
    this.value = value
  }

  fun clearValue() {
    this.value = UninitializedValue
  }

  override fun getValue(thisRef: Any?, property: KProperty<*>): T {
    check(this.value !== UninitializedValue) { "The value isn't parsed yet." }
    @Suppress("UNCHECKED_CAST")
    return this.value as T
  }
}
