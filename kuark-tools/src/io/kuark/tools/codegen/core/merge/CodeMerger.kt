package io.kuark.tools.codegen.core.merge

import io.kuark.base.io.FileKit
import java.io.File
import java.util.regex.Matcher

/**
 * 代码合并器，用于合并历史已生成的代码和当前生成的代码
 *
 * @author K
 * @since 1.0.0
 */
class CodeMerger(private val file: File) {

    private val oldFileContent: String
    private var newFileContent: String? = null
    private val retriever: CustomCodesRetriever

    /**
     * 代码生成后进行合并
     */
    fun merge() {
        handleRegion()
        handleImport()
        FileKit.write(file, newFileContent!!)
    }

    private fun handleRegion() {
        val customCodes = retriever.retrieve()
        newFileContent = FileKit.readFileToString(file)
        val appendCodesRetriever = AppendCodesRetriever(newFileContent)
        val appendCodesMap = appendCodesRetriever.retrieve()
        for ((index, value) in customCodes) {
            val codes = StringBuilder(value)
            val pair = appendCodesMap[index]
            if (pair != null) {
                val type = pair.first
                val appendCodes = pair.second
                if (type == AppendCodeType.PARTIBLE) {
                    val parts = appendCodes.split("\n".toRegex()).toTypedArray()
                    for (lineCode in parts) {
                        if (codes.indexOf(lineCode) == -1) {
                            codes.append(lineCode).append("\n")
                        }
                    }
                    codes.append("\n")
                } else {
                    if (codes.indexOf(appendCodes) == -1) {
                        codes.append(appendCodes).append("\n")
                    }
                }
            }
            val regexp =
                "(?<=(<!--)?#?//region your codes $index(-->)?\\r?\\n)[\\s\\S]*?(?=(<!--)?#?//endregion your codes $index(-->)?)"
            newFileContent = newFileContent!!.replaceFirst(regexp.toRegex(), Matcher.quoteReplacement(codes.toString()))
        }
    }

    private fun handleImport() {
        if (file.name.endsWith(".java")) {
            val oldImports = ImportStmtRetriever(oldFileContent).retrieveImports()
            val newImports = ImportStmtRetriever(newFileContent).retrieveImports()
            val commonImports = oldImports.intersect(newImports) // 交集
            val customImport = oldImports.subtract(commonImports) // 差集，用户自己导入的import
            val imports = StringBuilder()
            for (importStmt in customImport) {
                imports.append(importStmt).append("\n")
            }
            imports.append("\n")
            val index = newFileContent!!.indexOf("import")
            val sb = StringBuilder(newFileContent)
            sb.insert(index, imports)
            newFileContent = sb.toString()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val codes = "public void test() {" +
                    "<!--//region your codes 7-->" +
                    "\${root}" +
                    "<!--//endregion your codes 7-->" +
                    "}" +
                    "public void test() {" +
                    "<!--//region your codes 2-->" +
                    "<!--//endregion your codes 2-->" +
                    "}"
            val value = "\${codes}"
            val s1 = Matcher.quoteReplacement(value)
            val s = codes.replaceFirst(
                "(?<=(<!--)?//region your codes (\\d)(-->)?)[\\s\\S]*?(?=(<!--)?//endregion your codes \\d(-->)?)".toRegex(),
                """

     $s1

     """.trimIndent()
            )
            println(s)
        }
    }

    /**
     * 在代码生成前创建
     *
     * @param file
     */
    init {
        oldFileContent = FileKit.readFileToString(file)
        retriever = CustomCodesRetriever(oldFileContent)
    }
}