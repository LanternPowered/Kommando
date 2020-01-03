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
 * Constructs a boolean [Argument].
 */
fun boolean() = BooleanArgument()

/**
 * An argument that parses a boolean value.
 */
class BooleanArgument internal constructor() : Argument<Boolean, Any> {

  private val suggestions = listOf("true", "false")

  override fun parse(context: ArgumentParseContext<Any>) = context.run {
    result(parseBoolean())
  }

  override fun suggest(context: ArgumentParseContext<Any>) = this.suggestions
}
