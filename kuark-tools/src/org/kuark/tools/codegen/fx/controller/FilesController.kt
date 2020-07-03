package org.kuark.tools.codegen.fx.controller

import freemarker.template.Configuration
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.TableView
import org.apache.commons.io.filefilter.IOFileFilter
import org.kuark.base.io.FileKit
import org.kuark.base.io.FilenameKit
import org.kuark.base.scanner.classpath.ClassPathScanner
import org.kuark.tools.codegen.core.CodeGenerator
import org.kuark.tools.codegen.core.FreemarkerKit
import org.kuark.tools.codegen.core.TemplateModelCreator
import org.kuark.tools.codegen.service.CodeGenColumnService
import org.kuark.tools.codegen.service.CodeGenFileService
import org.kuark.tools.codegen.service.CodeGenObjectService
import org.kuark.tools.codegen.vo.ColumnInfo
import org.kuark.tools.codegen.vo.Config
import org.kuark.tools.codegen.vo.GenFile
import java.io.File
import java.net.URL
import java.util.*

class FilesController : Initializable {

    @FXML
    lateinit var fileTable: TableView<GenFile>

    private lateinit var tableName: String
    private var tableComment: String? = null
    private lateinit var columns: List<ColumnInfo>
    private lateinit var config: Config
    private lateinit var templateModel: Map<String, Any?>

    override fun initialize(location: URL?, resources: ResourceBundle?) {
    }

    fun readFiles() {
        val templateRootDir = config.getTemplateInfo()!!.rootDir
        val fileFilter: IOFileFilter = object : IOFileFilter {
            override fun accept(file: File): Boolean {
                return "macro.include" != file.name
            }

            override fun accept(file: File, s: String): Boolean {
                return "macro.include" != s
            }
        }
        val files =
            if (templateRootDir.contains(".jar")) jarFiles
            else FileKit.listFiles(File(templateRootDir), fileFilter, fileFilter)
        templateModel = TemplateModelCreator(config, tableName, columns).create()
        val cfg = Configuration()
        val genFiles = mutableListOf<GenFile>()
        val codeGenFiles = CodeGenFileService.read(tableName)
        for (file in files) {
            val filename = FreemarkerKit.processTemplateString(file.name, templateModel, cfg)
            var directory = FreemarkerKit.processTemplateString(file.parent, templateModel, cfg)
            directory = FilenameKit.normalize(directory, true)!!
            val finalFileRelativePath =
                directory.substring(templateRootDir.length + 1).replace('.', '/') + "/" + filename
            val templateFileRelativePath =
                FilenameKit.normalize(file.absolutePath, true).substring(templateRootDir.lastIndex + 2)
            genFiles.add(
                GenFile(
                    codeGenFiles.contains(filename), filename, directory,
                    finalFileRelativePath, templateFileRelativePath
                )
            )
        }
        genFiles.sort()
        fileTable.items = FXCollections.observableArrayList(genFiles)
    }

    /**
     * 获取jar内文件
     */
    private val jarFiles: Collection<File>
        private get() {
            val resources = ClassPathScanner.scanForResources(config.getTemplateInfo()!!.rootDir, "", "")
            val files = mutableListOf<File>()
            for (resource in resources) {
                if (resource.filename.isNotBlank() && !resource.filename.contains("macro.include")) {
                    files.add(File(resource.locationOnDisk))
                }
            }
            return files
        }

    @FXML
    fun generate(actionEvent: ActionEvent?) {
        try {
            CodeGenerator(config, templateModel, createFilePathModel()).generate()
            val persistence = persistence()
            if (persistence) {
                Alert(
                    Alert.AlertType.INFORMATION, "生成成功，请查看目录：${config.getCodeLoaction()}".trimIndent()
                ).show()
            } else {
                Alert(Alert.AlertType.INFORMATION, "生成成功，但配置信息持久化失败! ").show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Alert(Alert.AlertType.ERROR, "生成失败！").show()
        }
    }

    private fun createFilePathModel(): List<GenFile> {
        val genFiles = mutableListOf<GenFile>()
        fileTable.items.forEach {
            genFiles.add(it)
        }
        return genFiles
    }

    private fun persistence(): Boolean {
        var success = CodeGenObjectService.saveOrUpdate(tableName, tableComment, config.getAuthor())
        if (success) {
            success = CodeGenColumnService.saveColumns(tableName, columns)
            if (success) {
                val filenames = fileTable.items.filtered { it.getGenerate() }.map { it.getFilename() }
                success = CodeGenFileService.save(tableName, filenames)
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

    fun setConfig(config: Config) {
        this.config = config
    }

    @FXML
    private fun selectAll(actionEvent: ActionEvent?) = select(true)

    @FXML
    private fun deselectAll(actionEvent: ActionEvent?) = select(false)

    private fun select(select: Boolean) = fileTable.items.forEach { it.setGenerate(select) }

}