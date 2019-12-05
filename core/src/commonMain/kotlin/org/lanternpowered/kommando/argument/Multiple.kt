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
 * Constructs an pair [Argument] that parses the target argument two times.
 */
fun <T, S> Argument<T, S>.pair(): Argument<Pair<T, T>, S> = pair(this, this)

/**
 * Constructs an pair [Argument] based on the two given arguments.
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
 * Constructs an pair [Argument] that parses the target argument three times.
 */
fun <T, S> Argument<T, S>.triple(): Argument<Triple<T, T, T>, S> = triple(this, this, this)

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
