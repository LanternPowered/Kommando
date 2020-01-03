/*
 * Kommando
 *
 * Copyright (c) LanternPowered <https://www.lanternpowered.org>
 * Copyright (c) contributors
 *
 * This work is licensed under the terms of the MIT License (MIT). For
 * a copy, see 'LICENSE.txt' or <https://opensource.org/licenses/MIT>.
 */
package org.lanternpowered.kommando.tree

import org.lanternpowered.kommando.Command

/**
 * Represents a command that is constructed as a tree
 * structure. The tree structure is exposed so it can
 * be analyzed by other modules.
 *
 * @property rootElement The elements of the command tree
 */
interface CommandTree<S> : Command<S> {

  /**
   * The root tree element. The tree structure excludes
   * option and flag arguments.
   */
  val rootElement: TreeElement.Root<S>
}
