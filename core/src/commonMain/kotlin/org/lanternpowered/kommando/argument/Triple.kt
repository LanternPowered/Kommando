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

import org.lanternpowered.kommando.CommandUsage

/**
 * Constructs a triple [Argument] that parses the target argument three times.
 */
fun <T, S> Argument<T, S>.triple(): TripleArgument<T, T, T, S> =
    TripleArgument(this, this, this)

/**
 * Constructs a triple [Argument] based on the three given arguments.
 */
fun <A, B, C, S> triple(
    first: Argument<A, in S>, second: Argument<B, in S>, third: Argument<C, in S>
): TripleArgument<A, B, C, S> = TripleArgument(first, second, third)

/**
 * An argument that parses triples based on the [first], [second] and [third] arguments.
 */
data class TripleArgument<A, B, C, S> internal constructor(
    val first: Argument<A, in S>,
    val second: Argument<B, in S>,
    val third: Argument<C, in S>
) : Argument<Triple<A, B, C>, S> {

  override val usage: CommandUsage = CommandUsage.join(first.usage, second.usage, third.usage, separator = " ")

  override fun parse(context: ArgumentParseContext<S>) = context.run {
    val a = first.parse()
    val b = second.parse()
    val c = third.parse()
    (a + b + c).map { pair -> Triple(pair.first.first, pair.first.second, pair.second) }
  }

  override fun suggest(context: ArgumentParseContext<S>) = context.run {
    when {
      !first.tryParseOrReset() -> first.suggest()
      !second.tryParseOrReset() -> second.suggest()
      else -> third.suggest()
    }
  }
}
