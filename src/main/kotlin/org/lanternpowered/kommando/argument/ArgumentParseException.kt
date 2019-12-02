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

import org.lanternpowered.kommando.CommandException
import org.lanternpowered.kommando.LiteralMessage
import org.lanternpowered.kommando.Message

class ArgumentParseException(errorMessage: Message) : CommandException(errorMessage) {

  constructor(errorMessage: String) : this(LiteralMessage(errorMessage))
}
