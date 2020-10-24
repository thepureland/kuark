package io.kuark.service.sys.provider.dao

import io.kuark.ability.data.rdb.support.BaseDao
import io.kuark.service.sys.provider.model.po.SysDictItem
import io.kuark.service.sys.provider.model.table.SysDictItems
import org.ktorm.dsl.*
import org.springframework.stereotype.Repository

/**
 * 字典项数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class SysDictItemDao : BaseDao<String, SysDictItem, SysDictItems>() {
//endregion your codes 1

    //region your codes 2

    fun searchByIds(ids: List<String>): List<SysDictItem> {
        return querySource()
            .select(SysDictItems.columns)
            .orderBy(SysDictItems.seqNo.asc())
            .where { SysDictItems.dictId eq ids.first() }
            .map { row -> SysDictItems.createEntity(row) }
            .toList()
    }

    //endregion your codes 2

}