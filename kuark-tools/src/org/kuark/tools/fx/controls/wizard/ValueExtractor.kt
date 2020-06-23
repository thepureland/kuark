/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
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
package org.kuark.tools.fx.controls.wizard

import javafx.scene.Node
import javafx.scene.control.*
import javafx.util.Callback


object ValueExtractor {

    private val valueExtractors = mutableMapOf<Class<*>, Callback<Any, Any>>()

    private fun <T : Any> addValueExtractor(clazz: Class<T>, extractor: Callback<T, Any>) {
        valueExtractors[clazz] = extractor as Callback<Any, Any>
    }

    /**
     * Attempts to return a value for the given Node. This is done by checking
     * the map of value extractors, contained within this class. This
     * map contains value extractors for common UI controls, but more extractors
     * can be added by calling [.addValueExtractor].
     *
     * @param n The node from whom a value will hopefully be extracted.
     * @return The value of the given node.
     */
    fun getValue(n: Node): Any? {
        var value: Any? = null
        if (value == null && valueExtractors.containsKey(n.javaClass)) {
            val callback = valueExtractors[n.javaClass]!!
            value = callback.call(n)
        }
        return value
    }

    init {
        addValueExtractor(CheckBox::class.java, Callback<CheckBox, Any> { cb: CheckBox -> cb.isSelected })
        addValueExtractor(ChoiceBox::class.java, Callback<ChoiceBox<*>, Any> { cb: ChoiceBox<*> -> cb.value })
        addValueExtractor(ComboBox::class.java, Callback<ComboBox<*>, Any> { cb: ComboBox<*> -> cb.value })
        addValueExtractor(DatePicker::class.java, Callback<DatePicker, Any> { dp: DatePicker -> dp.value })
        addValueExtractor(PasswordField::class.java, Callback<PasswordField, Any> { pf: PasswordField -> pf.text })
        addValueExtractor(RadioButton::class.java, Callback<RadioButton, Any> { rb: RadioButton -> rb.isSelected })
        addValueExtractor(Slider::class.java, Callback<Slider, Any> { sl: Slider -> sl.value })
        addValueExtractor(TextArea::class.java, Callback<TextArea, Any> { ta: TextArea -> ta.text })
        addValueExtractor(TextField::class.java, Callback<TextField, Any> { tf: TextField -> tf.text })
        addValueExtractor(ListView::class.java, Callback<ListView<*>, Any> { lv: ListView<*> ->
            val sm = lv.selectionModel
            if (sm.selectionMode == SelectionMode.MULTIPLE) sm.selectedItems else sm.selectedItem
        })
        addValueExtractor(TreeView::class.java, Callback<TreeView<*>, Any> { tv: TreeView<*> ->
            val sm: MultipleSelectionModel<*> = tv.selectionModel
            if (sm.selectionMode == SelectionMode.MULTIPLE) sm.selectedItems else sm.selectedItem
        })
        addValueExtractor(TableView::class.java, Callback<TableView<*>, Any> { tv: TableView<*> ->
            val sm: MultipleSelectionModel<*> = tv.selectionModel
            if (sm.selectionMode == SelectionMode.MULTIPLE) sm.selectedItems else sm.selectedItem
        })
        addValueExtractor(TreeTableView::class.java, Callback<TreeTableView<*>, Any> { tv: TreeTableView<*> ->
            val sm: MultipleSelectionModel<*> = tv.selectionModel
            if (sm.selectionMode == SelectionMode.MULTIPLE) sm.selectedItems else sm.selectedItem
        })
    }
}
