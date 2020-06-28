package org.kuark.tools.codegen.fx.controller

import freemarker.template.Configuration
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.CheckBoxTableCell
import javafx.scene.control.cell.PropertyValueFactory
import javafx.util.Callback
import org.apache.commons.io.filefilter.IOFileFilter
import org.kuark.base.bean.BeanKit
import org.kuark.base.io.FileKit
import org.kuark.base.lang.string.StringKit
import org.kuark.base.scanner.classpath.ClassPathScanner
import org.kuark.config.kit.SpringKit
import org.kuark.data.jdbc.datasource.DataSourceKit
import org.kuark.data.jdbc.metadata.Column
import org.kuark.data.jdbc.metadata.RdbMetadataKit
import org.kuark.data.jdbc.metadata.RdbType
import org.kuark.data.jdbc.metadata.Table
import org.kuark.tools.codegen.core.GenerateConfig
import org.kuark.tools.codegen.core.Generator
import org.kuark.tools.codegen.core.FreemarkerKit
import org.kuark.tools.codegen.service.CodeGenColumnService
import org.kuark.tools.codegen.service.CodeGenFileService
import org.kuark.tools.codegen.service.CodeGenObjectService
import org.kuark.tools.codegen.vo.ColumnInfo
import org.kuark.tools.codegen.vo.Config
import org.kuark.tools.codegen.vo.GenFile
import java.io.File
import java.net.URL
import java.util.*
import java.util.function.Consumer

class FilesController : Initializable {

    @FXML
    var fileTable: TableView<GenFile>? = null

    private lateinit var tableName: String
    private var tableComment: String? = null
    private var columns: List<ColumnInfo>? = null
    private var config: Config? = null
    private lateinit var gConfig: GenerateConfig

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        bindColumns()
    }

    private fun bindColumns() {
        var i = 0
        fileTable!!.getVisibleLeafColumn(i).cellFactory = Callback { CheckBoxTableCell() }
        fileTable!!.getVisibleLeafColumn(i++).cellValueFactory =
            PropertyValueFactory<GenFile, Boolean>("generate") as Callback<TableColumn.CellDataFeatures<GenFile, out Any>, ObservableValue<out Any>>
        fileTable!!.getVisibleLeafColumn(i++).cellValueFactory =
            PropertyValueFactory<GenFile, String>("filename") as Callback<TableColumn.CellDataFeatures<GenFile, out Any>, ObservableValue<out Any>>
        fileTable!!.getVisibleLeafColumn(i++).cellValueFactory =
            PropertyValueFactory<GenFile, String>("directory") as Callback<TableColumn.CellDataFeatures<GenFile, out Any>, ObservableValue<out Any>>
    }

    fun readFiles() {
        if (config == null) {
            return
        }
        initGenerateConfig()
        val template = config!!.getTemplatePath()
        val resource = javaClass.getResource("/$templatePath")
        val templateDir = resource.file
        val fileFilter: IOFileFilter = object : IOFileFilter {
            override fun accept(file: File): Boolean {
                return "macro.include" != file.name
            }

            override fun accept(file: File, s: String): Boolean {
                return "macro.include" != s
            }
        }
        val codeGenFiles = SpringKit.getBean(CodeGenFileService::class).read(tableName)
        val files =
            if (templateDir.contains(".jar")) jarFiles
            else FileKit.listFiles(File(templateDir), fileFilter, fileFilter)
        val filePathModel: MutableMap<String, String?> = HashMap()
        filePathModel["basepackage_dir"] = gConfig.packageFolder
        filePathModel["module_dir"] = gConfig.module
        filePathModel["webModule_dir"] = gConfig.webModule
        filePathModel["className"] = StringKit.capitalize(StringKit.underscoreToHump(tableName))
        filePathModel["subModule_dir"] = gConfig.subModule
        filePathModel["topModule_dir"] = gConfig.topModule
        val codeLoaction = config!!.getCodeLoaction()
        val cfg = Configuration()
        val genFiles: MutableList<GenFile> = ArrayList(files.size)
        for (file in files) {
            val filename = FreemarkerKit.processTemplateString(file.name, filePathModel, cfg)
            var directory = FreemarkerKit.processTemplateString(file.parent, filePathModel, cfg)
            directory = directory.replaceFirst(templateDir.toRegex(), codeLoaction)
            val index = directory.indexOf(template) + template.length
            directory = if (filename.endsWith(".java")) {
                directory.substring(0, index) + directory.substring(index).replace("/".toRegex(), ".")
            } else {
                directory.substring(0, index) + directory.substring(index).replace("\\.".toRegex(), "/")
            }
            val check = if (codeGenFiles.isEmpty()) true else codeGenFiles.contains(filename)
            genFiles.add(GenFile(check, filename, directory))
        }
        genFiles.sort()
        fileTable!!.items = FXCollections.observableArrayList(genFiles)
    }

    /**
     * 获取jar内文件
     *
     * @return
     */
    private val jarFiles: Collection<File>
        private get() {
            val files: MutableCollection<File> = ArrayList()
            val resources = ClassPathScanner.scanForResources("$templatePath/", "", "")
            for (resource1 in resources) {
                if (StringKit.isNotBlank(resource1.filename) && !resource1.filename.contains("macro.include")) {
                    files.add(File(resource1.locationOnDisk))
                }
            }
            return files
        }

    @FXML
    fun generate(actionEvent: ActionEvent?) {
        if (StringKit.isNotBlank(tableName)) {
            val dataSource = DataSourceKit.createDataSource(
                RdbType.byProductName(config!!.getDbType()),
                config!!.getDbUrl(),
                config!!.getDbUser(),
                config!!.getDbPassword(),
                config!!.getDbCatalog(),
                config!!.getDbSchema()
            )

            val columnConfMap = columns!!.map { it.getColumn() to it }.toMap()
            val templateModel = mutableMapOf<String, Any>()
            dataSource.connection.use {
                templateModel["table"] = RdbMetadataKit.getTableByName(it, tableName)
                val columns = RdbMetadataKit.getColumnsByTableName(it, tableName).values
                templateModel["columns"] = columns
                for (column in columns) {
                    val columnName = column.name.toLowerCase()
                    val columnInfo = columnConfMap[columnName]
                    if (columnInfo != null) {
                        BeanKit.copyProperties(columnInfo, column)
                        column.comment = columnInfo.getComment()
                    }
                }

            }

            var filePathModel = mutableMapOf<String, Boolean>()
            fileTable!!.items.forEach(Consumer { item: GenFile ->
                filePathModel[item.getFilename()] = item.getGenerate()
            })

            try {
                Generator(gConfig, templateModel, filePathModel).generate()
                val persistence = persistence()
                if (persistence) {
                    Alert(
                        Alert.AlertType.INFORMATION, "生成成功，请查看目录：${config!!.getCodeLoaction()}".trimIndent()
                    ).show()
                } else {
                    Alert(Alert.AlertType.INFORMATION, "生成成功，但配置信息持久化失败! ").show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Alert(Alert.AlertType.ERROR, "生成失败！").show()
            }
        }
    }

    private fun initGenerateConfig() {
        gConfig = GenerateConfig().apply {
            packageFolder = config!!.getTemplatePath()
            templateRootDir = File(templatePath)
            outRootFolder = File(config!!.getCodeLoaction())
            isOverWrite = true
            val moduleName = config!!.getModuleName()
            module = moduleName
            var webModuleName = config!!.getWebModuleName()
            if (StringKit.isBlank(webModuleName)) {
                webModuleName = moduleName
            }
            webModule = webModuleName
            val i = webModuleName.indexOf(".")
            if (i != -1) {
                topModule = webModuleName.substring(0, i)
                subModule = webModuleName.substring(i + 1)
            } else {
                topModule = webModuleName
                subModule = webModuleName
            }
        }
    }

    private fun persistence(): Boolean {
        var success = SpringKit.getBean(CodeGenObjectService::class)
            .saveOrUpdate(tableName, tableComment, System.getProperty("user.name"))
        if (success) {
            success = SpringKit.getBean(CodeGenColumnService::class).saveColumns(tableName, columns!!)
            if (success) {
                val items = fileTable!!.items
                val filenames = items.filtered { it.getGenerate() }.map { it.getFilename() }
                success = SpringKit.getBean(CodeGenFileService::class).save(tableName, filenames)
            }
        }
        return success
    }

    fun setTable(table: String) {
        this.tableName = table
    }

    fun setTableComment(tableComment: String?) {
        this.tableComment = tableComment
    }

    fun setColumns(columns: List<ColumnInfo>) {
        this.columns = columns
    }

    fun setConfig(config: Config?) {
        this.config = config
    }

    fun selectAll(actionEvent: ActionEvent?) {
        select(true)
    }

    fun deselectAll(actionEvent: ActionEvent?) {
        select(false)
    }

    private fun select(select: Boolean) {
        val items = fileTable!!.items
        for (item in items) {
            item.setGenerate(select)
        }
    }

    companion object {
        const val templatePath = "template/business"
    }
}