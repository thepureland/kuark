package org.kuark.tools.codegen.service

import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.removeIf
import me.liuwj.ktorm.entity.sequenceOf
import org.kuark.data.jdbc.support.RdbKit
import org.kuark.tools.codegen.dao.CodeGenFiles
import org.springframework.stereotype.Service

@Service
class CodeGenFileService {

    fun read(table: String): List<String> {
        val results = mutableListOf<String>()
        val select = RdbKit.getDatabase().from(CodeGenFiles)
            .select(CodeGenFiles.filename)
            .where { CodeGenFiles.objectName eq table }
            .forEach { results.add(it[CodeGenFiles.filename]!!) }
        return results
    }

    fun save(table: String, files: Collection<String>): Boolean {
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