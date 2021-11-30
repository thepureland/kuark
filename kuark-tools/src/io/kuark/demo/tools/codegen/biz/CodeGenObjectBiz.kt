package io.kuark.demo.tools.codegen.biz

import io.kuark.ability.data.rdb.metadata.RdbMetadataKit
import io.kuark.ability.data.rdb.metadata.TableType
import io.kuark.tools.codegen.core.CodeGeneratorContext
import io.kuark.tools.codegen.dao.CodeGenObjectDao
import io.kuark.tools.codegen.model.po.CodeGenObject
import java.time.LocalDateTime

/**
 * 生成的表对象历史信息服务
 *
 * @author K
 * @since 1.0.0
 */
object CodeGenObjectBiz {

    fun readTables(): Map<String, String?> {
        // from meta data
        val tables = RdbMetadataKit.getTablesByType(TableType.TABLE, TableType.VIEW)
        val nameAndComments = mutableMapOf<String, String?>()
        tables.forEach { nameAndComments[it.name] = it.comment }

        // from code_gen_object
        val codeGenObjects = CodeGenObjectDao.allSearch()
        for (codeGenObject in codeGenObjects) {
            if (nameAndComments.contains(codeGenObject.name)) {
                nameAndComments[codeGenObject.name] = codeGenObject.comment
            }
        }
        return nameAndComments
    }

    fun saveOrUpdate(): Boolean {
        val table = CodeGeneratorContext.tableName
        val comment = CodeGeneratorContext.tableComment
        val author = CodeGeneratorContext.config.getAuthor()
        val codeGenObject = CodeGenObjectDao.searchByName(table)
        return if (codeGenObject == null) {
            CodeGenObjectDao.insert(CodeGenObject {
                name = table
                this.comment = comment
                createTime = LocalDateTime.now()
                createUser = author
                genCount = 1
            })
            true
        } else {
            with(codeGenObject) {
                this.comment = comment
                updateTime = LocalDateTime.now()
                updateUser = author
                genCount = codeGenObject.genCount + 1
            }
            CodeGenObjectDao.update(codeGenObject)
        }
    }

}