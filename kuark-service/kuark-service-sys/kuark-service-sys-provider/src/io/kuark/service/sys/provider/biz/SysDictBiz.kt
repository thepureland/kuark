package io.kuark.service.sys.provider.biz

import io.kuark.ability.data.rdb.biz.BaseBiz
import io.kuark.base.bean.BeanKit
import io.kuark.base.lang.string.StringKit
import io.kuark.base.query.sort.Order
import io.kuark.service.sys.common.model.dict.SysDictListRecord
import io.kuark.service.sys.common.model.dict.SysDictSearchPayload
import io.kuark.service.sys.common.model.dict.SysDictTreeNode
import io.kuark.service.sys.provider.dao.SysDictDao
import io.kuark.service.sys.provider.ibiz.ISysDictBiz
import io.kuark.service.sys.provider.model.po.SysDict
import io.kuark.service.sys.provider.model.table.SysDictItems
import io.kuark.service.sys.provider.model.table.SysDicts
import org.ktorm.dsl.asc
import org.ktorm.dsl.map
import org.ktorm.dsl.orderBy
import org.springframework.stereotype.Service

/**
 * 字典主表业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class SysDictBiz : BaseBiz<String, SysDict, SysDictDao>(), ISysDictBiz {
//endregion your codes 1

    //region your codes 2

    override fun pagingSearch(searchPayload: SysDictSearchPayload): Pair<List<SysDictListRecord>, Int> {
        val dictItems = dao.pagingSearch(searchPayload)
        val totalCount = dao.count(searchPayload)
        return Pair(dictItems, totalCount)
    }

    override fun loadDirectChildren(parentId: String?, activeOnly: Boolean): List<SysDictTreeNode> {
        return if (StringKit.isBlank(parentId)) { // 加载SysDict数据
            val results = dao.allSearch(Order.asc(SysDicts.dictType.name))
            results.map {
                BeanKit.copyProperties(
                    SysDictTreeNode::class, it, mapOf(
                        SysDict::dictType.name to SysDictTreeNode::code.name,
                        SysDict::dictName.name to SysDictTreeNode::name.name,
                    )
                )
            }
        } else { // 加载SysDictItem数据
            val searchPayload = SysDictSearchPayload().apply {
                this.parentId = parentId
                this.active = if (activeOnly) true else null
            }
            dao.leftJoinSearch(searchPayload)
                .orderBy(SysDictItems.seqNo.asc())
                .map { row ->
                    SysDictTreeNode().apply {
                        id = row[SysDictItems.id]
                        code = row[SysDictItems.itemCode]
                        name = row[SysDictItems.itemName]
                        seqNo = row[SysDictItems.seqNo]
                        module = row[SysDicts.module]
                    }
                }

        }
    }

    //endregion your codes 2

}