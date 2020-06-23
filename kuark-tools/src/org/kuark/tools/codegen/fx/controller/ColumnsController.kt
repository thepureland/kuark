package org.kuark.tools.codegen.fx.controller

import javafx.application.Platform
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.CheckBoxTableCell
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell
import javafx.util.Callback
import javafx.util.StringConverter
import org.kuark.base.lang.string.StringKit
import org.kuark.config.kit.SpringKit
import org.kuark.tools.codegen.service.CodeGenColumnService
import org.kuark.tools.codegen.service.CodeGenObjectService
import org.kuark.tools.codegen.vo.ColumnInfo
import org.kuark.tools.codegen.vo.Config
import org.kuark.tools.fx.controls.AutoCompleteComboBoxListener
import org.kuark.tools.fx.controls.XTextFieldTableCell
import java.net.URL
import java.util.*

/**
 * Create by (admin) on 2015/5/26.
 */
class ColumnsController : Initializable {

    @FXML
    var tableComboBox: ComboBox<Any>? = null

    @FXML
    var tableCommentTextField: TextField? = null

    @FXML
    var columnTable: TableView<ColumnInfo>? = null

    private var config: Config? = null
    private var tableMap: Map<String, String?>? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initColumnTable()
    }

    private fun initTableComboBox() {
        //解决wizard的bug: 从page3回到page2会执行page1的onExitingPage方法
        val columnList = columns
        val table = table
        val tableComment = tableComment
        val items = tableComboBox!!.items
        tableMap = SpringKit.getBean(CodeGenObjectService::class).readTables(config!!.getDbSchema())
        tableComboBox!!.setItems(FXCollections.observableArrayList(tableMap!!.keys.toSortedSet()))
        AutoCompleteComboBoxListener<Any>(tableComboBox)
        if (items.isEmpty()) {
            tableComboBox!!.editor.textProperty()
                .addListener { observable: ObservableValue<out String?>?, oldValue: String?, newValue: String? ->
                    tableCommentTextField!!.clear()
                    if (newValue != null) {
                        if (tableMap!!.containsKey(newValue)) {
                            tableCommentTextField!!.text = tableMap!![newValue]
                            object : Thread() {
                                override fun run() {
                                    val columns = SpringKit.getBean(CodeGenColumnService::class)
                                        .readColumns(config!!.getDbSchema(), newValue)
                                    Platform.runLater { columnTable!!.setItems(FXCollections.observableArrayList(columns)) }
                                }
                            }.start()
                        }
                    }
                }
        }
        if (columnList.isNotEmpty()) {
            tableComboBox!!.selectionModel.select(table)
            tableCommentTextField!!.text = tableComment
            columnTable!!.items = FXCollections.observableArrayList(columnList)
        }
    }

    private fun initColumnTable() {
        bindColumns()
        bindEvents()
    }

    private fun bindColumns() {
        val sc = NumberStringConverter()
        var i = 0
        columnTable!!.getVisibleLeafColumn(i++).cellValueFactory =
            PropertyValueFactory<ColumnInfo, String>("name") as Callback<TableColumn.CellDataFeatures<ColumnInfo, out Any>, ObservableValue<out Any>>
        columnTable!!.getVisibleLeafColumn(i++).cellValueFactory =
            PropertyValueFactory<ColumnInfo, String>("origComment") as Callback<TableColumn.CellDataFeatures<ColumnInfo, out Any>, ObservableValue<out Any>>
        columnTable!!.getVisibleLeafColumn(i).cellFactory =
            Callback<TableColumn<ColumnInfo, Any>, TableCell<ColumnInfo?, Any?>> {
                XTextFieldTableCell<ColumnInfo?, Any?>(object : StringConverter<Any?>() {
                    override fun toString(`object`: Any?): String? {
                        return `object`?.toString()
                    }

                    override fun fromString(string: String): Any? {
                        return string
                    }
                })
            } as Callback<TableColumn<ColumnInfo, out Any>, TableCell<ColumnInfo, out Any>>
        columnTable!!.getVisibleLeafColumn(i++).cellValueFactory =
            PropertyValueFactory<ColumnInfo, String>("customComment") as Callback<TableColumn.CellDataFeatures<ColumnInfo, out Any>, ObservableValue<out Any>>
        columnTable!!.getVisibleLeafColumn(i).cellFactory =
            Callback<TableColumn<ColumnInfo?, Any?>, TableCell<ColumnInfo?, Any?>> { CheckBoxTableCell() } as Callback<TableColumn<ColumnInfo, out Any>, TableCell<ColumnInfo, out Any>>
        columnTable!!.getVisibleLeafColumn(i++).cellValueFactory =
            PropertyValueFactory<ColumnInfo, Boolean>("sortable") as Callback<TableColumn.CellDataFeatures<ColumnInfo, out Any>, ObservableValue<out Any>>
        columnTable!!.getVisibleLeafColumn(i).cellFactory =
            Callback<TableColumn<ColumnInfo?, Any?>, TableCell<ColumnInfo?, Any?>> { CheckBoxTableCell<ColumnInfo?, Any?>() } as Callback<TableColumn<ColumnInfo, out Any>, TableCell<ColumnInfo, out Any>>
        columnTable!!.getVisibleLeafColumn(i++).cellValueFactory =
            PropertyValueFactory<ColumnInfo, String>("searchable") as Callback<TableColumn.CellDataFeatures<ColumnInfo, out Any>, ObservableValue<out Any>>
        columnTable!!.getVisibleLeafColumn(i).cellFactory =
            Callback<TableColumn<ColumnInfo?, Any?>, TableCell<ColumnInfo?, Any?>> {
                TextFieldTableCell<ColumnInfo?, Any?>(sc as StringConverter<Any?>)
            } as Callback<TableColumn<ColumnInfo, out Any>, TableCell<ColumnInfo, out Any>>
        columnTable!!.getVisibleLeafColumn(i++).cellValueFactory =
            PropertyValueFactory<ColumnInfo, Int>("orderInList") as Callback<TableColumn.CellDataFeatures<ColumnInfo, out Any>, ObservableValue<out Any>>
        val strings = FXCollections.observableArrayList("", "升序", "降序")
        columnTable!!.getVisibleLeafColumn(i).cellFactory =
            Callback<TableColumn<ColumnInfo?, Any?>, TableCell<ColumnInfo?, Any?>> {
                ComboBoxTableCell<ColumnInfo?, Any?>(strings as ObservableList<*>)
            } as Callback<TableColumn<ColumnInfo, out Any>, TableCell<ColumnInfo, out Any>>
        columnTable!!.getVisibleLeafColumn(i++).cellValueFactory =
            PropertyValueFactory<ColumnInfo, String>("defaultOrder") as Callback<TableColumn.CellDataFeatures<ColumnInfo, out Any>, ObservableValue<out Any>>
        columnTable!!.getVisibleLeafColumn(i).cellFactory =
            Callback<TableColumn<ColumnInfo?, Any?>, TableCell<ColumnInfo?, Any?>> {
                TextFieldTableCell<ColumnInfo?, Any?>(sc as StringConverter<Any?>)
            } as Callback<TableColumn<ColumnInfo, out Any>, TableCell<ColumnInfo, out Any>>
        columnTable!!.getVisibleLeafColumn(i++).cellValueFactory =
            PropertyValueFactory<ColumnInfo, Int>("orderInEdit") as Callback<TableColumn.CellDataFeatures<ColumnInfo, out Any>, ObservableValue<out Any>>
        columnTable!!.getVisibleLeafColumn(i).cellFactory =
            Callback<TableColumn<ColumnInfo?, Any?>, TableCell<ColumnInfo?, Any?>> {
                TextFieldTableCell<ColumnInfo?, Any?>(sc as StringConverter<Any?>)
            } as Callback<TableColumn<ColumnInfo, out Any>, TableCell<ColumnInfo, out Any>>
        columnTable!!.getVisibleLeafColumn(i++).cellValueFactory =
            PropertyValueFactory<ColumnInfo, Int>("orderInView") as Callback<TableColumn.CellDataFeatures<ColumnInfo, out Any>, ObservableValue<out Any>>
    }

    private fun bindEvents() {}
    private inner class NumberStringConverter : StringConverter<Int?>() {
        override fun toString(`object`: Int?): String {
            return `object`?.toString() ?: ""
        }

        override fun fromString(string: String): Int? {
            return if (StringKit.isBlank(string) || !StringKit.isNumeric(string)) null else Integer.valueOf(string)
        }
    }

    fun setConfig(config: Config?) {
        this.config = config
        initTableComboBox()
    }

    fun getConfig(): Config? {
        return config
    }

    val table: String?
        get() = tableComboBox!!.selectionModel.selectedItem.toString()

    val tableComment: String
        get() = if (tableCommentTextField!!.text == null) "" else tableCommentTextField!!.text.trim { it <= ' ' }

    val columns: List<ColumnInfo>
        get() = columnTable!!.items
}