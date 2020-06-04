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
 * Constructs a new int argument.
 */
fun int() = IntArgument()

/**
 * Constructs a new int argument with the given range.
 */
fun int(range: ClosedRange<Int>) = IntArgument(range.start, range.endInclusive)

/**
 * An argument that parses int values.
 */
data class IntArgument internal constructor(
    val min: Int = Int.MIN_VALUE,
    val max: Int = Int.MAX_VALUE
) : SimpleArgument<Int, Any>("int") {

  override fun parse(context: ArgumentParseContext<Any>) = context.run {
    val value = readInt()
    check(value >= min) {
      "Int must not be less than $min, but found $value" }
    check(value <= max) {
      "Int must not be greater than $max, but found $value" }
    result(value)
  }
}
