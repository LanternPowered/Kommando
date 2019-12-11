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
 * Constructs an [IntRange] argument.
 */
fun intRange() = IntRangeArgument()

/**
 * An argument that parses int ranges.
 */
class IntRangeArgument internal constructor() : RangeArgument<IntRange, Int>(
    "int", Int.MIN_VALUE, Int.MAX_VALUE, String::toIntOrNull, { start, end -> start..end })

/**
 * Constructs a [LongRange] argument.
 */
fun longRange() = LongRangeArgument()

/**
 * An argument that parses long ranges.
 */
class LongRangeArgument internal constructor() : RangeArgument<LongRange, Long>(
    "long", Long.MIN_VALUE, Long.MAX_VALUE, String::toLongOrNull, { start, end -> start..end })

/**
 * Constructs a double [ClosedFloatingPointRange] argument.
 */
fun doubleRange() = DoubleRangeArgument()

/**
 * An argument that parses double ranges.
 */
class DoubleRangeArgument internal constructor() : RangeArgument<ClosedFloatingPointRange<Double>, Double>(
    "double", -Double.MAX_VALUE, Double.MAX_VALUE, String::toDoubleOrNull, { start, end -> start..end })

/**
 * Constructs a float [ClosedFloatingPointRange] argument.
 */
fun floatRange() = FloatRangeArgument()

/**
 * An argument that parses float ranges.
 */
// TODO: Change back to ClosedFloatingPointRange, it's a bug in kotlin: https://youtrack.jetbrains.com/issue/KT-35299
class FloatRangeArgument internal constructor() : RangeArgument<ClosedRange<Float>, Float>(
    "float", -Float.MAX_VALUE, Float.MAX_VALUE, String::toFloatOrNull, { start, end -> start..end })

/**
 * The base class for range arguments.
 */
abstract class RangeArgument<R : ClosedRange<T>, T : Comparable<T>> internal constructor(
    private val name: String,
    private val min: T,
    private val max: T,
    private inline val parse: String.() -> T?,
    private inline val builder: (start: T, end: T) -> R
) : Argument<R, Any> {

  override fun parse(context: ArgumentParseContext<Any>) = context.run {
    val value = parseUnquotedString()
    val error = { error("Expected $name range, but found $value") }
    val index = value.indexOf("..")
    val start: T
    val end: T
    if (index == -1) {
      start = value.parse() ?: error()
      end = start
    } else {
      val startValue = value.substring(0, index)
      start = if (startValue.isEmpty()) min else startValue.parse() ?: error()
      val endValue = value.substring(index + 2)
      end = if (endValue.isEmpty()) max else endValue.parse() ?: error()
    }
    result(builder(start, end))
  }
}
