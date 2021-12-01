package io.kuark.demo.tools.codegen.fx.controller

import io.kuark.ability.ui.jfx.controls.AutoCompleteComboBoxListener
import io.kuark.demo.tools.codegen.biz.CodeGenColumnBiz
import io.kuark.demo.tools.codegen.biz.CodeGenObjectBiz
import io.kuark.demo.tools.codegen.core.CodeGeneratorContext
import io.kuark.demo.tools.codegen.model.vo.ColumnInfo
import io.kuark.demo.tools.codegen.model.vo.Config
import javafx.application.Platform
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ComboBox
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import java.net.URL
import java.util.*

/**
 * 数据库表的列信息界面JavaFx控制器
 *
 * @author K
 * @since 1.0.0
 */
class ColumnsController : Initializable {

    @FXML
    lateinit var tableComboBox: ComboBox<Any>

    @FXML
    lateinit var tableCommentTextField: TextField

    @FXML
    lateinit var columnTable: TableView<ColumnInfo>

    private lateinit var config: Config
    private var tableMap: Map<String, String?>? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        bindEvents()
    }

    private fun initTableComboBox() {
        //解决wizard的bug: 从page3回到page2会执行page1的onExitingPage方法
        val columnList = columns
        val table = table
        val tableComment = tableComment
        val items = tableComboBox.items
        tableMap = CodeGenObjectBiz.readTables()
        tableComboBox.setItems(FXCollections.observableArrayList(tableMap!!.keys.toSortedSet()))
        AutoCompleteComboBoxListener<Any>(tableComboBox)
        if (items.isEmpty()) {
            tableComboBox.editor.textProperty()
                .addListener { _: ObservableValue<out String?>?, _: String?, newValue: String? ->
                    tableCommentTextField.clear()
                    if (newValue != null) {
                        if (tableMap!!.containsKey(newValue)) {
                            tableCommentTextField.text = tableMap!![newValue]
                            CodeGeneratorContext.tableName = newValue
                            object : Thread() {
                                override fun run() {
                                    val columns = CodeGenColumnBiz.readColumns()
                                    Platform.runLater { columnTable.setItems(FXCollections.observableArrayList(columns)) }
                                }
                            }.start()
                        }
                    }
                }
        }
        if (columnList.isNotEmpty()) {
            tableComboBox.selectionModel.select(table)
            tableCommentTextField.text = tableComment
            columnTable.items = FXCollections.observableArrayList(columnList)
        }
    }

    private fun bindEvents() {}

    fun setConfig(config: Config) {
        this.config = config
        initTableComboBox()
    }

    fun getConfig(): Config {
        return config
    }

    val table: String?
        get() = tableComboBox.selectionModel.selectedItem?.toString()

    val tableComment: String
        get() = tableCommentTextField.text?.trim() ?: ""

    val columns: List<ColumnInfo>
        get() = columnTable.items

}