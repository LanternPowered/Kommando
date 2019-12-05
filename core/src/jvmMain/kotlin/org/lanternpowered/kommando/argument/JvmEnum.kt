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

import kotlin.reflect.KClass

/**
 * Constructs an enum [Argument] based on the given enum type [E].
 */
fun <E : Enum<E>> enum(type: KClass<E>): Argument<E, Any>
    = enum(type.java.enumConstants.asList())

/**
 * Constructs an enum [Argument] based on the given enum type [E].
 */
fun <E : Enum<E>> enum(type: Class<E>): Argument<E, Any>
    = enum(type.enumConstants.asList())
