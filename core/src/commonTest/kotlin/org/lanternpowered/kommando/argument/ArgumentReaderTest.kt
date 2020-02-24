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

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ArgumentReaderTest {

  @JsName("skipWhiteSpaces")
  @Test
  fun `skip whitespaces`() {
    val reader = argumentReaderOf(" \t\na")
    reader.skipWhitespaces()
    assertEquals('a', reader.read())
  }

  @JsName("skipWhitespaces_NoSpace")
  @Test
  fun `skip whitespaces - no space`() {
    val reader = argumentReaderOf("a")
    reader.skipWhitespaces()
    assertEquals('a', reader.read())
  }

  @JsName("readUnquotedString")
  @Test
  fun `read unquoted string`() {
    val reader = argumentReaderOf("test a b")
    assertEquals("test", reader.readUnquotedString())
    reader.skipWhitespaces()
    assertEquals("a", reader.readUnquotedString())
    reader.skipWhitespaces()
    assertEquals("b", reader.readUnquotedString())
  }

  @JsName("readQuotedString")
  @Test
  fun `read quoted string`() {
    val reader = argumentReaderOf("'hello' \"test\"")
    assertEquals("hello", reader.readQuotedString())
    reader.skipWhitespaces()
    assertEquals("test", reader.readQuotedString())
  }

  @JsName("readQuotedString_WithLiteralQuotes")
  @Test
  fun `read quoted string - with literal quotes`() {
    val reader = argumentReaderOf("'hello\\'' \"\\\"test\"")
    assertEquals("hello'", reader.readQuotedString())
    reader.skipWhitespaces()
    assertEquals("\"test", reader.readQuotedString())
  }

  @JsName("readInvalidQuotedString_NoQuotes")
  @Test
  fun `read invalid quoted string - no quotes`() {
    val reader = argumentReaderOf("hello")
    assertFails { reader.readQuotedString() }
  }

  @JsName("readInvalidQuotedString_NoTerminationQuote")
  @Test
  fun `read invalid quoted string - no termination quote`() {
    val reader = argumentReaderOf("\"hello")
    assertFails { reader.readQuotedString() }
  }

  @JsName("readString")
  @Test
  fun `read string`() {
    val reader = argumentReaderOf("'hello' \"test\" world")
    assertEquals("hello", reader.readString())
    reader.skipWhitespaces()
    assertEquals("test", reader.readString())
    reader.skipWhitespaces()
    assertEquals("world", reader.readString())
  }

  @JsName("readInt")
  @Test
  fun `read int`() {
    val reader = argumentReaderOf("203578")
    assertEquals(203578, reader.readInt())
  }

  @JsName("readInt_explicitPositive")
  @Test
  fun `read int - explicit positive`() {
    val reader = argumentReaderOf("+203578")
    assertEquals(203578, reader.readInt())
  }

  @JsName("readInt_negative")
  @Test
  fun `read int - negative`() {
    val reader = argumentReaderOf("-203578")
    assertEquals(-203578, reader.readInt())
  }

  @JsName("readInt_invalid")
  @Test
  fun `read int - invalid`() {
    val reader = argumentReaderOf("test")
    assertFails { reader.readInt() }
  }

  @JsName("readLong")
  @Test
  fun `read long`() {
    val reader = argumentReaderOf("203578")
    assertEquals(203578L, reader.readLong())
  }

  @JsName("readLong_explicitPositive")
  @Test
  fun `read long - explicit positive`() {
    val reader = argumentReaderOf("+203578")
    assertEquals(203578L, reader.readLong())
  }

  @JsName("readLong_negative")
  @Test
  fun `read long - negative`() {
    val reader = argumentReaderOf("-203578")
    assertEquals(-203578L, reader.readLong())
  }

  @JsName("readLong_untilInvalidChar")
  @Test
  fun `read long - invalid value - floating point number`() {
    val reader = argumentReaderOf("12.36")
    assertEquals(12L, reader.readLong())
  }

  @JsName("readLong_invalid")
  @Test
  fun `read long - invalid`() {
    val reader = argumentReaderOf("test")
    assertFails { reader.readLong() }
  }
}
