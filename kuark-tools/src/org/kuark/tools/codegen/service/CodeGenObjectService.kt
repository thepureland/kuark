package org.kuark.tools.codegen.service

import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.entity.*
import org.kuark.data.jdbc.metadata.RdbMetadataKit
import org.kuark.data.jdbc.metadata.TableType
import org.kuark.data.jdbc.support.RdbKit
import org.kuark.tools.codegen.dao.CodeGenObjects
import java.time.LocalDateTime

object CodeGenObjectService {

    fun readTables(): Map<String, String?> {
        // from meta data
        val tables = RdbMetadataKit.getTablesByType(TableType.TABLE, TableType.VIEW)
        val nameAndComments = mutableMapOf<String, String?>()
        tables.forEach { nameAndComments[it.name] = it.comment }

        // from code_gen_object
        val codeGenObjects = RdbKit.getDatabase().sequenceOf(CodeGenObjects).toList()
        for (codeGenObject in codeGenObjects) {
            if (nameAndComments.contains(codeGenObject.name)) {
                nameAndComments[codeGenObject.name] = codeGenObject.comment
            }
        }
        return nameAndComments
    }

    fun saveOrUpdate(table: String, comment: String?, author: String?): Boolean {
        val codeGenObjects = RdbKit.getDatabase().sequenceOf(CodeGenObjects).filter { it.name eq table }
        return if (codeGenObjects.isEmpty()) {
            RdbKit.getDatabase().insert(CodeGenObjects) {
                it.name to table
                it.comment to comment
                it.createTime to LocalDateTime.now()
                it.createUser to author
                it.genCount to 1
            } == 1
        } else {
            val codeGenObject = codeGenObjects.first()
            with(codeGenObject) {
                this.comment = comment
                updateTime = LocalDateTime.now()
                updateUser = author
                genCount = codeGenObject.genCount + 1
            }
            codeGenObject.flushChanges() == 1
        }
    }

}