/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando.color.argument

import org.lanternpowered.kommando.argument.Argument
import org.lanternpowered.kommando.argument.argument
import org.lanternpowered.kommando.color.Color
import org.lanternpowered.kommando.color.Colors
import org.lanternpowered.kommando.util.clamp

/**
 * The inbuilt color presets, will can be used by name.
 */
private val inbuiltPresets = mapOf(
    "white" to Colors.White,
    "silver" to Colors.Silver,
    "gray" to Colors.Gray,
    "black" to Colors.Black,
    "red" to Colors.Red,
    "maroon" to Colors.Maroon,
    "yellow" to Colors.Yellow,
    "gold" to Colors.Gold,
    "orange" to Colors.Orange,
    "olive" to Colors.Olive,
    "lime" to Colors.Lime,
    "green" to Colors.Green,
    "cyan" to Colors.Cyan,
    "teal" to Colors.Teal,
    "blue" to Colors.Blue,
    "navy" to Colors.Navy,
    "violet" to Colors.Violet,
    "magenta" to Colors.Magenta,
    "purple" to Colors.Purple,
    "pink" to Colors.Pink,
    "brown" to Colors.Brown
)

/**
 * Constructs a color argument.
 */
fun color(presets: Map<String, Color> = inbuiltPresets): Argument<Color, Any> = argument {
  val immutablePresets = presets.toMap()
  parse {
    val cursor = this.cursor
    val value = parseUnquotedString()
    // Try as preset
    val color = immutablePresets[value]
    if (color != null)
      return@parse result(color)
    // Try as hex value
    if (value.startsWith("0x")) {
      // Parse a hex value
      val hex = value.substring(2)
      val hexValue = hex.toIntOrNull(16) ?: error("Expected hex color, but found $value")
      return@parse result(if (hex.length == 6) Color.rgb(hexValue) else Color.rgba(hexValue))
    }
    // Reset cursor
    this.cursor = cursor
    // Try as RGB values
    val red = clamp(parseFloat(), 0f..1.0f)
    val green = clamp(parseFloat(), 0f..1.0f)
    val blue = clamp(parseFloat(), 0f..1.0f)
    result(Color(red, green, blue))
  }
}
