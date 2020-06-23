package org.kuark.tools.codegen.service


import me.liuwj.ktorm.dsl.batchInsert
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.filter
import me.liuwj.ktorm.entity.removeIf
import me.liuwj.ktorm.entity.sequenceOf
import org.kuark.base.bean.BeanKit
import org.kuark.data.jdbc.support.RdbKit
import org.kuark.tools.codegen.dao.CodeGenColumns
import org.kuark.tools.codegen.dao.MetaDataDao
import org.kuark.tools.codegen.po.CodeGenColumn
import org.kuark.tools.codegen.vo.ColumnInfo
import org.springframework.transaction.annotation.Transactional
import java.util.*

open class CodeGenColumnService {

    fun readColumns(schema: String?, table: String): List<ColumnInfo> {
        // from meta data
        val columns = MetaDataDao.getColumns(schema, table)

        // from code_gen_column table
        val codeGenColumns = RdbKit.getDatabase().sequenceOf(CodeGenColumns).filter { it.objectName eq table }
        val columnMap = HashMap<String, CodeGenColumn>(columns.size, 1f)
        for (column in codeGenColumns) {
            columnMap[column.name] = column
        }

        // merge
        for (columnInfo in columns) {
            val codeGenColumn = columnMap[columnInfo.getName()]
            if (codeGenColumn != null) { // old column
                BeanKit.copyProperties(codeGenColumn, columnInfo)
            }
        }
        return columns
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