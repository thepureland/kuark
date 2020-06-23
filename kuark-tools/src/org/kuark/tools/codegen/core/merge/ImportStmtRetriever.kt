package org.kuark.tools.codegen.core.merge

import java.util.*
import java.util.regex.Pattern

/**
 * Create by (admin) on 6/25/15.
 */
class ImportStmtRetriever(private val fileContent: String?) {
    fun retrieveImports(): List<String> {
        val imports: MutableList<String> = ArrayList()
        val p = Pattern.compile("import .+?;")
        val m = p.matcher(fileContent)
        while (m.find()) {
            imports.add(m.group(0))
        }
        return imports
    }

}