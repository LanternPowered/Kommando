/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando.impl

import org.lanternpowered.kommando.ValidationContext
import org.lanternpowered.kommando.argument.ArgumentParseException
import org.lanternpowered.kommando.argument.ArgumentReader
import kotlin.js.JsName

internal open class ArgumentReaderImpl(
    override val content: String
) : ArgumentReader, ValidationContext {

  companion object {

    const val SingleQuote = '\''
    const val DoubleQuote = '\"'
  }

  override var cursor: Int = 0

  override fun canRead(offset: Int) = this.cursor + offset < this.content.length

  override fun canRead() = this.cursor < this.content.length

  /**
   * Checks whether the reader is readable.
   */
  private fun checkReadable() {
    if (!canRead()) {
      error("The end of the reader is reached.")
    }
  }

  /**
   * Checks whether the reader is readable.
   */
  private fun checkReadable(offset: Int) {
    if (!canRead(offset)) {
      error("The end of the reader is reached.")
    }
  }

  override fun peek(): Char {
    checkReadable()
    return this.content[this.cursor]
  }

  override fun peek(offset: Int): Char {
    checkReadable(offset)
    return this.content[this.cursor + offset]
  }

  override fun read(): Char {
    checkReadable()
    return this.content[this.cursor++]
  }

  override fun skip() {
    this.cursor++
  }

  override fun skipWhitespaces() {
    while (canRead() && peek().isWhitespace()) {
      read()
    }
  }

  private fun isQuoteChar(c: Char) = c == SingleQuote || c == DoubleQuote

  override fun readString(): String {
    if (!canRead()) {
      return ""
    }
    val quoteChar = peek()
    if (isQuoteChar(quoteChar)) {
      return readQuotedString(quoteChar)
    }
    return readUnquotedString()
  }

  override fun tryReadString(): String? {
    if (!canRead()) {
      return ""
    }
    val quoteChar = peek()
    if (isQuoteChar(quoteChar)) {
      return tryReadQuotedString(quoteChar)
    }
    return tryReadUnquotedString()
  }

  override fun readQuotedString(): String {
    val quoteChar = peek()
    if (!isQuoteChar(quoteChar)) {
      error("Expected a quote character.")
    }
    return readQuotedString(quoteChar)
  }

  override fun tryReadQuotedString(): String? {
    val quoteChar = peek()
    if (!isQuoteChar(quoteChar)) {
      return null
    }
    return tryReadQuotedString(quoteChar)
  }

  /**
   * Checks whether the given character should be quoted.
   */
  private fun shouldCharBeQuoted(c: Char)
      = c !in '0'..'9' && c !in 'A'..'Z' && c !in 'a'..'z' && c != '_' && c != '-' && c != '.' && c != '+'

  override fun readUnquotedString(): String {
    val start = this.cursor
    while (canRead() && !shouldCharBeQuoted(peek())) {
      read()
    }
    return this.content.substring(start, this.cursor)
  }

  override fun tryReadUnquotedString(): String? {
    return readUnquotedString()
  }

  @JsName("readQuotedStringWithQuoteChar")
  private fun readQuotedString(quoteChar: Char): String {
    return readQuotedString0(quoteChar) { error(it) }
  }

  @JsName("tryReadQuotedStringWithQuoteChar")
  private fun tryReadQuotedString(quoteChar: Char): String? {
    val cursor = this.cursor
    return readQuotedString0(quoteChar) {
      this.cursor = cursor
      // Return null in case of an error.
      return null
    }
  }

  private inline fun readQuotedString0(quoteChar: Char, error: (message: String) -> Nothing): String {
    skip() // Quote char

    val builder = StringBuilder()
    while (true) {
      if (!canRead()) {
        // No quote char was found as last character
        error("No string termination quote character was found.")
      }
      var c = peek()
      // If we find a quote, we reached the end of the string
      when (c) {
        quoteChar -> {
          skip() // Skip quote for next value
          return builder.toString()
        }
        '\\' -> { // The next character is literal
          skip()
          c = read()
        }
        else -> {
          skip()
        }
      }
      builder.append(c)
    }
  }

  private fun isAllowedNumberChar(c: Char) = isAllowedIntChar(c) || c == '.'
  private fun isAllowedIntChar(c: Char) = c in '0'..'9' || c == '-' || c == '+'

  private inline fun <T : Any> readNumber(name: String, charValidator: (Char) -> Boolean, fn: String.() -> T?): T {
    val start = this.cursor
    while (canRead() && charValidator(peek())) {
      skip()
    }
    val value = this.content.substring(start, this.cursor)
    if (value.isEmpty()) {
      error("Expected $name")
    }
    return fn(value) ?: error("Invalid $name '$value'")
  }

  private inline fun <T : Any> tryReadNumber(charValidator: (Char) -> Boolean, fn: String.() -> T?): T? {
    val start = this.cursor
    while (canRead() && charValidator(peek())) {
      skip()
    }
    val value = this.content.substring(start, this.cursor)
    if (value.isEmpty()) {
      return null
    }
    val number = fn(value)
    if (number == null) {
      this.cursor = start
    }
    return number
  }

  override fun readInt() = readNumber("int", ::isAllowedIntChar) { toIntOrNull() }

  override fun tryReadInt() = tryReadNumber(::isAllowedIntChar) { toIntOrNull() }

  override fun readDouble() = readNumber("double", ::isAllowedNumberChar) { toDoubleOrNull() }

  override fun tryReadDouble() = tryReadNumber(::isAllowedNumberChar) { toDoubleOrNull() }

  override fun readFloat() = readNumber("float", ::isAllowedNumberChar) { toFloatOrNull() }

  override fun tryReadFloat() = tryReadNumber(::isAllowedNumberChar) { toFloatOrNull() }

  override fun readLong() = readNumber("long", ::isAllowedIntChar) { toLongOrNull() }

  override fun tryReadLong() = tryReadNumber(::isAllowedIntChar) { toLongOrNull() }

  override fun readBoolean(): Boolean {
    val cursor = this.cursor
    val value = readString()
    if (value.isEmpty()) {
      error("Expected boolean")
    }
    return when (value) {
      "true" -> true
      "false" -> false
      else -> {
        this.cursor = cursor
        error("Invalid boolean '$value'")
      }
    }
  }

  override fun tryReadBoolean(): Boolean? {
    val cursor = this.cursor
    val value = readString()
    if (value.isEmpty()) {
      this.cursor = cursor
      return null
    }
    return when (value) {
      "true" -> true
      "false" -> false
      else -> {
        this.cursor = cursor
        null
      }
    }
  }

  override fun <T : Any> tryRead(fn: ArgumentReader.() -> T): T? {
    val cursor = this.cursor
    return try {
      this.fn()
    } catch (ex: ArgumentParseException) {
      this.cursor = cursor
      null
    }
  }

  override fun error(message: Any): Nothing {
    throw ArgumentParseException(messageOf(message))
  }

  override fun check(state: Boolean, message: () -> Any) {
    if (!state) {
      error(message)
    }
  }

  override fun check(state: Boolean) {
    if (!state) {
      error("")
    }
  }

}
