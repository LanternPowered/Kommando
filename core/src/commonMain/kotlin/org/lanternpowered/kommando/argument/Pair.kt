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
 * Constructs a pair [Argument] that parses the target argument two times.
 */
fun <T, S> Argument<T, S>.pair()
    = PairArgument(this, this)

/**
 * Constructs a pair [Argument] based on the two given arguments.
 */
fun <A, B, S> pair(first: Argument<A, in S>, second: Argument<B, in S>)
    = PairArgument(first, second)

/**
 * An argument that parses pairs based on the [first] and [second] arguments.
 */
data class PairArgument<A, B, S> internal constructor(
    val first: Argument<A, in S>,
    val second: Argument<B, in S>
) : Argument<Pair<A, B>, S> {

  override val usage: ArgumentUsage = ArgumentUsage("${first.usage} ${second.usage}",
      optional = first.usage.optional && second.usage.optional)

  override fun parse(context: ArgumentParseContext<S>) = context.run {
    val a = first.parse()
    val b = second.parse()
    a + b
  }

  override fun suggest(context: ArgumentParseContext<S>) = context.run {
    if (!first.tryParseOrReset()) first.suggest() else second.suggest()
  }
}
