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

import org.lanternpowered.kommando.tree.FlagNames
import org.lanternpowered.kommando.util.ToStringHelper
import org.lanternpowered.kommando.impl.util.collection.contentEquals
import org.lanternpowered.kommando.impl.util.collection.contentHashCode

internal class FlagNamesImpl(
    override val long: List<String>,
    override val short: List<Char>
) : FlagNames {

  override fun hashCode(): Int {
    var result = this.long.contentHashCode()
    result = 31 * result + this.short.contentHashCode()
    return result
  }

  override fun equals(other: Any?): Boolean {
    return other is FlagNamesImpl &&
        other.long contentEquals this.long &&
        other.short contentEquals this.short
  }

  override fun toString(): String {
    return ToStringHelper()
        .add("long", this.long.joinToString(separator = ", ", prefix = "[", postfix = "]"))
        .add("short", this.short.joinToString(separator = ", ", prefix = "[", postfix = "]"))
        .toString()
  }
}
