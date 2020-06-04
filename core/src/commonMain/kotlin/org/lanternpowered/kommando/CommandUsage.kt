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

/**
 * Represents the usage of a command.
 */
sealed class CommandUsage {

  /**
   * A literal value.
   */
  private data class Literal internal constructor(val value: String) : CommandUsage() {

    override fun toString(): String = value
  }

  /**
   * An argument.
   */
  private data class Argument internal constructor(val name: String) : CommandUsage() {

    private val toString = "<$name>"

    override fun toString(): String = toString
  }

  /**
   * An optional piece of usage.
   */
  private data class Optional internal constructor(val usage: CommandUsage) : CommandUsage() {

    private val toString = "[$usage]"

    override fun toString(): String = toString
  }

  /**
   * Usages that are joined together.
   */
  private data class Joined internal constructor(
      val usages: List<CommandUsage>,
      val separator: String = ""
  ) : CommandUsage() {

    private val toString = usages.joinToString(separator) { it.toString() }

    override fun toString(): String = toString
  }

  /**
   * Whether this usage is optional.
   */
  val isOptional: Boolean
    get() = this is Optional

  /**
   * Sets or resets the optional state of the usage.
   */
  fun optional(optional: Boolean): CommandUsage {
    if (optional)
      return if (this is Optional) this else Optional(this)
    return if (this is Optional) this.usage else this
  }

  /**
   * Creates a new [CommandUsage] where this and the given usage are appended.
   */
  operator fun plus(other: CommandUsage): CommandUsage {
    if (this is Joined && this.separator.isEmpty())
      return Joined(this.usages + other)
    return Joined(listOf(this, other))
  }

  /**
   * Creates a new [CommandUsage] where this and the given literal value are appended.
   */
  operator fun plus(literal: String): CommandUsage =
      plus(Literal(literal))

  companion object {

    private val empty = Literal("")

    /**
     * An empty command usage.
     */
    fun empty(): CommandUsage = empty

    /**
     * Constructs a literal command usage.
     */
    fun literal(literal: String): CommandUsage =
        if (literal.isEmpty()) empty else Literal(literal)

    /**
     * Constructs a argument command usage.
     */
    fun argument(name: String): CommandUsage =
        Argument(name)

    /**
     * Joins the command usages with the given separator.
     */
    fun join(vararg usages: CommandUsage, separator: String = ""): CommandUsage =
        join(usages.asList(), separator)

    /**
     * Joins the command usages with the given separator.
     */
    fun join(usages: Iterable<CommandUsage>, separator: String = ""): CommandUsage {
      val list = usages as? List<CommandUsage> ?: usages.toList()
      if (list.isEmpty())
        return empty
      if (list.size == 1)
        return list[0]
      if (usages.all { it.isOptional })
        return Optional(Joined(usages.map { it.optional(false) }, separator))
      return Joined(usages.toList(), separator)
    }
  }
}
