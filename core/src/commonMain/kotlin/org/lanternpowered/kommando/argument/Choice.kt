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

import kotlin.jvm.JvmName
import kotlin.reflect.KProperty

/**
 * Constructs a choices [Argument] based on the given values.
 */
fun choice(first: String, second: String, vararg more: String): Argument<String, Any>
    = choice((arrayOf(first, second) + more).toList())

/**
 * Constructs a choices [Argument] based on the given values.
 */
fun choice(values: List<String>): Argument<String, Any>
    = choice(values.associateWith { it })

/**
 * Constructs a choices [Argument] based on the given values.
 */
fun choice(values: () -> List<String>): Argument<String, Any>
    = choice(values) { it }

/**
 * Constructs a choices [Argument] based on the given values.
 */
fun <V> choice(first: Pair<String, V>, second: Pair<String, V>, vararg more: Pair<String, V>): Argument<V, Any>
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

private fun joinKeys(map: Map<String, *>) = map.keys.joinToString(", ")

/**
 * Constructs a choices [Argument] based on the given values.
 */
@JvmName("mapChoice")
fun <V> choice(values: () -> List<V>, toName: (value: V) -> String): Argument<V, Any> = argument {
  parse {
    val key = parseString()
    val map = values().associateBy(toName)
    val value = map[key]
    if (value != null) result(value) else error("Choice must be one of [${joinKeys(map)}], but found $key")
  }
  // TODO suggestions
}

/**
 * Constructs a choices [Argument] based on the given [values].
 */
fun <V> choice(values: Map<String, V>): Argument<V, Any> = argument {
  check(values.isNotEmpty()) { "The values may not be empty" }
  val immutableValues = values.toMap()
  val joinedKeys = joinKeys(immutableValues)
  parse {
    val key = parseString()
    val value = immutableValues[key]
    if (value != null) result(value) else error("Choice must be one of [$joinedKeys], but found $key")
  }
  // TODO suggestions
}

/**
 * A base classes for local choices objects in the builder.
 */
@Suppress("ClassName")
abstract class choices<T> : Argument<T, Any> {

  private val choices = mutableMapOf<String, T>()
  private val joinedKeys by lazy { joinKeys(this.choices) }

  protected fun register(name: String, value: T): T {
    this.choices[name.toLowerCase()] = value
    return value
  }

  /**
   * Gets a delegate to access the source.
   */
  @Suppress("NOTHING_TO_INLINE")
  protected inline operator fun T.provideDelegate(thisRef: Any?, prop: KProperty<*>): T {
    return register(prop.name, this)
  }

  /**
   * Gets a delegate to access the source.
   */
  @Suppress("NOTHING_TO_INLINE")
  protected inline operator fun Pair<String, T>.provideDelegate(thisRef: Any?, prop: KProperty<*>): T {
    return register(this.first, this.second)
  }

  @Suppress("NOTHING_TO_INLINE")
  protected inline operator fun T.getValue(thisRef: Any?, property: KProperty<*>) = this

  final override fun parse(context: ArgumentParseContext<Any>): ParseResult<T> {
    val key = context.parseString()
    val value = choices[key]
    return if (value != null) ParseResult(value) else error("Choice must be one of [$joinedKeys], but found $key")
  }

  final override fun suggest(context: ArgumentParseContext<Any>): List<String> = listOf() // TODO
}
