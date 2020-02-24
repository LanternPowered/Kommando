/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando.impl.suggestion

import org.lanternpowered.kommando.Message
import org.lanternpowered.kommando.suggestion.Suggestion

internal data class SuggestionImpl(
    override val range: IntRange,
    override val value: String,
    override val tooltip: Message?
) : Suggestion
