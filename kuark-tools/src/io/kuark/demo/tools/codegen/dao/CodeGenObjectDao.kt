package io.kuark.demo.tools.codegen.dao

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.tools.codegen.model.po.CodeGenObject
import io.kuark.tools.codegen.model.table.CodeGenObjects
import org.ktorm.dsl.eq
import org.ktorm.entity.first
import org.springframework.stereotype.Repository

/**
 * 代码生成-对象信息数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
object CodeGenObjectDao: BaseDao<String, CodeGenObject, CodeGenObjects>() {
//endregion your codes 1

    //region your codes 2

    fun searchByName(name: String): CodeGenObject? {
        return try {
            entitySequence().first { it.name eq name }
        } catch (e: NoSuchElementException) {
            null
        }
    }

    //endregion your codes 2

}