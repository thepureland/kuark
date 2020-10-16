package io.kuark.service.msg.provider.dao

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.service.provider.sys.model.po.SysDict
import io.kuark.service.provider.sys.model.table.SysDicts
import org.ktorm.dsl.*
import org.springframework.stereotype.Repository

/**
 * 字典主表数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
class SysDictDao: BaseDao<String, SysDict, SysDicts>() {
//endregion your codes 1

    //region your codes 2

    fun searchIdsByModuleAndType(module: String, type: String): List<String> {
        return querySource()
            .select(SysDicts.id)
            .whereWithConditions {
                it += (SysDicts.dictType eq type) and (SysDicts.isActive eq true)
                if (module.isNotEmpty()) {
                    it += SysDicts.module eq module
                }
            }
            .map { row -> row[SysDicts.id] }
            .toList() as List<String>
    }

    //endregion your codes 2

}