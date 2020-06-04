/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando.arrow.argument

import arrow.core.Either
import org.lanternpowered.kommando.CommandUsage
import org.lanternpowered.kommando.argument.Argument
import org.lanternpowered.kommando.argument.ArgumentParseContext
import org.lanternpowered.kommando.argument.ArgumentParseException
import org.lanternpowered.kommando.argument.ParseResult

/**
 * Constructs an argument that either parses the [left]
 * argument type, or the other one [right].
 *
 * @param left The first argument type
 * @param right The second argument type
 */
fun <L, R, S> either(left: Argument<L, in S>, right: Argument<R, in S>): EitherArgument<L, R, S> =
    EitherArgument(left, right)

/**
 * An argument that either parses the [left]
 * argument type, or the other one [right].
 *
 * @property left The first argument type
 * @property right The second argument type
 */
class EitherArgument<L, R, S> internal constructor(
    val left: Argument<L, in S>,
    val right: Argument<R, in S>
) : Argument<Either<L, R>, S> {

  override val usage: CommandUsage = CommandUsage.join(left.usage, right.usage, separator = "|")

  override fun parse(context: ArgumentParseContext<S>): ParseResult<Either<L, R>> = context.run {
    try {
      left.parse().map { Either.left(it) }
    } catch (ex: ArgumentParseException) {
      right.parse().map { Either.right(it) }
    }
  }

  override fun suggest(context: ArgumentParseContext<S>) = context.run {
    val aList = if (!left.tryParseAndReset()) left.suggest() else emptyList()
    val bList = if (!right.tryParseAndReset()) right.suggest() else emptyList()
    aList + bList
  }
}
