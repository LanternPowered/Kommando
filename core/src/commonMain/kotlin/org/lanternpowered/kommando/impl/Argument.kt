/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando.impl

import org.lanternpowered.kommando.ArgumentBaseBuilder
import org.lanternpowered.kommando.ArgumentBuilder
import org.lanternpowered.kommando.BuiltArgument
import org.lanternpowered.kommando.Group
import org.lanternpowered.kommando.NamedArgument
import org.lanternpowered.kommando.Path
import org.lanternpowered.kommando.ValidationContext
import org.lanternpowered.kommando.argument.Argument
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class ArgumentProperty<T>(
    val argument: Argument<T, *>,
    val name: String?
) : ValueProperty<T>()

internal class ArgumentBuilderImpl<T, S> : ArgumentBaseBuilderImpl<T, S>(), ArgumentBuilder<T, S>, BuiltArgument<T, S> {

  /**
   * The converter of the argument.
   */
  var converter: (ValidationContext.() -> T)? = null

  override fun convert(fn: ValidationContext.() -> T) {
    this.converter = fn
  }
}

// Tree
// -> Argument builder (arguments and/or literals)
// -> Argument
// -> Options (literal and arguments)
// -> Command builder (only commands)
// -> Flags (only commands)

internal open class ArgumentBaseBuilderImpl<T, S> :
    AbstractPathAwareBuilder<S>(), ArgumentBaseBuilder<T, S> {

  private class ArgumentBuilderWithPath<S>(
      override var path: Path,
      override val element: FoldedTreeElement<S>
  ) : ArgumentBaseBuilder.ArgumentBuilderWithPath, ObjectWithPath<S>

  override fun <A, B> A.to(b: B) = throw UnsupportedOperationException()

  override fun Path.invoke(
      fn: ArgumentBuilder<T, S>.() -> Unit
  ): ArgumentBaseBuilder.ArgumentBuilderWithPath {
    val builder = ArgumentBuilderImpl<T, S>()
    builder.fn()
    val objectWithPath = ArgumentBuilderWithPath(this,
        FoldedBuiltArgument(builder, null))
    children += objectWithPath
    return objectWithPath
  }

  override fun String.invoke(fn: ArgumentBuilder<T, S>.() -> Unit) = this.path.invoke(fn)

  override fun Path.or(
      other: ArgumentBaseBuilder.ArgumentBuilderWithPath
  ): ArgumentBaseBuilder.ArgumentBuilderWithPath {
    other as ArgumentBuilderWithPath<*>
    other.path = this.or(other.path)
    return other
  }

  override fun String.or(other: ArgumentBaseBuilder.ArgumentBuilderWithPath) = this.path.or(other)

  override fun Path.then(
      other: ArgumentBaseBuilder.ArgumentBuilderWithPath
  ): ArgumentBaseBuilder.ArgumentBuilderWithPath {
    other as ArgumentBuilderWithPath<*>
    other.path = this.then(other.path)
    return other
  }

  override fun String.then(other: ArgumentBaseBuilder.ArgumentBuilderWithPath) = this.path.then(other)

  override fun Path.to(other: Argument<out T, in S>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun String.to(other: Argument<out T, in S>) = this.path to other

  override fun Group.invoke(fn: ArgumentBaseBuilder<T, S>.() -> Unit) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun Path.beforeArguments(fn: ArgumentBuilder<T, S>.() -> Unit) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun String.beforeArguments(fn: ArgumentBuilder<T, S>.() -> Unit) = this.path.beforeArguments(fn)

  override fun <T> Argument<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> NamedArgument<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T> BuiltArgument<T, in S>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, T> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T, S> Argument<T, S>.named(name: String) = NamedArgumentImpl(name, this)
}
