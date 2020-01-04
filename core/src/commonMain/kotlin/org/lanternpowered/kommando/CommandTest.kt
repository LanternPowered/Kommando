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
import org.lanternpowered.kommando.argument.choices
import org.lanternpowered.kommando.argument.convert
import org.lanternpowered.kommando.argument.default
import org.lanternpowered.kommando.argument.double
import org.lanternpowered.kommando.argument.int
import org.lanternpowered.kommando.argument.multiple
import org.lanternpowered.kommando.argument.optional
import org.lanternpowered.kommando.argument.rawRemainingString
import org.lanternpowered.kommando.argument.string
import org.lanternpowered.kommando.argument.suggest
import org.lanternpowered.kommando.argument.validate

val otherCommand = command<Any> {

}

val testCommand = command<Any> {
  val source by source()
      .validate {
        check(it !is String)
      }

  val value by int()
  val bool by boolean()
      .optional()
      .default(false)
      .suggest {
        listOf("true")
      }
      .named("bool")

  val converted by boolean()
      .convert { it.toString() + bool }
      .suggest("test", "other")
      .validate {
        check(it.isNotEmpty())
      }
      .optional()
      .default("unknown")

  // test <arg>
  // test_1 <arg>
  // test_1 <arg> test_2 <arg>
  // test_2 <arg> test_1 <arg> test <arg>
  // test_2 <arg> test_1 <arg> test_3 <arg>
  // test_2 <arg> test_1 <arg> test_2 <arg> test_2 <arg>
  /*
  val options = object : options() {
    val test by boolean()
    val test1 by int() named "test_1"
    val test2 by double().repeatable() named "test_2"
    val test3 by double().multiple() named "test_3"
  }
  */

  // Group the options in an object for better overview
  val options = object {
    val test by boolean().option("test") // test true|false
    val test1 by int().option("test_1") // test_1 <int>
    val test2 by option("test_2", "alias") { // test_2 <double> <double>
      val a by double()
      val b by double()
      convert {
        a + b
      }
    }
  }

  val merged by argument {
    val first by int()
    val second by int().named("second")
    convert {
      first + second
    }
  } // Usage will be by default: <int> <second>

  // --my-flag
  val flagValue by flag("--my-flag", "-f")

  // --my-other-flag 100
  val otherFlagValue by int().flag("--my-other-flag", "-o")

  // Add children based on other commands
  subcommand("name", "alias", "more", otherCommand)

  subcommand("with", "alias") {
    // Do things

    execute {

    }
  }

  // Define sub commands in this builder
  "sub-command" {
    // sub-command <values>
    val another by int(1..100).multiple().named("values")

    execute {
      println("/<$value> <$bool> sub-command <$another>")
    }
  }

  subcommand("test", "alias") execute {

  }

  execute {
    println("/<$value> <$bool>")
  }
}

val mcExecuteCommand = command<Any> {
  val source by source()

  // TODO: Figure even more advanced structures, for
  //       options that take multiple arguments, etc.

  val anchor = object : choices<String>() {
    val eyes by "eyes"
    val feet by "feet"
  }

  // align <axis>
  val align by string()
      .named("axis")
      .option("align")

  // anchored <anchor>
  val anchored by anchor
      .named("anchor")
      .option("anchored")

  // /execute align <axis> anchored <anchor>
  // /execute align <axis>
  // /execute anchored <anchor> align <axis>
  // /execute anchored <anchor>

  "run" {
    val command by rawRemainingString() // TODO: Allow to redirect to other commands? Special argument type?

    execute {
      when (anchored) {
        anchor.eyes -> {
        }
        anchor.feet -> {
        }
        else -> {
          // Anchor is null
        }
      }

      // /execute align <axis> anchored <anchor> run <command>
      // /execute align <axis> run <command>
      // /execute anchored <anchor> align <axis> run <command>
      // /execute anchored <anchor> run <command>
      println("/advancement align <axis: ${align}> anchored <${anchored}> run <$command>")
    }
  }
}

val mcAdvancementCommand = command<Any> {
  val source by source()

  // local enum classes aren't supported yet by kotlin

  val actions = object : choices<String>() {
    val grant by "grant"
    val revoke by "revoke" named "revoke"
  }

  val action by actions
  val target by string()

  "everything" execute {
    println("/advancement $action <target: $target> everything")
  }

  groupBefore {
    val advancement by string()

    "only" {
      val criterion by string().optional()

      execute {
        println("/advancement $action <target: $target> only <advancement: $advancement> [<criterion: $criterion>]")

        when (action) {
          actions.grant -> {
            // Do this
            println("grant")
          }
          actions.revoke -> {
            // Do that
            println("revoke")
          }
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
