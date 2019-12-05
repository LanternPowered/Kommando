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
fun intRange(): Argument<IntRange, Any>
    = range("int", Int.MIN_VALUE, Int.MAX_VALUE, String::toIntOrNull) { start, end -> start..end }

/**
 * Constructs a [LongRange] argument.
 */
fun longRange(): Argument<LongRange, Any>
    = range("long", Long.MIN_VALUE, Long.MAX_VALUE, String::toLongOrNull) { start, end -> start..end }

/**
 * Constructs a double [ClosedFloatingPointRange] argument.
 */
fun doubleRange(): Argument<ClosedFloatingPointRange<Double>, Any>
    = range("double", -Double.MAX_VALUE, Double.MAX_VALUE, String::toDoubleOrNull) { start, end -> start..end }

/**
 * Constructs a float [ClosedFloatingPointRange] argument.
 */
// TODO: Change back to ClosedFloatingPointRange, it's a bug in kotlin: https://youtrack.jetbrains.com/issue/KT-35299
fun floatRange(): Argument<ClosedRange<Float>, Any>
    = range("float", -Float.MAX_VALUE, Float.MAX_VALUE, String::toFloatOrNull) { start, end -> start..end }

private inline fun <R : ClosedRange<T>, T> range(
    name: String, min: T, max: T, crossinline parse: String.() -> T?, crossinline builder: (start: T, end: T) -> R
): Argument<R, Any> = argument {
  parse {
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
