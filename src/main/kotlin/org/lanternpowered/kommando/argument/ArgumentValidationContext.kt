/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando.argument

import org.lanternpowered.kommando.Message
import kotlin.contracts.contract

fun ArgumentValidationContext.check(value: Boolean, message: () -> Any) {
  contract {
    returns() implies value
  }
}

interface ArgumentValidationContext {

  fun fail(message: Message): Nothing

  fun fail(message: String): Nothing

  //fun check(state: Boolean, message: () -> Any)

  fun check(state: Boolean)

  companion object {

  }
}
