package io.kuark.tools.codegen.fx.controller

import freemarker.template.Configuration
import io.kuark.base.io.FileKit
import io.kuark.base.io.FilenameKit
import io.kuark.base.scanner.classpath.ClassPathScanner
import io.kuark.tools.codegen.core.CodeGenerator
import io.kuark.tools.codegen.core.CodeGeneratorContext
import io.kuark.tools.codegen.core.FreemarkerKit
import io.kuark.tools.codegen.biz.CodeGenFileBiz
import io.kuark.tools.codegen.vo.GenFile
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.TableView
import org.apache.commons.io.filefilter.IOFileFilter
import java.io.File
import java.net.URL
import java.util.*

/**
 * 生成的文件选择界面JavaFx控制器
 *
 * @author K
 * @since 1.0.0
 */
class FilesController : Initializable {

    @FXML
    lateinit var fileTable: TableView<GenFile>

    private lateinit var templateModel: Map<String, Any?>

    override fun initialize(location: URL?, resources: ResourceBundle?) {
    }

    fun readFiles() {
        val templateRootDir = CodeGeneratorContext.config.getTemplateInfo()!!.rootDir
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
        templateModel = CodeGeneratorContext.templateModelCreator.create()
        val cfg = Configuration()
        val genFiles = mutableListOf<GenFile>()
        val codeGenFiles = CodeGenFileBiz.read()
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
            val resources =
                ClassPathScanner.scanForResources(CodeGeneratorContext.config.getTemplateInfo()!!.rootDir, "", "")
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
        val filePathModel = createFilePathModel()
        if (filePathModel.isEmpty()) {
            Alert(Alert.AlertType.ERROR, "未选择任何文件！").show()
            return
        }

        try {
            CodeGenerator(templateModel, filePathModel).generate()
        } catch (e: Exception) {
            e.printStackTrace()
            Alert(Alert.AlertType.ERROR, "生成失败！").show()
        }
    }

    @FXML
    fun generateAll(actionEvent: ActionEvent?) {
        selectAll(null)
        generate(null)
    }

    private fun createFilePathModel(): List<GenFile> {
        val genFiles = mutableListOf<GenFile>()
        fileTable.items.filter { it.getGenerate() }.forEach {
            genFiles.add(it)
        }
        return genFiles
    }

    @FXML
    private fun selectAll(actionEvent: ActionEvent?) = select(true)

    @FXML
    private fun deselectAll(actionEvent: ActionEvent?) = select(false)

    private fun select(select: Boolean) = fileTable.items.forEach { it.setGenerate(select) }

}