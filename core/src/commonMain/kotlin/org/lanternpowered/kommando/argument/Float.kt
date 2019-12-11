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
 * Constructs a new float argument.
 */
fun float() = FloatArgument()

/**
 * Constructs a new float argument with the given range.
 */
fun float(range: ClosedRange<Float>) = FloatArgument(range.start, range.endInclusive)

/**
 * An argument that parses float values.
 */
data class FloatArgument internal constructor(
    val min: Float = -Float.MAX_VALUE,
    val max: Float = Float.MAX_VALUE
) : Argument<Float, Any> {

  override fun parse(context: ArgumentParseContext<Any>) = context.run {
    val value = parseFloat()
    check(value >= min) {
      "Float must not be less than $min, but found $value" }
    check(value <= max) {
      "Float must not be greater than $max, but found $value" }
    result(value)
  }
}
