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
fun string() = QuotableStringArgument()

/**
 * A string [Argument] that parses a quotable string.
 *
 * If quotes are specified, it will be parsing until the next quote. Quotes
 * can be escaped so that they are read literal. If no quotes are specified,
 * only a single word will be read.
 */
class QuotableStringArgument internal constructor() : Argument<String, Any> {

  override fun parse(context: ArgumentParseContext<Any>) = context.run {
    result(readString())
  }
}

/**
 * Constructs a string [Argument] that parses a string from all the
 * remaining content. It ignores any quotes, everything will be read
 * literally.
 */
fun rawRemainingString() = RawRemainingStringArgument()

/**
 * A string [Argument] that parses a string from all the
 * remaining content. It ignores any quotes, everything will be read
 * literally.
 */
class RawRemainingStringArgument internal constructor() : Argument<String, Any> {

  override fun parse(context: ArgumentParseContext<Any>) = context.run {
    val start = context.cursor
    val end = context.input.length
    context.cursor = end
    result(context.input.substring(start, end))
  }
}

/**
 * Constructs a string [Argument] that a single word.
 */
fun word() = SingleWordArgument()

/**
 * Constructs a string [Argument] that parses a single word.
 */
class SingleWordArgument internal constructor() : Argument<String, Any> {

  override fun parse(context: ArgumentParseContext<Any>) = context.run {
    result(readUnquotedString())
  }
}
