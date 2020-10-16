package io.kuark.tools.codegen.biz


import io.kuark.ability.data.rdb.metadata.Column
import io.kuark.ability.data.rdb.metadata.RdbMetadataKit
import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.base.bean.BeanKit
import io.kuark.tools.codegen.core.CodeGeneratorContext
import io.kuark.tools.codegen.model.table.CodeGenColumns
import io.kuark.tools.codegen.model.po.CodeGenColumn
import io.kuark.tools.codegen.model.vo.ColumnInfo
import org.ktorm.dsl.batchInsert
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.removeIf
import org.ktorm.entity.sequenceOf
import java.util.*

/**
 * 生成的文件历史信息服务
 *
 * @author K
 * @since 1.0.0
 */
object CodeGenColumnBiz {

    fun readColumns(): List<ColumnInfo> {
        val table = CodeGeneratorContext.tableName
        // from meta data
        val columns: Map<String, Column> = RdbMetadataKit.getColumnsByTableName(table)

        // from code_gen_column table
        val codeGenColumns = RdbKit.getDatabase().sequenceOf(CodeGenColumns).filter { it.objectName eq table }
        val columnMap = HashMap<String, CodeGenColumn>(columns.size, 1f)
        for (column in codeGenColumns) {
            columnMap[column.name] = column
        }

        // merge
        val results = mutableListOf<ColumnInfo>()
        for (column in columns.values) {
            val codeGenColumn = columnMap[column.name]
            val columnInfo = ColumnInfo()
            results.add(columnInfo)
            if (codeGenColumn != null) { // old column
                BeanKit.copyProperties(codeGenColumn, columnInfo)
                columnInfo.setCustomComment(codeGenColumn.comment)
                columnInfo.setOrigComment(column.comment)
            } else {
                with(columnInfo) {
                    setName(column.name)
                    setOrigComment(column.comment)
                }
            }
        }
        return results
    }

    fun saveColumns(): Boolean {
        val table = CodeGeneratorContext.tableName
        val columnInfos = CodeGeneratorContext.columns

        // delete old columns first
        RdbKit.getDatabase().sequenceOf(CodeGenColumns).removeIf { it.objectName eq table }

        // insert new columns
        return RdbKit.getDatabase().batchInsert(CodeGenColumns) {
            for (column in columnInfos) {
                item {
                    set(it.name, column.getName())
                    set(it.objectName, table)
                    set(it.comment, column.getCustomComment())
                    set(it.isSearchable, column.getSearchable())
                    set(it.isSortable, column.getSortable())
                    set(it.orderInEdit, column.getOrderInEdit())
                    set(it.orderInList, column.getOrderInList())
                    set(it.orderInView, column.getOrderInView())
                    set(it.defaultOrder, column.getDefaultOrder())
                }
            }
        }.isNotEmpty()
    }



}