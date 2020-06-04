/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando.impl.util.collection

/**
 * Matches whether the contents of the iterables are equal. The
 * position within the iterables must also match.
 */
internal infix fun <E> Iterable<E>.contentEquals(that: Iterable<E>): Boolean {
  val thisList = this as? List<E> ?: this.toList()
  val thatList = that as? List<E> ?: that.toList()
  return thisList contentEquals thatList
}

/**
 * Matches whether the contents of the lists are equal. The
 * position within the lists must also match.
 */
internal infix fun <E> List<E>.contentEquals(that: List<E>): Boolean {
  if (this.size != that.size) return false
  for (i in this.indices) {
    if (this[i] != that[i])
      return false
  }
  return true
}

/**
 * Gets hashcode for the contents of the iterable.
 */
internal fun <E> Iterable<E>.contentHashCode(): Int {
  var result = 1
  for (element in this)
    result = 31 * result + (element?.hashCode() ?: 0)
  return result
}
