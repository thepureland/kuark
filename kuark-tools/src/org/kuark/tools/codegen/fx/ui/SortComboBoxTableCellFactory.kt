package org.kuark.tools.codegen.fx.ui

import javafx.collections.FXCollections
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.util.Callback

class SortComboBoxTableCellFactory<S>: Callback<TableColumn<S, String>?, TableCell<S, String>?> {

    override fun call(param: TableColumn<S, String>?): TableCell<S, String> {
        val strings = FXCollections.observableArrayList("", "升序", "降序")
        return ComboBoxTableCell<S?, Any?>(*strings.toTypedArray()) as TableCell<S, String>
    }

}