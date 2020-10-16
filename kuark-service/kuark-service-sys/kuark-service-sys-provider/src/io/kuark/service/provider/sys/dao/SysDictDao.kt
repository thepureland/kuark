package io.kuark.service.provider.sys.dao

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.service.provider.sys.model.po.SysDict
import io.kuark.service.provider.sys.model.table.SysDicts
import org.ktorm.dsl.*
import org.springframework.stereotype.Component

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@Component
class SysDictDao: BaseDao<String, SysDict, SysDicts>() {

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

}