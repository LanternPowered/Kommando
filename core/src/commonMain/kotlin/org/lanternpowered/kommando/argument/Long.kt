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
 * Constructs a new long argument.
 */
fun long() = LongArgument()

/**
 * Constructs a new long argument with the given range.
 */
fun long(range: ClosedRange<Long>) = LongArgument(range.start, range.endInclusive)

/**
 * An argument that parses long values.
 */
data class LongArgument internal constructor(
    val min: Long = Long.MIN_VALUE,
    val max: Long = Long.MAX_VALUE
) : Argument<Long, Any> {

  override fun parse(context: ArgumentParseContext<Any>) = context.run {
    val value = parseLong()
    check(value >= min) {
      "Long must not be less than $min, but found $value" }
    check(value <= max) {
      "Long must not be greater than $max, but found $value" }
    result(value)
  }
}
