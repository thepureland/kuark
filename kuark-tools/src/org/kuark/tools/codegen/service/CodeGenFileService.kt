package org.kuark.tools.codegen.service

import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.removeIf
import me.liuwj.ktorm.entity.sequenceOf
import org.kuark.data.jdbc.support.RdbKit
import org.kuark.tools.codegen.core.CodeGeneratorContext
import org.kuark.tools.codegen.dao.CodeGenFiles

/**
 * 生成的文件历史信息服务
 *
 * @author K
 * @since 1.0.0
 */
object CodeGenFileService {

    fun read(): List<String> {
        val results = mutableListOf<String>()
        RdbKit.getDatabase().from(CodeGenFiles)
            .select(CodeGenFiles.filename)
            .where { CodeGenFiles.objectName eq CodeGeneratorContext.tableName }
            .forEach { results.add(it[CodeGenFiles.filename]!!) }
        return results
    }

    fun save(files: Collection<String>): Boolean {
        val table = CodeGeneratorContext.tableName
        RdbKit.getDatabase().sequenceOf(CodeGenFiles).removeIf { it.objectName eq table }

        return RdbKit.getDatabase().batchInsert(CodeGenFiles) {
            for (file in files) {
                item {
                    it.objectName to table
                    it.filename to file
                }
            }
        }.size == files.size
    }

}