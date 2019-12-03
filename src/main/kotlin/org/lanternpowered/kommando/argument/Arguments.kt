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

import org.lanternpowered.kommando.NullContext

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
