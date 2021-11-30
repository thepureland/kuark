/*
 * Copyright (c) 2014, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package io.kuark.ability.ui.jfx.controls.wizard

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.Control
import javafx.scene.control.SkinBase
import javafx.scene.layout.Pane

object ImplUtils {
    fun getChildren(n: Node?): List<Node?> {
        return if (n is Parent) getChildren(n as Parent?) else emptyList()
    }

    fun getChildren(p: Parent?): ObservableList<Node?> {
        var children: ObservableList<Node?>? = null

        // previously we used reflection immediately, now we try to avoid reflection
        // by checking the type of the Parent. Still not great...
        if (p is Pane) {
            // This should cover the majority of layout containers, including
            // AnchorPane, FlowPane, GridPane, HBox, Pane, StackPane, TilePane, VBox
            children = p.children
        } else if (p is Group) {
            children = p.children
        } else if (p is Control) {
            val s = p.skin
            children = if (s is SkinBase<*>) s.children else null
        }
        return children ?: FXCollections.emptyObservableList()
    }
}