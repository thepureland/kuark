package io.kuark.tools.codegen.biz

import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.tools.codegen.core.CodeGeneratorContext
import io.kuark.tools.codegen.model.table.CodeGenFiles
import org.ktorm.dsl.*

/**
 * 生成的文件历史信息服务
 *
 * @author K
 * @since 1.0.0
 */
object CodeGenFileBiz {

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
                    set(it.filename, file)
                    set(it.objectName, CodeGeneratorContext.tableName)
                }
            }
        }
        return true
    }

}