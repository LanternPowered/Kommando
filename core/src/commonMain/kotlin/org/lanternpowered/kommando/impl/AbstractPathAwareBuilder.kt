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

import org.lanternpowered.kommando.Group
import org.lanternpowered.kommando.Path
import org.lanternpowered.kommando.PathAwareBuilder
import org.lanternpowered.kommando.Source
import org.lanternpowered.kommando.SourceAwareBuilder
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal interface ObjectWithPath<S> {
  val path: Path?
  val element: FoldedTreeElement<S>
}

internal class ImmutableObjectWithPath<S>(
    override val path: Path?,
    override val element: FoldedTreeElement<S>
) : ObjectWithPath<S>

internal abstract class AbstractPathAwareBuilder<S> : PathAwareBuilder, SourceAwareBuilder<S> {

  val children = mutableListOf<ObjectWithPath<S>>()

  override fun source() = SourceImpl<S>()

  override fun <O> Source<O>.provideDelegate(thisRef: Any?, prop: KProperty<*>): ReadOnlyProperty<Any?, O> {
    @Suppress("UNCHECKED_CAST")
    val property = SourceProperty(this as SourceImpl<S, O>)
    val objectWithPath = ImmutableObjectWithPath(null, FoldedSource(property))
    this@AbstractPathAwareBuilder.children += objectWithPath
    return property
  }

  override val String.path: Path
    get() = ValuePath(this)

  override fun String.or(other: String) = or(this.path, other.path)
  override fun String.or(other: Path) = or(this.path, other)

  override fun Path.or(other: String) = or(this, other.path)
  override fun Path.or(other: Path) = or(this, other)

  override fun String.then(other: String) = then(this.path, other.path)
  override fun String.then(other: Path) = then(this.path, other)

  override fun Path.then(other: String) = then(this, other.path)
  override fun Path.then(other: Path) = then(this, other)

  override val otherwise: Path
    get() = OtherwisePath

  override val group: Group
    get() = GroupAfterArguments

  override val Group.beforeArguments: Group
    get() = GroupBeforeArguments

  override val Path.beforeArguments: Path
    get() = beforeArguments(this)

  override val String.beforeArguments: Path
    get() = beforeArguments(this.path)
}
