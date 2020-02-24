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

import org.lanternpowered.kommando.impl.ArgumentReaderImpl

/**
 * Constructs an argument reader for the given content.
 */
fun argumentReaderOf(content: String): ArgumentReader {
  return ArgumentReaderImpl(content)
}

interface ArgumentReader {

  /**
   * The complete content.
   */
  val content: String

  /**
   * The cursor position of the reader.
   */
  var cursor: Int

  /**
   * Gets whether there's a character
   * available to be read.
   */
  fun canRead(): Boolean

  /**
   * Gets whether there's a character
   * available to be read at the given offset.
   */
  fun canRead(offset: Int): Boolean

  /**
   * Skips the next character.
   */
  fun skip()

  /**
   * Moves the cursor to the next
   * non-whitespace character.
   */
  fun skipWhitespaces()

  /**
   * Peeks the next character in the reader.
   */
  fun peek(): Char

  /**
   * Peeks the next character with the given offset in the reader.
   */
  fun peek(offset: Int): Char

  /**
   * Reads the next character in the reader
   * and increases the cursor.
   */
  fun read(): Char

  /**
   * Reads a string. This string may be quoted or unquoted.
   */
  fun readString(): String

  /**
   * Reads a string. This string may be quoted or unquoted. On
   * failure, resets the cursor and returns null.
   */
  fun tryReadString(): String?

  /**
   * Reads an unquoted string.
   */
  fun readUnquotedString(): String

  /**
   * Reads an unquoted string. On failure, resets
   * the cursor and returns null.
   */
  fun tryReadUnquotedString(): String?

  /**
   * Reads a quoted string. The quotes are required.
   */
  fun readQuotedString(): String

  /**
   * Attempts to read a quoted string. The quotes are
   * required. On failure, resets the cursor and returns
   * null.
   */
  fun tryReadQuotedString(): String?

  /**
   * Reads an int.
   */
  fun readInt(): Int

  /**
   * Attempts to read an int. On failure, resets
   * the cursor and returns null.
   */
  fun tryReadInt(): Int?

  /**
   * Reads a double.
   */
  fun readDouble(): Double

  /**
   * Attempts to read a double. On failure, resets
   * the cursor and returns null.
   */
  fun tryReadDouble(): Double?

  /**
   * Reads a float.
   */
  fun readFloat(): Float

  /**
   * Attempts to read a float. On failure, resets
   * the cursor and returns null.
   */
  fun tryReadFloat(): Float?

  /**
   * Reads a boolean.
   */
  fun readBoolean(): Boolean

  /**
   * Attempts to read a boolean. On failure, resets
   * the cursor and returns null.
   */
  fun tryReadBoolean(): Boolean?

  /**
   * Reads a long.
   */
  fun readLong(): Long

  /**
   * Attempts to read a long. On failure, resets
   * the cursor and returns null.
   */
  fun tryReadLong(): Long?

  /**
   * Tries to read a value from the argument reader, if
   * reading fails, null will be returned and the cursor
   * will be reset to the point before attempting to read.
   */
  fun <T : Any> tryRead(fn: ArgumentReader.() -> T): T?
}
