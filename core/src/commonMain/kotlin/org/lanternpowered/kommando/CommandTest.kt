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
import org.lanternpowered.kommando.argument.word

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

  // Group the options in an object for better overview
  val options = object {
    val test by "test".option() with boolean() // test true|false
    val test1 by "test_1".option() with int() // test_1 <int>
    val test2 by ("test_2" or "alias").option() with argument { // test_2 <double> <double>
      val a by double()
      val b by double()
      convert {
        a + b
      }
    }
    // No argument, so true if present, false otherwise
    val test3 by "test_3".option() // test_3
    // Repeatable, no argument, int value with the amount of times
    val test4 by "test_4".option().repeatable() // test_4
  }

  // first <int> second <value>
  // second <value> first <int>
  val otherOptions by options<() -> Unit> {
    // first <int>
    "first" with int()
        .convert {
          fun() = println(it)
        }
    // second <value>
    "second".repeatable() with double()
        .convert {
          fun() = println(it)
        }
        .named("value")
    // third <v1> <v2>
    "third" {
      val v1 by double().named("v1")
      val v2 by double().named("v2")
      convert {
        fun() = println("$v1 + $v2 = ${v1 + v2}")
      }
    }
  }

  "test" execute {
    otherOptions.forEach { it() }
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
  "name" or "alias" or "more" then otherCommand

  "with" or "alias" {
    // Do things

    execute {

    }
  }

  // a b|c <int>
  // a <int>
  "a" then ("b" or "c" or otherwise) {
    val v by int()

    execute {

    }
  }

  // a
  "a" {
    val v by int()

    // a <int> b <double>
    "b" {
      val d by double()
    }

    // a <int> <word>
    otherwise {
      val s by word()
    }
  }

  // a|b|c <int> d|e
  "a" or "b" or "c" {
    val v by int()

    "d" or "e" execute {

    }
  }

  // a|b|c
  "a" or "b" or "c" {
    val v by int()

    group.beforeArguments {
      val s by word()

      // a|b|c f <word>
      "f" execute {
      }

      // a|b|c q <word>
      "q" execute {
      }
    }

    // a|b|c d|e <int>
    ("d" or "e").beforeArguments execute {

    }

    // a|b|c <int> d|e
    "d" or "e" execute {

    }

    // a|b|c <int> <word>
    otherwise {
      val s by word()
    }
  }

  val arg by argument {
    // plus|minus <v1> <v2>
    group.beforeArguments {
      val v1 by int().named("v1")
      val v2 by int().named("v2")

      "plus" convert { v1 + v2 }
      "minus" convert { v1 - v2 }
    }

    // negate <v>
    "negate" {
      val v by int().named("v")
      convert { -v }
    }
  }

  // Define sub commands in this builder
  // /command sub-command
  "sub-command" {
    // sub-command <values>
    val another by int(1..100).multiple().named("values")

    execute {
      println("/<$value> <$bool> sub-command <$another>")
    }
  }

  "test" or "alias" execute {

  }

  // /command ..
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
  val align by "align".option() with string()
      .named("axis")

  // anchored <anchor>
  val anchored by "anchored".option() with anchor
      .named("anchor")

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

  group.beforeArguments {
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
