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

sealed class ParseResult<T> {

  /**
   * Maps the value if this result is successful.
   */
  fun <N> map(fn: (value: T) -> N): ParseResult<N> {
    if (this is Success) {
      return Success(fn(this.value), this.potentialError)
    }
    @Suppress("UNCHECKED_CAST")
    return this as ParseResult<N>
  }

  /**
   * Merges the two [ParseResult]s.
   */
  operator fun <O> plus(other: ParseResult<O>): ParseResult<Pair<T, O>> {
    return when {
      this is Error -> mapType()
      other is Error -> other.mapType()
      else -> {
        this as Success
        other as Success
        Success(this.value to other.value, other.potentialError ?: this.potentialError)
      }
    }
  }

  /**
   * Maps this [ParseResult] as a nullable value type.
   */
  fun asNullable(): ParseResult<T?> {
    @Suppress("UNCHECKED_CAST")
    return this as ParseResult<T?>
  }

  /**
   * Represents a parse result that was successful. But can have a potential error.
   *
   * E.g. for a potential error: if a optional argument is being parsed it will be successful
   * regardless of it's value being successfully parsed or not, it will just return null otherwise.
   * However, a potential error will be passed along, in the case that the optional argument
   * is the last possible argument. The last argument is not allowed to fail to parse, because
   * otherwise 'too many arguments' will be detected, the potential error will be thrown in that
   * case.
   */
  data class Success<T>(val value: T, val potentialError: Message? = null) : ParseResult<T>()

  /**
   * Represents a parse result that errored.
   */
  data class Error<T>(val error: Message) : ParseResult<T>() {

    /**
     * Maps the value type to a new type [N].
     */
    fun <N> mapType(): ParseResult<N> {
      @Suppress("UNCHECKED_CAST")
      return this as ParseResult<N>
    }
  }
}
