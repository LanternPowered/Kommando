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

import org.lanternpowered.kommando.argument.Argument

/**
 * Creates a new argument.
 */
fun <T, S> argument(
    @BuilderInference builder: ArgumentBuilder<T, S>.() -> Unit
): BuiltArgument<T, S> {
  TODO()
}

/**
 * The builder for arguments based on other arguments.
 */
@CommandDsl
interface ArgumentBaseBuilder<T, S> : SourceAwareBuilder<S>, ArgumentAwareBuilder<S>, PathAwareBuilder {

  infix fun String.to(other: Argument<out T, in S>)

  infix fun String.or(other: ArgumentBuilderWithPath): ArgumentBuilderWithPath

  infix fun String.then(other: ArgumentBuilderWithPath): ArgumentBuilderWithPath

  infix fun Path.to(other: Argument<out T, in S>)

  infix fun Path.or(other: ArgumentBuilderWithPath): ArgumentBuilderWithPath

  infix fun Path.then(other: ArgumentBuilderWithPath): ArgumentBuilderWithPath

  fun Path.beforeArguments(fn: ArgumentBuilder<T, S>.() -> Unit)

  fun String.beforeArguments(fn: ArgumentBuilder<T, S>.() -> Unit)

  /**
   * Builds and registers a sub-command.
   */
  operator fun String.invoke(fn: ArgumentBuilder<T, S>.() -> Unit): ArgumentBuilderWithPath

  operator fun Path.invoke(fn: ArgumentBuilder<T, S>.() -> Unit): ArgumentBuilderWithPath

  /**
   * Builds and registers a sub-command.
   */
  infix fun String.convert(fn: ValidationContext.() -> T) {
    this {
      convert(fn)
    }
  }

  /**
   * Builds and registers a sub-command.
   */
  infix fun Path.convert(fn: ValidationContext.() -> T) {
    this {
      convert(fn)
    }
  }

  /**
   * Apply changes to the builder within a group.
   */
  operator fun Group.invoke(fn: ArgumentBaseBuilder<T, S>.() -> Unit)

  /**
   * Represents a command builder that is bound to a path.
   */
  interface ArgumentBuilderWithPath
}

/**
 * The builder for arguments based on other arguments.
 */
@CommandDsl
interface ArgumentBuilder<T, S> : ArgumentBaseBuilder<T, S> {

  /**
   * Specifies the conversion function where multiple
   * argument values can be converted into a new result.
   */
  fun convert(fn: ValidationContext.() -> T)
}
