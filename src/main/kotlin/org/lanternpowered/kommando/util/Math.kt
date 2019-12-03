/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando.util

fun clamp(value: Float, range: ClosedFloatingPointRange<Float>): Float {
  return when {
    value < range.start -> range.start
    value > range.endInclusive -> range.endInclusive
    else -> value
  }
}
