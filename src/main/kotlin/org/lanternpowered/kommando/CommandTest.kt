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

import org.lanternpowered.kommando.argument.choices
import org.lanternpowered.kommando.argument.boolean
import org.lanternpowered.kommando.argument.convert
import org.lanternpowered.kommando.argument.default
import org.lanternpowered.kommando.argument.int
import org.lanternpowered.kommando.argument.multiple
import org.lanternpowered.kommando.argument.optional
import org.lanternpowered.kommando.argument.string
import org.lanternpowered.kommando.argument.validate

val otherCommand = command<Any> {

}

val testCommand = command<Any> {
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

  val test by argument<Int> {
    parse {
      result(1)
    }
  }.name("test-argument")

  // --my-flag
  val flagValue by flag().name("my-flag")

  // --my-option 100
  // --my-option=100
  val optionValue by int().option().name("--my-option", "-o")

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

  execute {
    println("/<$value> <$bool>")
  }
}

val mcAdvancementCommand = command<Any> {
  val source by source()

  // local enum classes aren't supported yet by kotlin

  val actions = object : choices<String>() {
    val grant by "grant"
    val revoke by "revoke" to "revoke"
  }

  val action by actions
  val target by string()

  "everything" execute {
    println("/advancement $action <target: $target> everything")
  }

  group {
    val advancement by string()

    "only" {
      val criterion by string().optional()

      execute {
        println("/advancement $action <target: $target> only <advancement: $advancement> [<criterion: $criterion>]")

        if (action == actions.grant) {
          // Do this
          println("grant")
        } else {
          // Do that
          println("revoke")
        }
      }
    }

    "from" execute {
      println("/advancement $action <target: $target> from <advancement: $advancement>")
    }

    "through" execute {
      println("/advancement $action <target: $target> through <advancement: $advancement>")
    }

    "until" execute {
      println("/advancement $action <target: $target> until <advancement: $advancement>")
    }
  }
}
