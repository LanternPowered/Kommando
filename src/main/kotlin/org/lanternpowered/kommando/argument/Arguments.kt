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

fun <A, B, C, S> triple(
    a: Argument<A, in S>,
    b: Argument<B, in S>,
    c: Argument<C, in S>
): Argument<Triple<A, B, C>, S> = argumentOf {
  parse {
    val resultA = a.parse()
    if (resultA is ParseResult.Error)
      return@parse resultA.mapType()

    val resultB = b.parse()
    if (resultB is ParseResult.Error)
      return@parse resultB.mapType()

    val resultC = c.parse()
    (resultA + resultB + resultC).map { pair -> Triple(pair.first.first, pair.first.second, pair.second) }
  }
}

/**
 * Constructs a enum [Argument] based on the given enum type [E].
 */
inline fun <reified E : Enum<E>> enum(): Argument<E, Any> = enum(enumValues<E>().toList())

/**
 * Constructs a enum [Argument] based on the given [values].
 */
fun <E : Enum<E>> enum(values: List<E>): Argument<E, Any> = argumentOf {
  val mappedValues = values.associateBy { value -> value.name.toLowerCase() }
  val joinedKeys = mappedValues.keys.joinToString(", ")
  parse {
    val name = parseString()
    val value = mappedValues[name]
    if (value != null) success(value) else error("Enum value must be one of [$joinedKeys], but found $name")
  }
}

/**
 * Constructs a choices [Argument] based on the given values.
 */
fun choice(first: String, second: String, vararg more: String): Argument<String, Any> {
  return choice((arrayOf(first, second) + more).toList())
}

/**
 * Constructs a choices [Argument] based on the given values.
 */
fun choice(values: List<String>): Argument<String, Any> {
  return choice(values.associateWith { it })
}

/**
 * Constructs a choices [Argument] based on the given values.
 */
fun <V> choice(first: Pair<String, V>, second: Pair<String, V>, vararg more: Pair<String, V>): Argument<V, Any> {
  return choice((arrayOf(first, second) + more).toMap())
}

/**
 * Constructs a choices [Argument] based on the given [values].
 */
fun <V> choice(values: Map<String, V>): Argument<V, Any> = argumentOf {
  check(values.isNotEmpty()) { "The values may not be empty" }
  val immutableValues = values.toMap()
  val joinedKeys = immutableValues.keys.joinToString(", ")
  parse {
    val key = parseString()
    val value = immutableValues[key]
    if (value != null) success(value) else error("Choice must be one of [$joinedKeys], but found $key")
  }
}

/**
 * Constructs a boolean [Argument].
 */
fun boolean(): Argument<Boolean, Any> = argumentOf {
  parse {
    success(parseBoolean())
  }
  val suggestions = listOf("true", "false")
  suggest {
    suggestions
  }
}

/**
 * Constructs a string [Argument].
 */
fun string(): Argument<String, Any> = argumentOf {
  parse {
    success(parseString())
  }
}

fun int(): Argument<Int, Any> = argumentOf {
  parse {
    success(parseInt())
  }
}

fun int(range: IntRange): Argument<Int, Any> = argumentOf {
  parse {
    val value = parseInt()
    check(value >= range.first) {
      "Int must not be less than ${range.first}, but found $value" }
    check(value <= range.last) {
      "Int must not be greater than ${range.last}, but found $value" }
    success(value)
  }
}

fun long(): Argument<Long, Any> = argumentOf {
  parse {
    success(parseLong())
  }
}

fun long(range: LongRange): Argument<Long, Any> = argumentOf {
  parse {
    val value = parseLong()
    check(value >= range.first) {
      "Long must not be less than ${range.first}, but found $value" }
    check(value <= range.last) {
      "Long must not be greater than ${range.last}, but found $value" }
    success(value)
  }
}

fun float(): Argument<Float, Any> = argumentOf {
  parse {
    success(parseFloat())
  }
}

fun float(range: ClosedFloatingPointRange<Float>): Argument<Float, Any> = argumentOf {
  parse {
    val value = parseFloat()
    check(value >= range.start) {
      "Float must not be less than ${range.start}, but found $value" }
    check(value <= range.endInclusive) {
      "Float must not be greater than ${range.endInclusive}, but found $value" }
    success(value)
  }
}

fun double(): Argument<Double, Any> = argumentOf {
  parse {
    success(parseDouble())
  }
}

fun double(range: ClosedFloatingPointRange<Double>): Argument<Double, Any> = argumentOf {
  parse {
    val value = parseDouble()
    check(value >= range.start) {
      "Double must not be less than ${range.start}, but found $value" }
    check(value <= range.endInclusive) {
      "Double must not be greater than ${range.endInclusive}, but found $value" }
    success(value)
  }
}

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
 * Constructs a double [ClosedFloatingPointRange] argument.
 */
fun floatRange(): Argument<ClosedFloatingPointRange<Float>, Any>
    = range("float", -Float.MAX_VALUE, Float.MAX_VALUE, String::toFloatOrNull) { start, end -> start..end }

private inline fun <R : ClosedRange<T>, T> range(
    name: String, min: T, max: T, crossinline parse: String.() -> T?, crossinline builder: (start: T, end: T) -> R
): Argument<R, Any> = argumentOf {
  parse {
    val value = parseUnquotedString()
    val fail = { fail("Expected $name range, but found $value") }
    val index = value.indexOf("..")
    val start: T
    val end: T
    if (index == -1) {
      start = value.parse() ?: fail()
      end = start
    } else {
      val startValue = value.substring(0, index)
      start = if (startValue.isEmpty()) min else startValue.parse() ?: fail()
      val endValue = value.substring(index + 2)
      end = if (endValue.isEmpty()) max else endValue.parse() ?: fail()
    }
    success(builder(start, end))
  }
}

/**
 * Constructs a argument that parses the argument optionally.
 */
fun <T, S> Argument<T, S>.optional(): Argument<T?, S> = argumentOf {
  val argument = this@optional
  parse {
    when (val result = argument.parse()) {
      is ParseResult.Success -> result.asNullable()
      is ParseResult.Error -> success(null, result.error)
    }
  }
  suggest {
    argument.suggest()
  }
}

fun <T, S> Argument<T?, S>.defaultBy(defaultValue: ArgumentParseContext<S>.() -> T): Argument<T, S> = argumentOf {
  val argument = this@defaultBy
  parse {
    argument.parse().map { value -> value ?: defaultValue() }
  }
  suggest {
    argument.suggest()
  }
}

fun <T, S> Argument<T?, S>.default(defaultValue: T): Argument<T, S> = defaultBy { defaultValue }

fun <T, S> Argument<T, S>.pair(): Argument<Pair<T, T>, S> = argumentOf {
  val argument = this@pair
  parse {
    val first = argument.parse()
    if (first is ParseResult.Error)
      return@parse first.mapType()

    val second = argument.parse()
    first + second
  }
}

fun <T, S> Argument<T, S>.triple(): Argument<Triple<T, T, T>, S> = argumentOf {
  val argument = this@triple
  parse {
    val first = argument.parse()
    if (first is ParseResult.Error)
      return@parse first.mapType()

    val second = argument.parse()
    if (second is ParseResult.Error)
      return@parse second.mapType()

    val third = argument.parse()
    (first + second + third).map { pair -> Triple(pair.first.first, pair.first.second, pair.second) }
  }
}

fun <T, S> Argument<T, S>.multiple(times: IntRange = 1..Int.MAX_VALUE): Argument<List<T>, S> = argumentOf {
  val argument = this@multiple
  parse {
    val list = mutableListOf<T>()
    var potentialError: Message? = null
    loop@ for (i in 0 until times.last) {
      when (val result = argument.parse(this)) {
        is ParseResult.Success -> {
          list += result.value
          potentialError = result.potentialError
        }
        is ParseResult.Error -> {
          // Not enough arguments were parsed in order
          // to be successful
          if (i < times.first)
            return@parse result.mapType()

          // The minimum amount of values got reached,
          // so try to parse the next argument.
          potentialError = result.error
          break@loop
        }
      }
    }
    success(list, potentialError)
  }
  val rangeName = times.formatName()
  name {
    val name = argument.transformName(baseName)
    "$name{$rangeName}"
  }
}

/**
 * Converts the output value.
 */
fun <T, N, S> Argument<T, S>.convert(fn: NullContext.(T) -> N): Argument<N, S> = argumentOf {
  val argument = this@convert
  parse {
    argument.parse().map { NullContext.fn(it) }
  }
  suggest {
    argument.suggest()
  }
  name {
    argument.transformName(baseName)
  }
}

fun <T, S> Argument<T, S>.validate(fn: ArgumentValidationContext.(T) -> Unit): Argument<T, S> = argumentOf {
  val argument = this@validate
  parse {
    argument.parse().also {
      if (it is ParseResult.Success) fn(it.value)
    }
  }
  suggest {
    argument.suggest()
  }
  name {
    argument.transformName(baseName)
  }
}

/**
 * Formats the range string.
 */
private fun IntRange.formatName(): String {
  return when {
    this.first == Int.MIN_VALUE && this.last == Int.MAX_VALUE -> ".."
    this.last == Int.MAX_VALUE -> "$first.."
    this.first == Int.MAX_VALUE -> "..$last"
    this.first == this.last -> "$first"
    else -> "$first..$last"
  }
}
