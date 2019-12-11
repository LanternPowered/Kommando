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

/**
 * Constructs a new double argument.
 */
fun double() = DoubleArgument()

/**
 * Constructs a new double argument with the given range.
 */
fun double(range: ClosedRange<Double>) = DoubleArgument(range.start, range.endInclusive)

/**
 * An argument that parses float values.
 */
data class DoubleArgument internal constructor(
    val min: Double = -Double.MAX_VALUE,
    val max: Double = Double.MAX_VALUE
) : Argument<Double, Any> {

  override fun parse(context: ArgumentParseContext<Any>) = context.run {
    val value = parseDouble()
    check(value >= min) {
      "Double must not be less than $min, but found $value" }
    check(value <= max) {
      "Double must not be greater than $max, but found $value" }
    result(value)
  }
}
