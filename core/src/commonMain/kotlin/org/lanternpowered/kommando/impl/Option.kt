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

import org.lanternpowered.kommando.BoundOption
import org.lanternpowered.kommando.Option
import org.lanternpowered.kommando.Path
import org.lanternpowered.kommando.argument.Argument

internal class OptionImpl(
    val path: Path
) : Option {

  override fun repeatable() = RepeatableOptionImpl(this.path)
}

internal class RepeatableOptionImpl(
    val path: Path
) : Option.Repeatable

internal class BoundOptionImpl<T, S>(
    val path: Path,
    val argument: Argument<T, S>
) : BoundOption<T, S>

internal class RepeatableBoundOptionImpl<T, S>(
    val path: Path,
    val argument: Argument<T, S>
) : BoundOption.Repeatable<T, S>
