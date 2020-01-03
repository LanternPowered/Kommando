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

import org.lanternpowered.kommando.util.ToStringHelper
import kotlin.reflect.KProperty

/**
 * Constructs a choices [Argument] based on the given values.
 */
fun choice(first: String, second: String, vararg more: String): ChoicesArgument<String>
    = choice((arrayOf(first, second) + more).toList())

/**
 * Constructs a choices [Argument] based on the given values.
 */
fun choice(values: List<String>): ChoicesArgument<String>
    = choice(values.associateWith { it })

/**
 * Constructs a choices [Argument] based on the given values.
 */
fun choice(values: () -> List<String>): DynamicChoicesArgument<String>
    = choice(values) { it }

/**
 * Constructs a choices [Argument] based on the given values.
 */
fun <V> choice(first: Pair<String, V>, second: Pair<String, V>, vararg more: Pair<String, V>): ChoicesArgument<V>
    = choice((arrayOf(first, second) + more).toMap())

/* TODO: Bug in kotlin, where the proper function can't be selected based on the return type.
/**
 * Constructs a choices [Argument] based on the given values.
 */
@JvmName("mapChoice")
fun <V> choice(values: () -> Map<String, V>): Argument<String, Any> {
  TODO()
}
*/

/**
 * Constructs a choices [Argument] based on the given values.
 */
fun <V> choice(values: () -> List<V>, toName: (value: V) -> String) = DynamicChoicesArgument { values().associateBy(toName) }

/**
 * Constructs a choices [Argument] based on the given [values].
 */
fun <V> choice(values: Map<String, V>): ChoicesArgument<V> {
  check(values.isNotEmpty()) { "The values may not be empty" }
  return ConstantChoicesArgument(values.toMap())
}

/**
 * An argument that handles choices.
 */
abstract class ChoicesArgument<V> internal constructor() : Argument<V, Any> {

  /**
   * The choices that are available for the argument.
   */
  abstract val choices: Map<String, V>

  /**
   * Gets the joined keys.
   */
  protected open val joinedKeys
    get() = joinKeys(this.choices)

  final override fun parse(context: ArgumentParseContext<Any>): ParseResult<V> = context.run {
    val key = parseString()
    val map = choices
    val value = map[key] ?: error("Choice must be one of [$joinedKeys], but found $key")
    result(value)
  }

  final override fun suggest(context: ArgumentParseContext<Any>): List<String> = listOf() // TODO
}

/**
 * An argument that handles dynamic choices.
 */
class DynamicChoicesArgument<V> internal constructor(
    private val provider: () -> Map<String, V>
) : ChoicesArgument<V>() {

  override val choices: Map<String, V>
    get() = this.provider()
}

/**
 * A base classes for local choices objects in the builder.
 */
@Suppress("ClassName")
abstract class choices<T> : ChoicesArgument<T>() {

  override val choices: Map<String, T>
    get() = this.mutableChoices

  private val mutableChoices = mutableMapOf<String, T>()
  override val joinedKeys by lazy { joinKeys(this.mutableChoices) }

  /**
   * Registers the value and maps it to the name.
   */
  protected fun register(name: String, value: T): T {
    this.mutableChoices[name.toLowerCase()] = value
    return value
  }

  /**
   * Gets a delegate to access the source. The name of the
   * property will be used as name for the mapping.
   */
  @Suppress("NOTHING_TO_INLINE")
  protected inline operator fun T.provideDelegate(thisRef: Any?, prop: KProperty<*>): T {
    return register(prop.name, this)
  }

  /**
   * Gets a delegate to access the source.
   */
  @Suppress("NOTHING_TO_INLINE")
  protected inline operator fun NamedChoice<T>.provideDelegate(thisRef: Any?, prop: KProperty<*>): T {
    return register(this.name, this.value)
  }

  @Suppress("NOTHING_TO_INLINE")
  protected inline operator fun T.getValue(thisRef: Any?, property: KProperty<*>) = this

  /**
   * Uses the specified name for the given value.
   */
  infix fun T.named(name: String) = NamedChoice(name, this)

  /**
   * Represents a choice that has been named.
   */
  inner class NamedChoice<T>(val name: String, val value: T)

  final override fun toString(): String = ToStringHelper(this)
      .add("choices", "[" + this.joinedKeys + "]")
      .toString()
}

private fun joinKeys(map: Map<String, *>) = map.keys.joinToString(", ")

/**
 * A choices argument that uses constant choices, they will never be changed.
 */
private class ConstantChoicesArgument<V>(
    override val choices: Map<String, V>
) : ChoicesArgument<V>() {

  override val joinedKeys = joinKeys(this.choices)

  override fun toString(): String = ToStringHelper(this)
      .add("choices", "[" + this.joinedKeys + "]")
      .toString()
}
