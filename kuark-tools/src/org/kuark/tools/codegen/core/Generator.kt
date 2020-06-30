package org.kuark.tools.codegen.core

import freemarker.cache.MultiTemplateLoader
import freemarker.cache.URLTemplateLoader
import freemarker.template.Configuration
import freemarker.template.Template
import org.kuark.base.io.FileKit
import org.kuark.base.io.PathKit
import org.kuark.base.log.LogFactory
import org.kuark.base.scanner.classpath.ClassPathScanner
import org.kuark.tools.codegen.core.merge.CodeMerger
import org.kuark.tools.codegen.core.merge.PrivateContentEraser
import org.kuark.tools.codegen.fx.controller.FilesController
import java.io.File
import java.io.Writer
import java.net.URL

class Generator(
    private val config: GenerateConfig,
    private val templateModel: Map<String, Any>,
    private val filePathModel: Map<String, Boolean>
) {

    fun generate() {
        with(config) {
            check(outRootFolder == null) { "'outRootFolder' property must be not null." }
            check(templateRootDir == null) { "'templateRootDir' property must be not null." }
            log.debug("load template from templateRootDir = '${templateRootDir!!.absolutePath}' outRootDir:${outRootFolder!!.absolutePath}")
            val srcFiles =
                if (templateRootDir!!.path.contains(".jar")) getJarFiles()
                else FileKit.listFiles(templateRootDir!!, null, true)
            srcFiles.forEach { executeGenerate(templateRootDir!!, it) }
        }

    }

    private fun getJarFiles(): List<File> {
        val files = mutableListOf<File>()
        val resources = ClassPathScanner.scanForResources(FilesController.templatePath + "/", "", "")
        for (resource1 in resources) {
            if (resource1.filename.isNotBlank() && !resource1.filename.contains("macro.include")) {
                files.add(File(resource1.locationOnDisk))
            }
        }
        return files
    }

    private fun executeGenerate(tmplRootDir: File, templateFile: File) {
        val templateFile = PathKit.getRelativePath(tmplRootDir, templateFile)
        var outputFilepath = proceeForOutputFilepath(templateFile)
        if (!filePathModel[outputFilepath]!!) return
        processTemplateForGeneratorControl(templateFile)
        outputFilepath?.let { generateNewFileOrInsertIntoFile(templateFile, it) }
    }

    private fun processTemplateForGeneratorControl(templateFile: String) {
        val template = getFreeMarkerTemplate(templateFile)
        template.process(templateModel, object : Writer() {
            override fun close() {}
            override fun flush() {}
            override fun write(cbuf: CharArray, off: Int, len: Int) {}
        })
    }

    private fun getFreeMarkerTemplate(templateFile: String): Template {
        return newFreeMarkerConfiguration(templateFile).getTemplate(templateFile)
    }

    private fun proceeForOutputFilepath(templateFile: String): String? {
        var outputFilePath = templateFile

        //TODO 删除兼容性的@testExpression
        var testExpressionIndex = -1
        if (templateFile.indexOf('@').also { testExpressionIndex = it } != -1) {
            outputFilePath = templateFile.substring(0, testExpressionIndex)
            val testExpressionKey = templateFile.substring(testExpressionIndex + 1)
            val expressionValue = filePathModel[testExpressionKey]
            if (expressionValue == null) {
                System.err.println("[not-generate] WARN: test expression is null by key:[$testExpressionKey] on template:[$templateFile]")
                return null
            }
            if ("true" != expressionValue.toString()) {
                log.debug("[not-generate]\t test expression '@$testExpressionKey' is false,template:$templateFile")
                return null
            }
        }
        val conf = newFreeMarkerConfiguration("/filepath/processor/")
        return FreemarkerKit.processTemplateString(outputFilePath, filePathModel, conf)
    }

    private fun newFreeMarkerConfiguration(templateFile: String): Configuration {
        val multiTemplateLoader = MultiTemplateLoader(arrayOf(MyURLTemplateLoader(URL(config.templateRootDir!!.path))))
        val conf = Configuration()
        conf.templateLoader = multiTemplateLoader
        conf.numberFormat = "###############"
        conf.booleanFormat = "true,false"
        conf.defaultEncoding = config.sourceEncoding
        conf.setClassForTemplateLoading(Generator::class.java, getTemplateFilePath())

        val autoIncludes = getParentPaths(templateFile, "macro.include")
        val availableAutoInclude: List<String?> = FreemarkerKit.getAvailableAutoInclude(conf, autoIncludes)
        conf.setAutoIncludes(availableAutoInclude)
        log.debug("set Freemarker.autoIncludes:$availableAutoInclude for templateName:$templateFile autoIncludes:$autoIncludes")
        return conf
    }

    private fun getTemplateFilePath(): String? {
        val isWindow = System.getProperty("os.name").indexOf("Windows") > -1
        return ("/" + FilesController.templatePath + "/").replace("/", if (isWindow) "\\" else "/")
    }

    private fun getParentPaths(templateName: String, suffix: String): List<String> {
        val array = templateName.split("\\/")
        val list: MutableList<String> = ArrayList()
        list.add(suffix)
        list.add(File.separator + suffix)
        var path = ""
        for (i in array.indices) {
            path = path + File.separator + array[i]
            list.add(path + File.separator + suffix)
        }
        return list
    }

    private fun generateNewFileOrInsertIntoFile(templateFile: String, outputFilepath: String) {
        val template = getFreeMarkerTemplate(templateFile)
        template.outputEncoding = config.outputEncoding
        val absoluteOutputFilePath = File("$config.outRoot/$outputFilepath")
        FileKit.forceMkdir(absoluteOutputFilePath)
        val exists = absoluteOutputFilePath.exists()
        val existStr = if (exists) "Override " else ""
        log.debug("[" + existStr + "generate]\t template:$templateFile ==> $outputFilepath")
        var codeMerger: CodeMerger? = null
        if (exists) {
            codeMerger = CodeMerger(absoluteOutputFilePath)
        }
        FreemarkerKit.processTemplate(template, templateModel, absoluteOutputFilePath, config.outputEncoding)
        if (codeMerger != null) {
            codeMerger.merge()
        } else {
            PrivateContentEraser.erase(absoluteOutputFilePath)
        }
    }

    private class MyURLTemplateLoader(private val root: URL) : URLTemplateLoader() {
        override fun getURL(template: String): URL {
            return URL(root, template)
        }
    }

    companion object {
        private val log = LogFactory.getLog(Generator::class)
    }
}