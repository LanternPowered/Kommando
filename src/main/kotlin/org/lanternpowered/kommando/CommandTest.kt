/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando

import org.lanternpowered.kommando.argument.boolean
import org.lanternpowered.kommando.argument.convert
import org.lanternpowered.kommando.argument.default
import org.lanternpowered.kommando.argument.int
import org.lanternpowered.kommando.argument.multiple
import org.lanternpowered.kommando.argument.optional
import org.lanternpowered.kommando.argument.validate

val otherCommand = commandOf<Any> {

}

val testCommand = commandOf<Any> {
  val source by source()

  val value by int()
  val bool by boolean()
      .optional()
      .default(false)
      .name("my_boolean_value")

  val converted by boolean()
      .convert { it.toString() + bool }
      .validate {
        check(it.isNotEmpty())
      }
      .optional()
      .default("unknown")

  val test by argumentOf<Int> {
    parse {
      result(1)
    }
  }.name("test-argument")

  // --my-flag
  val flagValue by flag().name("my-flag")

  // --my-option 100
  // --my-option=100
  val optionValue by int().option().name("--my-option", "-o")

  execute {
    println("/<$value> <$bool>")
  }

  // Add children based on other commands
  subcommand("name", "alias", "more", otherCommand)

  subcommand("with", "alias") {
    // Do things

    execute {

    }
  }

  // Define sub commands in this builder
  "sub-command" {
    val another by int(1..100).multiple().name("values")

    execute {
      println("/<$value> <$bool> sub-command <$another>")
    }
  }
}

