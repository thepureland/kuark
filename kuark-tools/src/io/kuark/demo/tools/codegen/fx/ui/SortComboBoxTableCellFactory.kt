package io.kuark.demo.tools.codegen.fx.ui

import io.kuark.base.support.Consts
import javafx.collections.FXCollections
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.util.Callback

/**
 * 排序规则ComboBox表格单元格工厂
 *
 * @author K
 * @since 1.0.0
 */
class SortComboBoxTableCellFactory<S>: Callback<TableColumn<S, String>?, TableCell<S, String>?> {

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun call(param: TableColumn<S, String>?): TableCell<S, String> {
        val strings = FXCollections.observableArrayList("", "升序", "降序")
        return ComboBoxTableCell<S?, Any?>(*strings.toTypedArray()) as TableCell<S, String>
    }

}