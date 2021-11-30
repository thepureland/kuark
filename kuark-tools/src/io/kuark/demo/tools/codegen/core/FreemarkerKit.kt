package io.kuark.demo.tools.codegen.core

import freemarker.template.Configuration
import freemarker.template.Template
import java.io.*

/**
 * freemaker工具类
 *
 * @author K
 * @since 1.0.0
 */
object FreemarkerKit {

    fun getAvailableAutoInclude(conf: Configuration, autoIncludes: List<String>): List<String?> {
        val results = mutableListOf<String?>()
        autoIncludes.forEach {
            val t = Template("__auto_include_test__", StringReader("1"), conf)
            conf.setAutoIncludes(listOf(it))
            t.process(HashMap<Any?, Any?>(), StringWriter())
            results.add(it)
        }
        return results
    }

    fun processTemplate(template: Template, model: Map<String, *>?, outputFile: File?, encoding: String?) {
        BufferedWriter(OutputStreamWriter(FileOutputStream(outputFile), encoding)).use {
            template.process(model, it)
        }
    }

    fun processTemplateString(templateString: String?, model: Map<String, *>?, conf: Configuration?): String {
        StringWriter().use {
            val template = Template("templateString...", StringReader(templateString), conf)
            template.process(model, it)
            return it.toString()
        }
    }
}