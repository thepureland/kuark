package org.kuark.ui.jfx.controls.table.cell.factory

import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.CheckBoxTableCell
import javafx.util.Callback


class CheckBoxTableCellFactory<S, T> : Callback<TableColumn<S, T>?, TableCell<S, T>?> {

    override fun call(param: TableColumn<S, T>?): TableCell<S, T> {
        return CheckBoxTableCell<S, T>()
    }

}