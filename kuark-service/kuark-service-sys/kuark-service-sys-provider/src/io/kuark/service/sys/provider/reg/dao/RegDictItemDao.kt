package io.kuark.service.sys.provider.reg.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import io.kuark.service.sys.provider.reg.model.po.RegDictItem
import io.kuark.service.sys.provider.reg.model.table.RegDictItems
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
open class RegDictItemDao : BaseCrudDao<String, RegDictItem, RegDictItems>() {
//endregion your codes 1

    //region your codes 2

    fun searchByDictId(dictId: String): List<RegDictItem> {
        return entitySequence().filter { RegDictItems.dictId eq dictId }.sortedBy { RegDictItems.seqNo }.toList()
//        return querySource()
//            .select(RegDictItems.columns)
//            .orderBy()
//            .where {  }
//            .map { row -> RegDictItems.createEntity(row) }
//            .toList()
    }

    //endregion your codes 2

}