package io.kuark.service.sys.provider.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.service.sys.provider.model.po.SysDictItem
import io.kuark.service.sys.provider.model.table.SysDictItems
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.sortedBy
import org.ktorm.entity.toList
import org.springframework.stereotype.Repository

/**
 * 字典项数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class SysDictItemDao : BaseCrudDao<String, SysDictItem, SysDictItems>() {
//endregion your codes 1

    //region your codes 2

    fun searchByDictId(dictId: String): List<SysDictItem> {
        return entitySequence().filter { SysDictItems.dictId eq dictId }.sortedBy { SysDictItems.seqNo }.toList()
//        return querySource()
//            .select(RegDictItems.columns)
//            .orderBy()
//            .where {  }
//            .map { row -> RegDictItems.createEntity(row) }
//            .toList()
    }

    //endregion your codes 2

}