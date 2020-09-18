package io.kuark.tools.codegen.core

import freemarker.cache.MultiTemplateLoader
import freemarker.cache.URLTemplateLoader
import freemarker.template.Configuration
import io.kuark.base.io.FileKit
import io.kuark.base.log.LogFactory
import io.kuark.tools.codegen.core.merge.CodeMerger
import io.kuark.tools.codegen.core.merge.PrivateContentEraser
import io.kuark.tools.codegen.biz.CodeGenColumnBiz
import io.kuark.tools.codegen.biz.CodeGenFileBiz
import io.kuark.tools.codegen.biz.CodeGenObjectBiz
import io.kuark.tools.codegen.vo.GenFile
import javafx.scene.control.Alert
import java.io.File
import java.net.URL

/**
 * 代码生成器，代码生成核心逻辑处理
 *
 * @author K
 * @since 1.0.0
 */
class CodeGenerator(
    private val templateModel: Map<String, Any?>,
    private val genFiles: List<GenFile>
) {

    fun generate() {
        genFiles.forEach { executeGenerate(it) }
        val persistence = persistence()
        if (persistence) {
            Alert(
                Alert.AlertType.INFORMATION, "生成成功，请查看目录：${CodeGeneratorContext.config.getCodeLoaction()}".trimIndent()
            ).show()
        } else {
            Alert(Alert.AlertType.INFORMATION, "生成成功，但配置信息持久化失败! ").show()
        }
    }

    private fun persistence(): Boolean {
        var success = CodeGenObjectBiz.saveOrUpdate()
        if (success) {
            success = CodeGenColumnBiz.saveColumns()
            if (success) {
                val filenames = genFiles.filter { it.getGenerate() }.map { it.getFilename() }
                success = CodeGenFileBiz.save(filenames)
            }
        }
        return success
    }

    private fun newFreeMarkerConfiguration(): Configuration {
        val templateRootDir = CodeGeneratorContext.config.getTemplateInfo()!!.rootDir
        val root = URL("file:$templateRootDir")
        val multiTemplateLoader = MultiTemplateLoader(arrayOf(
            object : URLTemplateLoader() {
                override fun getURL(template: String): URL = URL(root, template)
            }
        ))
        val conf = Configuration()
        conf.templateLoader = multiTemplateLoader
        conf.numberFormat = "###############"
        conf.booleanFormat = "true,false"
        conf.defaultEncoding = "UTF-8"
        conf.setClassForTemplateLoading(
            CodeGenerator::class.java,
            "/template/${CodeGeneratorContext.config.getTemplateInfo()!!.name}"
        )
        val autoIncludes = listOf("macro.include")
        val availableAutoInclude =
            FreemarkerKit.getAvailableAutoInclude(conf, autoIncludes)
        conf.setAutoIncludes(availableAutoInclude)
        log.debug("set Freemarker.autoIncludes:$availableAutoInclude for templateName:$templateRootDir autoIncludes:$autoIncludes")
        return conf
    }

    private fun executeGenerate(genFile: GenFile) {
        val template = newFreeMarkerConfiguration().getTemplate(genFile.templateFileRelativePath)
        template.outputEncoding = "UTF-8"
        val absoluteOutputFilePath =
            File("${CodeGeneratorContext.config.getCodeLoaction()}/${genFile.finalFileRelativePath}")
        val exists = absoluteOutputFilePath.exists()
        val existStr = if (exists) "Override " else ""
        log.debug("[" + existStr + "generate]\t template:${genFile.getDirectory()}/${genFile.getFilename()} ==> ${genFile.finalFileRelativePath}")
        var codeMerger: CodeMerger? = null
        if (exists) {
            codeMerger = CodeMerger(absoluteOutputFilePath)
        } else {
            FileKit.touch(absoluteOutputFilePath)
        }
        FreemarkerKit.processTemplate(
            template,
            templateModel,
            absoluteOutputFilePath,
            "UTF-8"
        )
        if (codeMerger != null) {
            codeMerger.merge()
        } else {
            PrivateContentEraser.erase(absoluteOutputFilePath)
        }
    }

    companion object {
        private val log = LogFactory.getLog(CodeGenerator::class)
    }
}