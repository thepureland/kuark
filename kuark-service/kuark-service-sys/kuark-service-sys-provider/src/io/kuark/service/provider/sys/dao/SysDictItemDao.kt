package io.kuark.service.provider.sys.dao

import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.service.provider.sys.model.po.SysDictItem
import io.kuark.service.provider.sys.model.table.SysDictItems
import org.ktorm.dsl.*
import org.springframework.stereotype.Component

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@Component
class SysDictItemDao: BaseDao<String, SysDictItem, SysDictItems>() {

    fun searchByIds(ids: List<String>): List<SysDictItem> {
        return querySource()
            .select(SysDictItems.columns)
            .orderBy(SysDictItems.seqNo.asc())
            .where { SysDictItems.dictId eq ids.first() }
            .map { row -> SysDictItems.createEntity(row) }
            .toList()
    }

}