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
 * Constructs an argument that provides custom suggestions.
 *
 * @param first The first suggestion
 * @param more The rest of the suggestions
 * @return The argument
 */
fun <T, S> Argument<T, S>.suggest(first: String, vararg more: String)
    = suggest(listOf(first) + more)

/**
 * Constructs an argument that provides custom suggestions.
 *
 * @param iterable The suggestions
 * @return The argument
 */
fun <T, S> Argument<T, S>.suggest(iterable: Iterable<String>): SuggestionsArgument<T, S>
    = ConstantSuggestionsArgument(this, iterable.toList())

/**
 * Constructs an argument that provides custom suggestions.
 *
 * @param provider The suggestions provider
 * @return The argument
 */
fun <T, S> Argument<T, S>.suggest(provider: () -> List<String>)
    = DynamicSuggestionsArgument(this, provider)

/**
 * A suggestions argument that has a dynamic set of suggestions.
 */
class DynamicSuggestionsArgument<T, S> internal constructor(
    argument: Argument<T, S>,
    private val provider: () -> List<String>
) : SuggestionsArgument<T, S>(argument) {

  override val suggestions: List<String>
    get() = this.provider()
}

/**
 * An argument that provides custom suggestions by
 * overriding the default ones.
 *
 * @property argument The backing and original argument
 */
abstract class SuggestionsArgument<T, S> internal constructor(
    val argument: Argument<T, S>
) : Argument<T, S> {

  /**
   * The suggestions.
   */
  abstract val suggestions: List<String>

  override fun parse(context: ArgumentParseContext<S>) = this.argument.parse(context)
  override fun suggest(context: ArgumentParseContext<S>) = this.suggestions
  override fun transformName(baseName: String) = this.argument.transformName(baseName)
}

/**
 * A suggestions argument that has a constant set
 * of suggestions, the suggestions will never change.
 *
 * @property suggestions The constant suggestions
 */
private class ConstantSuggestionsArgument<T, S>(
    argument: Argument<T, S>,
    override val suggestions: List<String>
) : SuggestionsArgument<T, S>(argument)
