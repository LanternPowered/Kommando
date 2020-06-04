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

import org.lanternpowered.kommando.ValidationContext

/*
 * Usage formatting rules:
 *
 * Required argument:
 *   <name: type>
 *   <type>
 *
 * Optional argument:
 *   [<name1: type1>]
 *   [<type1>]
 *   [<name1: type1> <name2: type2>]
 *   [<type1> <type2>]
 *
 * Literal options (only when less then 5? and not dynamic):
 *   add|remove|modify
 *   [add|remove|modify]
 *   <name: add|remove|modify>
 *   [<name: add|remove|modify>]
 *
 * Literal and sub-commands:
 *   -> Merge literal options and sub-commands into one usage.
 *   -> But not for named/optional literals:
 *      add|remove|modify|<name: these|are|literals>
 *      add|remove|modify|[<name: these|are|literals>]
 *      add|remove|modify|[these|are|literals]
 *
 * Sub-commands:
 *   add|remove|modify
 *
 * Sub-commands or argument:
 *   add|remove|modify|<name: type>
 *   add|remove|modify|<type>
 *
 * Options: (always optional)
 *   Long named options:
 *     [--option <name1: type1>]
 *     [--option <type1>]
 *     [--option [<type1>]]
 *     [--option <name1: type1>, --other <name2: type2>]
 *     [--option <type1>, --other <type2>]
 *     [--option add|remove|modify]
 *
 *   Short named options:
 *     [-f <name1: type1>]
 *     [-f <type1>]
 *     [-fo <name1: type1> <name2: type2>]
 *     [-fo <type1> <type2>]
 *
 * Flags: (always optional) (true if applied|false if not)
 *   Long named flags:
 *     [--flag]
 *     [--flag, --other]
 *
 *   Short named flags
 *     [-f]
 *     [-fo]
 *
 * Examples:
 *   /advancement
 */

/**
 * Converts the output value.
 */
fun <T, N, S> Argument<T, S>.convert(fn: ValidationContext.(T) -> N) = ConvertedArgument(this, fn)

/**
 * An argument that transforms the parsed value
 * from the specified argument.
 */
class ConvertedArgument<T, N, S>(
    val argument: Argument<T, S>,
    val converter: ValidationContext.(T) -> N
) : Argument<N, S> {

  override val usage: ArgumentUsage
    get() = argument.usage

  override fun parse(context: ArgumentParseContext<S>) = context.run {
    argument.parse().map { converter(it) }
  }

  override fun suggest(context: ArgumentParseContext<S>) = context.run {
    argument.suggest()
  }
}

/**
 * Validates the output value.
 */
fun <T, S> Argument<T, S>.validate(fn: ValidationContext.(T) -> Unit) = ValidatedArgument(this, fn)

/**
 * A arguments that validates the parsed value
 * from the specified argument.
 */
class ValidatedArgument<T, S>(
    val argument: Argument<T, S>,
    val validator: ValidationContext.(T) -> Unit
) : Argument<T, S> {

  override val usage: ArgumentUsage
    get() = argument.usage

  override fun parse(context: ArgumentParseContext<S>) = context.run {
    argument.parse().also { validator(it.value) }
  }

  override fun suggest(context: ArgumentParseContext<S>) = context.run {
    argument.suggest()
  }
}
