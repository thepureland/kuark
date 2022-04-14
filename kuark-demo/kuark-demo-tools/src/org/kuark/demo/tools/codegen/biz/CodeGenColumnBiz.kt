package org.kuark.demo.tools.codegen.biz


import io.kuark.ability.data.rdb.metadata.RdbMetadataKit
import io.kuark.base.bean.BeanKit
import org.kuark.demo.tools.codegen.core.CodeGeneratorContext
import org.kuark.demo.tools.codegen.dao.CodeGenColumnDao
import org.kuark.demo.tools.codegen.model.po.CodeGenColumn
import org.kuark.demo.tools.codegen.model.vo.ColumnInfo

/**
 * 生成的列信息服务
 *
 * @author K
 * @since 1.0.0
 */
object CodeGenColumnBiz {

    fun readColumns(): List<ColumnInfo> {
        val table = CodeGeneratorContext.tableName
        // from meta data
        val columns = RdbMetadataKit.getColumnsByTableName(table)

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
        if (columnMap.isEmpty()) { // 默认都为详情项
            results.map { it.setDetailItem(true) }
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
                    comment = column.getCustomComment()
                    searchItem = column.getSearchItem()
                    listItem = column.getListItem()
                    editItem = column.getEditItem()
                    detailItem = column.getDetailItem()
                }
            )
        }
        return CodeGenColumnDao.batchInsert(codeGenColumns) == codeGenColumns.size
    }

}