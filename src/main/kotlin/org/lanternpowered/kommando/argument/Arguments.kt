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
import org.lanternpowered.kommando.NullContext
import kotlin.reflect.KProperty

/**
 * Constructs an [Argument] based on the three given arguments.
 */
fun <A, B, S> pair(
    first: Argument<A, in S>,
    second: Argument<B, in S>
): Argument<Pair<A, B>, S> = argument {
  parse {
    val a = first.parse()
    val b = second.parse()
    a + b
  }
  suggest {
    if (!first.tryParse()) first.suggest() else second.suggest()
  }
}

/**
 * Constructs an [Argument] based on the three given arguments.
 */
fun <A, B, C, S> triple(
    first: Argument<A, in S>,
    second: Argument<B, in S>,
    third: Argument<C, in S>
): Argument<Triple<A, B, C>, S> = argument {
  parse {
    val a = first.parse()
    val b = second.parse()
    val c = third.parse()
    (a + b + c).map { pair -> Triple(pair.first.first, pair.first.second, pair.second) }
  }
  suggest {
    when {
      !first.tryParse() -> first.suggest()
      !second.tryParse() -> second.suggest()
      else -> third.suggest()
    }
  }
}

/**
 * Constructs an enum [Argument] based on the given enum type [E].
 */
inline fun <reified E : Enum<E>> enum(): Argument<E, Any> = enum(enumValues<E>().toList())

/**
 * Constructs an enum [Argument] based on the given [values].
 */
fun <E : Enum<E>> enum(values: List<E>): Argument<E, Any> = argument {
  val mappedValues = values.associateBy { value -> value.name.toLowerCase() }
  val joinedKeys = mappedValues.keys.joinToString(", ")
  parse {
    val name = parseString()
    val value = mappedValues[name]
    if (value != null) result(value) else error("Enum value must be one of [$joinedKeys], but found $name")
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
fun <V> choice(values: Map<String, V>): Argument<V, Any> = argument {
  check(values.isNotEmpty()) { "The values may not be empty" }
  val immutableValues = values.toMap()
  val joinedKeys = immutableValues.keys.joinToString(", ")
  parse {
    val key = parseString()
    val value = immutableValues[key]
    if (value != null) result(value) else error("Choice must be one of [$joinedKeys], but found $key")
  }
  // TODO suggestions
}

/**
 * A base classes for local choices objects in the builder.
 */
@Suppress("ClassName")
abstract class choices<T> : Argument<T, Any> {

  private val choices = mutableMapOf<String, T>()
  private val joinedKeys by lazy { this.choices.keys.joinToString { ", " } }

  /**
   * Gets a delegate to access the source.
   */
  @JvmName("register")
  protected operator fun T.provideDelegate(thisRef: Any?, prop: KProperty<*>): T {
    choices[prop.name.toLowerCase()] = this
    return this
  }

  /**
   * Gets a delegate to access the source.
   */
  @JvmName("register")
  protected operator fun Pair<String, T>.provideDelegate(thisRef: Any?, prop: KProperty<*>): T {
    choices[this.first.toLowerCase()] = this.second
    return this.second
  }

  @Suppress("NOTHING_TO_INLINE")
  protected inline operator fun T.getValue(thisRef: Any?, property: KProperty<*>) = this

  override fun parse(context: ArgumentParseContext<Any>): ParseResult<T> {
    val key = context.parseString()
    val value = choices[key]
    return if (value != null) ParseResult(value) else error("Choice must be one of [$joinedKeys], but found $key")
  }

  override fun suggest(context: ArgumentParseContext<Any>): List<String> = listOf() // TODO
}

/**
 * Constructs a boolean [Argument].
 */
fun boolean(): Argument<Boolean, Any> = argument {
  parse {
    result(parseBoolean())
  }
  val suggestions = listOf("true", "false")
  suggest {
    suggestions
  }
}

/**
 * Constructs a string [Argument].
 */
fun string(): Argument<String, Any> = argument {
  parse {
    result(parseString())
  }
}

fun int(): Argument<Int, Any> = argument {
  parse {
    result(parseInt())
  }
}

fun int(range: IntRange): Argument<Int, Any> = argument {
  parse {
    val value = parseInt()
    check(value >= range.first) {
      "Int must not be less than ${range.first}, but found $value" }
    check(value <= range.last) {
      "Int must not be greater than ${range.last}, but found $value" }
    result(value)
  }
}

fun long(): Argument<Long, Any> = argument {
  parse {
    result(parseLong())
  }
}

fun long(range: LongRange): Argument<Long, Any> = argument {
  parse {
    val value = parseLong()
    check(value >= range.first) {
      "Long must not be less than ${range.first}, but found $value" }
    check(value <= range.last) {
      "Long must not be greater than ${range.last}, but found $value" }
    result(value)
  }
}

fun float(): Argument<Float, Any> = argument {
  parse {
    result(parseFloat())
  }
}

fun float(range: ClosedFloatingPointRange<Float>): Argument<Float, Any> = argument {
  parse {
    val value = parseFloat()
    check(value >= range.start) {
      "Float must not be less than ${range.start}, but found $value" }
    check(value <= range.endInclusive) {
      "Float must not be greater than ${range.endInclusive}, but found $value" }
    result(value)
  }
}

fun double(): Argument<Double, Any> = argument {
  parse {
    result(parseDouble())
  }
}

fun double(range: ClosedFloatingPointRange<Double>): Argument<Double, Any> = argument {
  parse {
    val value = parseDouble()
    check(value >= range.start) {
      "Double must not be less than ${range.start}, but found $value" }
    check(value <= range.endInclusive) {
      "Double must not be greater than ${range.endInclusive}, but found $value" }
    result(value)
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

/**
 * Constructs a argument that parses the argument optionally.
 */
fun <T, S> Argument<T, S>.optional(): Argument<T?, S> = argument {
  val argument = this@optional
  parse {
    try {
      argument.parse().asNullable()
    } catch (ex: ArgumentParseException) {
      result(null, ex.error)
    }
  }
  suggest {
    argument.suggest()
  }
}

fun <T, S> Argument<T?, S>.defaultBy(defaultValue: ArgumentParseContext<S>.() -> T): Argument<T, S> = argument {
  val argument = this@defaultBy
  parse {
    argument.parse().map { value -> value ?: defaultValue() }
  }
  suggest {
    argument.suggest()
  }
}

fun <T, S> Argument<T?, S>.default(defaultValue: T): Argument<T, S> = defaultBy { defaultValue }

fun <T, S> Argument<T, S>.pair(): Argument<Pair<T, T>, S> = pair(this, this)

fun <T, S> Argument<T, S>.triple(): Argument<Triple<T, T, T>, S> = triple(this, this, this)

fun <T, S> Argument<T, S>.multiple(times: IntRange = 1..Int.MAX_VALUE): Argument<List<T>, S> = argument {
  val argument = this@multiple
  parse {
    val list = mutableListOf<T>()
    var potentialError: Message? = null
    for (i in 0 until times.last) {
      try {
        val result = argument.parse(this)
        list += result.value
        potentialError = result.potentialError
      } catch (ex: ArgumentParseException) {
        // Not enough arguments were parsed in order
        // to be successful
        if (i < times.first)
          throw ex

        // The minimum amount of values got reached,
        // so try to parse the next argument.
        potentialError = ex.error
        break
      }
    }
    result(list, potentialError)
  }
  suggest {
    for (i in 0 until times.last) {
      if (!argument.tryParse())
        break
    }
    argument.suggest()
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
fun <T, N, S> Argument<T, S>.convert(fn: NullContext.(T) -> N): Argument<N, S> = argument {
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

fun <T, S> Argument<T, S>.validate(fn: ArgumentValidationContext.(T) -> Unit): Argument<T, S> = argument {
  val argument = this@validate
  parse {
    argument.parse().also { fn(it.value) }
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
