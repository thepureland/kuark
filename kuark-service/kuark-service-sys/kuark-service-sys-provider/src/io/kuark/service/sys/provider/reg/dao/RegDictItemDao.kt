package io.kuark.service.sys.provider.reg.dao

import io.kuark.ability.data.rdb.support.BaseDao
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
open class RegDictItemDao : BaseDao<String, io.kuark.service.sys.provider.reg.model.po.RegDictItem, io.kuark.service.sys.provider.reg.model.table.RegDictItems>() {
//endregion your codes 1

    //region your codes 2

    fun searchByDictId(dictId: String): List<io.kuark.service.sys.provider.reg.model.po.RegDictItem> {
        return entitySequence().filter { io.kuark.service.sys.provider.reg.model.table.RegDictItems.dictId eq dictId }.sortedBy { io.kuark.service.sys.provider.reg.model.table.RegDictItems.seqNo }.toList()
//        return querySource()
//            .select(RegDictItems.columns)
//            .orderBy()
//            .where {  }
//            .map { row -> RegDictItems.createEntity(row) }
//            .toList()
    }

    //endregion your codes 2

}