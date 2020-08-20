package io.kuark.tools.codegen.service

import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.isEmpty
import me.liuwj.ktorm.entity.removeIf
import me.liuwj.ktorm.entity.sequenceOf
import io.kuark.data.jdbc.support.RdbKit
import io.kuark.tools.codegen.core.CodeGeneratorContext
import io.kuark.tools.codegen.dao.CodeGenFiles

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
        val filesInDb = read()
        RdbKit.getDatabase().batchInsert(CodeGenFiles) {
            files.filter { !filesInDb.contains(it) }.forEach { file ->
                item {
                    it.filename to file
                    it.objectName to CodeGeneratorContext.tableName
                }
            }
        }
        return true
    }

}