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
inline fun <reified E : Enum<E>> enum() = enum(enumValues<E>().toList())

/**
 * Constructs an enum [Argument] based on the given [values].
 */
fun <E : Enum<E>> enum(values: List<E>) = choice(values.associateBy { value -> value.name.toLowerCase() })
