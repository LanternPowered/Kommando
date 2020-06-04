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

/**
 * Constructs a new argument that parses the argument multiple times.
 */
fun <T, S> Argument<T, S>.multiple(times: Int): ListArgument<T, S> {
  check(times > 0) { "The amount of times must be greater than 0, but found $times" }
  return multiple(times..times)
}

/**
 * Constructs a new argument that parses the argument multiple times.
 */
fun <T, S> Argument<T, S>.multiple(times: IntRange = 1..Int.MAX_VALUE): ListArgument<T, S> {
  check(times.first >= 0) { "The minimum amount of times must not be negative, but found ${times.first}" }
  check(times.last > 0) { "The maximum amount of times must be greater than 0, but found ${times.last}" }
  return ListArgument(this, times)
}

/**
 * An argument that parses an argument multiple times.
 */
class ListArgument<T, S> internal constructor(
    val argument: Argument<T, S>,
    val times: IntRange = 1..Int.MAX_VALUE
): Argument<List<T>, S> {

  override val usage = ArgumentUsage(argument.usage.name + times.formatName()) // Unset optional

  override fun parse(context: ArgumentParseContext<S>): ParseResult<List<T>> = context.run {
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

  override fun suggest(context: ArgumentParseContext<S>) = context.run {
    for (i in 0 until times.last) {
      if (!argument.tryParseOrReset())
        break
    }
    argument.suggest()
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
