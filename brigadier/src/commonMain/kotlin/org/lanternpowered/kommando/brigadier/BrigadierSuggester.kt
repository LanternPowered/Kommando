/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
@file:Suppress("FunctionName")

package org.lanternpowered.kommando.brigadier

/**
 * Constructs a new brigadier suggester.
 */
fun BrigadierSuggester(id: String): BrigadierSuggester = SimpleBrigadierSuggester(id)

private data class SimpleBrigadierSuggester(override val id: String) : BrigadierSuggester

/**
 * Represents a minecraft suggester.
 */
interface BrigadierSuggester {

  /**
   * The id of the suggester.
   */
  val id: String
}

val AskServerSuggester = BrigadierSuggester("minecraft:ask_server")
