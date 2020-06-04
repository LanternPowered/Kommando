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
 * Constructs a [NamedArgument] with the argument and the given name.
 */
fun <T, S> Argument<T, S>.named(name: String): NamedArgument<T, S> =
    if (this is NamedArgument) this.argument.named(name) else NamedArgument(name, this)

/**
 * An argument which modifies the default name of the argument.
 */
class NamedArgument<T, S> internal constructor(
    name: String,
    val argument: Argument<T, S>
) : Argument<T, S> by argument {

  override val usage: ArgumentUsage = argument.usage.copy(name = name)
}
