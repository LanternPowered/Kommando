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

import org.lanternpowered.kommando.argument.Argument
import org.lanternpowered.kommando.argument.ArgumentBuilder
import org.lanternpowered.kommando.argument.ArgumentParseContext
import org.lanternpowered.kommando.argument.BaseName
import org.lanternpowered.kommando.argument.ParseResult

class ArgumentBuilderImpl<T, S> : ArgumentBuilder<T, S> {

  fun build(): Argument<T, S> {
    TODO()
  }

  override fun parse(fn: ArgumentParseContext<S>.() -> ParseResult<T>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun suggest(fn: ArgumentParseContext<S>.() -> List<String>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun name(fn: BaseName.() -> String) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}
