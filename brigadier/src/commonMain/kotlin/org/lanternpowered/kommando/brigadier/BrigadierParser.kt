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

package org.lanternpowered.kommando.brigadier

/**
 * Constructs a simple minecraft parser.
 */
fun BrigadierParser(id: String): BrigadierParser = SimpleBrigadierParser(id)

/**
 * Represents a Minecraft parser. These only represents the parser so that
 * a command tree can be built for the Minecraft client.
 */
interface BrigadierParser {

  /**
   * The id of the parser
   */
  val id: String
}

private class SimpleBrigadierParser(override val id: String) : BrigadierParser

/**
 * A parser for double values.
 */
data class BrigadierDoubleParser(val min: Double = -Double.MAX_VALUE, val max: Double = Double.MAX_VALUE) : BrigadierParser {

  /**
   * Constructs a new double parser from the range.
   */
  constructor(range: ClosedRange<Double>) : this(range.start, range.endInclusive)

  override val id = "brigadier:double"
}

/**
 * A parser for float values.
 */
data class BrigadierFloatParser(val min: Float = -Float.MAX_VALUE, val max: Float = Float.MAX_VALUE) : BrigadierParser {

  /**
   * Constructs a new float parser from the range.
   */
  constructor(range: ClosedRange<Float>) : this(range.start, range.endInclusive)

  override val id = "brigadier:float"
}

/**
 * A parser for int values.
 */
data class BrigadierIntParser(val min: Int = Int.MIN_VALUE, val max: Int = Int.MAX_VALUE) : BrigadierParser {

  /**
   * Constructs a new int parser from the range.
   */
  constructor(range: ClosedRange<Int>) : this(range.start, range.endInclusive)

  override val id = "brigadier:integer"
}

/**
 * A parser for string values.
 */
data class BrigadierStringParser(val mode: Mode = Mode.SingleWord) : BrigadierParser {

  enum class Mode {
    SingleWord,
    QuotablePhrase,
    GreedyPhrase,
  }

  override val id = "brigadier:string"
}

/**
 * A parser for boolean values.
 */
val BrigadierBooleanParser = BrigadierParser("brigadier:boolean")

/**
 * A parser for int ranges.
 */
val MinecraftIntRangeParser = BrigadierParser("minecraft:int_range")

/**
 * A parser for float ranges.
 */
val MinecraftFloatRangeParser = BrigadierParser("minecraft:float_range")
