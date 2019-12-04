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

import org.lanternpowered.kommando.LiteralMessage
import org.lanternpowered.kommando.Message

private val nullMessage = LiteralMessage("null")

/**
 * Converts the object into a [Message].
 */
internal fun messageOf(any: Any?): Message {
  if (any == null)
    return nullMessage
  if (any is Message)
    return any
  if (any is Function0<*>)
    return messageOf(any())
  return LiteralMessage(any.toString())
}
