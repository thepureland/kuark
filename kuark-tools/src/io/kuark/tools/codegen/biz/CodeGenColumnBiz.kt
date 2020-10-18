package io.kuark.tools.codegen.biz


import io.kuark.ability.data.rdb.metadata.Column
import io.kuark.ability.data.rdb.metadata.RdbMetadataKit
import io.kuark.base.bean.BeanKit
import io.kuark.tools.codegen.core.CodeGeneratorContext
import io.kuark.tools.codegen.dao.CodeGenColumnDao
import io.kuark.tools.codegen.model.po.CodeGenColumn
import io.kuark.tools.codegen.model.vo.ColumnInfo

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
        val columnMap = CodeGenColumnDao.searchCodeGenColumnMap(table)

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
        CodeGenColumnDao.deleteCodeGenColumn(table)

        // insert new columns
        val codeGenColumns = mutableListOf<CodeGenColumn>()
        for (column in columnInfos) {
            codeGenColumns.add(
                CodeGenColumn {
                    name = column.getName()!!
                    objectName = table
                    comment = column.getCustomComment()!!
                    isSearchable = column.getSearchable()
                    isSortable = column.getSortable()
                    orderInEdit = column.getOrderInEdit()
                    orderInList = column.getOrderInList()
                    orderInView = column.getOrderInView()
                    defaultOrder = column.getDefaultOrder()!!
                }
            )
        }
        return CodeGenColumnDao.batchInsert(codeGenColumns) == codeGenColumns.size
    }

}