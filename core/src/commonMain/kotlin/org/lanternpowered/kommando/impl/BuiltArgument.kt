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

import org.lanternpowered.kommando.BuiltArgument
import org.lanternpowered.kommando.argument.Argument

internal class BuiltArgumentImpl<T, S>(
    val argument: Argument<T, S>
) : BuiltArgument<T, S>
