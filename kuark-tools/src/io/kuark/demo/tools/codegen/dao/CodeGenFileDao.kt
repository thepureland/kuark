package io.kuark.demo.tools.codegen.dao

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.tools.codegen.model.po.CodeGenFile
import io.kuark.tools.codegen.model.table.CodeGenFiles
import org.ktorm.dsl.eq
import org.ktorm.dsl.forEach
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.springframework.stereotype.Repository

/**
 * 代码生成-文件信息数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
object CodeGenFileDao : BaseDao<String, CodeGenFile, CodeGenFiles>() {
//endregion your codes 1

    //region your codes 2

    fun searchCodeGenFileNames(objectName: String): List<String> {
        val results = mutableListOf<String>()
        querySource()
            .select(CodeGenFiles.filename)
            .where { CodeGenFiles.objectName eq objectName }
            .forEach { results.add(it[CodeGenFiles.filename]!!) }
        return results
    }

    //endregion your codes 2

}