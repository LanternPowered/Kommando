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
import org.lanternpowered.kommando.argument.Argument
import org.lanternpowered.kommando.argument.ArgumentParseException
import org.lanternpowered.kommando.argument.argument

fun <A, B, S> either(a: Argument<A, in S>, b: Argument<B, in S>): Argument<Either<A, B>, S> = argument {
  parse {
    try {
      a.parse().map { Either.left(it) }
    } catch (ex: ArgumentParseException) {
      b.parse().map { Either.right(it) }
    }
  }
  suggest {
    val aList = if (!a.tryParseAndReset()) a.suggest() else emptyList()
    val bList = if (!b.tryParseAndReset()) b.suggest() else emptyList()
    aList + bList
  }
}
