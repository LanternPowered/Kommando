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
fun <T, N, S> Argument<T, S>.convert(fn: ValidationContext.(T) -> N): Argument<N, S> = argument {
  val argument = this@convert
  parse {
    argument.parse().map { fn(it) }
  }
  suggest {
    argument.suggest()
  }
  name {
    argument.transformName(baseName)
  }
}

fun <T, S> Argument<T, S>.validate(fn: ValidationContext.(T) -> Unit): Argument<T, S> = argument {
  val argument = this@validate
  parse {
    argument.parse().also { fn(it.value) }
  }
  suggest {
    argument.suggest()
  }
  name {
    argument.transformName(baseName)
  }
}
