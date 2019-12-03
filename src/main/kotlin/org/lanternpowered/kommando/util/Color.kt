/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("FunctionName")

package org.lanternpowered.kommando.util

/**
 * Constructs a new color from the rgba values.
 */
fun Color(red: UByte, green: UByte, blue: UByte, alpha: UByte = 255u): Color
    = Color(red.toInt(), green.toInt(), blue.toInt(), alpha.toInt())

/**
 * Constructs a new color from the rgba values. Each component with range 0 - 255
 */
fun Color(red: Int, green: Int, blue: Int, alpha: Int = 255): Color
    = Color((alpha shl 24) or (red shl 16) or (green shl 8) or blue)

/**
 * Constructs a new color from the rgba value.
 */
fun Color(rgba: UInt): Color
    = Color(rgba.toInt())

/**
 * Represents a color.
 *
 * @property rgba The RGBA value.
 */
inline class Color(val rgba: Int) {

  /**
   * The RGB value.
   */
  val rgb: Int get() = this.rgba and 0xffffff

  /**
   * The alpha component. 0 - 255
   */
  val alpha: Int get() = (this.rgba ushr 24) and 0xff

  /**
   * The red component. 0 - 255
   */
  val red: Int get() = (this.rgba ushr 16) and 0xff

  /**
   * The green component. 0 - 255
   */
  val green: Int get() = (this.rgba ushr 8) and 0xff

  /**
   * The blue component. 0 - 255
   */
  val blue: Int get() = this.rgba and 0xff

  /**
   * Converts this color into a solid color, with the alpha value set to maximum.
   */
  fun solid(): Color = Color(this.rgba or 0xff000000.toInt())

  override fun toString(): String
      = if (this.alpha == 255) "Color(red=$red, green=$green, blue=$blue)" else "Color(red=$red, green=$green, blue=$blue, alpha=$alpha)"

  companion object {

    /**
     * Constructs a new color with the given rgba value.
     */
    fun rgba(rgba: Long) = Color(rgba.toUInt().toInt())

    /**
     * Constructs a new color with the given rgba value.
     */
    fun rgba(rgba: Int) = Color(rgba)

    /**
     * Constructs a new color with the given rgb value.
     */
    fun rgb(rgb: Int) = Color(rgb).solid()
  }
}

object Colors {

  val White = Color.rgb(0xffffff)

  val Silver = Color.rgb(0xc0c0c0)

  val Gray = Color.rgb(0x808080)

  val Black = Color.rgb(0x000000)

  val Red = Color.rgb(0xff0000)

  val Maroon = Color.rgb(0x800000)

  val Yellow = Color.rgb(0xffff00)

  val Gold = Color.rgb(0xffd700)

  val Orange = Color.rgb(0xffa500)

  val Olive = Color.rgb(0x808000)

  val Lime = Color.rgb(0x00ff00)

  val Green = Color.rgb(0x008000)

  val Cyan = Color.rgb(0x0000ff)

  val Teal = Color.rgb(0x008080)

  val Blue = Color.rgb(0x0000ff)

  val Navy = Color.rgb(0x000080)

  val Violet = Color.rgb(0xee82ee)

  val Magenta = Color.rgb(0xff00ff)

  val Purple = Color.rgb(0x800080)

  val Pink = Color.rgb(0xffc0cb)

  val Brown = Color.rgb(0x8b4513)
}
