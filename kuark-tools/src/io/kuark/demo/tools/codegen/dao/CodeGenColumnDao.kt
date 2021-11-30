package io.kuark.demo.tools.codegen.dao

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.tools.codegen.model.po.CodeGenColumn
import io.kuark.tools.codegen.model.table.CodeGenColumns
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.removeIf
import org.springframework.stereotype.Repository

/**
 * 代码生成-列信息数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
object CodeGenColumnDao: BaseDao<String, CodeGenColumn, CodeGenColumns>() {
//endregion your codes 1

    //region your codes 2

    fun searchCodeGenColumnMap(objectName: String): Map<String, CodeGenColumn> {
        val codeGenColumns = entitySequence().filter { it.objectName eq objectName }
        val columnMap = HashMap<String, CodeGenColumn>()
        for (column in codeGenColumns) {
            columnMap[column.name] = column
        }
        return columnMap
    }

    fun deleteCodeGenColumn(objectName: String) {
        entitySequence().removeIf { it.objectName eq objectName }
    }

    //endregion your codes 2

}