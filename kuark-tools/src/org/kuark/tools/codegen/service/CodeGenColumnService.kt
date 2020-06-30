package org.kuark.tools.codegen.service


import me.liuwj.ktorm.dsl.batchInsert
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.removeIf
import me.liuwj.ktorm.entity.sequenceOf
import org.kuark.base.bean.BeanKit
import org.kuark.data.jdbc.metadata.Column
import org.kuark.data.jdbc.metadata.RdbMetadataKit
import org.kuark.data.jdbc.support.RdbKit
import org.kuark.tools.codegen.dao.CodeGenColumns
import org.kuark.tools.codegen.po.CodeGenColumn
import org.kuark.tools.codegen.vo.ColumnInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class CodeGenColumnService {

    fun readColumns(table: String): List<ColumnInfo> {
        // from meta data
        val columns: Map<String, Column> = RdbMetadataKit.getColumnsByTableName(table)

        // from code_gen_column table
        val codeGenColumns = RdbKit.getDatabase().sequenceOf(CodeGenColumns).filter { it.objectName eq table }
        val columnMap = HashMap<String, CodeGenColumn>(columns.size, 1f)
        for (column in codeGenColumns) {
            columnMap[column.name] = column
        }

        // merge
        val results = listOf<ColumnInfo>()
        for (column in columns.values) {
            val codeGenColumn = columnMap[column.name]
            val columnInfo = ColumnInfo()
            if (codeGenColumn != null) { // old column
                BeanKit.copyProperties(codeGenColumn, columnInfo)
            } else {
                with(columnInfo) {
                    setName(column.name)
                    setOrigComment(column.comment)
                }
            }
        }
        return results
    }

    @Transactional(rollbackFor = [Exception::class])
    open fun saveColumns(table: String, columnInfos: List<ColumnInfo>): Boolean {
        // delete old columns first
        RdbKit.getDatabase().sequenceOf(CodeGenColumns).removeIf { it.objectName eq table }

        // insert new columns
        val codeGenColumns = BeanKit.batchCopyProperties(CodeGenColumn::class, columnInfos)
        for (codeGenColumn in codeGenColumns) {
            codeGenColumn.objectName = table
        }
        return RdbKit.getDatabase().batchInsert(CodeGenColumns) {
            codeGenColumns.iterator()
        }.isNotEmpty()
    }

}