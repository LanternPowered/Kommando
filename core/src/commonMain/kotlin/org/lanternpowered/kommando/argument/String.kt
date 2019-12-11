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
 * Constructs a string [Argument] that parses a quotable string.
 *
 * If quotes are specified, it will be parsing until the next quote. Quotes
 * can be escaped so that they are read literal. If no quotes are specified,
 * only a single word will be read.
 */
fun string() = StringArgument()

/**
 * Constructs a string [Argument] that parses a string from all the
 * remaining content. It ignores any quotes, everything will be read
 * literally.
 */
fun rawRemainingString() = StringArgument(StringParseMode.RemainingPhrase)

/**
 * Constructs a string [Argument] that parses one word.
 */
fun word(): Argument<String, Any> = StringArgument(StringParseMode.SingleWord)

/**
 * Represents the mode for parsing strings.
 */
enum class StringParseMode {
  SingleWord,
  QuotablePhrase,
  RemainingPhrase,
}

/**
 * An argument that parses string values.
 */
data class StringArgument(val mode: StringParseMode = StringParseMode.QuotablePhrase) : Argument<String, Any> {

  override fun parse(context: ArgumentParseContext<Any>) = context.run {
    result(when (mode) {
      StringParseMode.SingleWord -> parseUnquotedString()
      StringParseMode.QuotablePhrase -> parseString()
      StringParseMode.RemainingPhrase -> parseRemainingString()
    })
  }
}
