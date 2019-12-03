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
 * Constructs an enum [Argument] based on the given enum type [E].
 */
inline fun <reified E : Enum<E>> enum(): Argument<E, Any> = enum(enumValues<E>().toList())

/**
 * Constructs an enum [Argument] based on the given [values].
 */
fun <E : Enum<E>> enum(values: List<E>): Argument<E, Any> = argument {
  val mappedValues = values.associateBy { value -> value.name.toLowerCase() }
  val joinedKeys = mappedValues.keys.joinToString(", ")
  parse {
    val name = parseString()
    val value = mappedValues[name]
    if (value != null) result(value) else error("Enum value must be one of [$joinedKeys], but found $name")
  }
}
