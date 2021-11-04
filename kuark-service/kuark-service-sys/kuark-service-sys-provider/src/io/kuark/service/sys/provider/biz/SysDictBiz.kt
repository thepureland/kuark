package io.kuark.service.sys.provider.biz

import io.kuark.ability.data.rdb.biz.BaseBiz
import io.kuark.base.bean.BeanKit
import io.kuark.base.lang.string.StringKit
import io.kuark.base.query.sort.Order
import io.kuark.service.sys.common.model.dict.SysDictListRecord
import io.kuark.service.sys.common.model.dict.SysDictSearchPayload
import io.kuark.service.sys.common.model.dict.SysDictTreeNode
import io.kuark.service.sys.provider.dao.SysDictDao
import io.kuark.service.sys.provider.dao.SysDictItemDao
import io.kuark.service.sys.provider.ibiz.ISysDictBiz
import io.kuark.service.sys.provider.model.po.SysDict
import io.kuark.service.sys.provider.model.table.SysDictItems
import io.kuark.service.sys.provider.model.table.SysDicts
import org.ktorm.dsl.asc
import org.ktorm.dsl.map
import org.ktorm.dsl.orderBy
import org.springframework.beans.factory.annotation.Autowired
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

    @Autowired
    private lateinit var sysDictItemDao: SysDictItemDao

    override fun pagingSearch(searchPayload: SysDictSearchPayload): Pair<List<SysDictListRecord>, Int> {
        val dictItems = dao.pagingSearch(searchPayload)
        val totalCount = dao.count(searchPayload)

        // 查询parentCode
        val parentIds = dictItems.filter { StringKit.isNotBlank(it.parentId) }.map { it.parentId }.toSet()
        val returnProperties = listOf(SysDictItems.id.name, SysDictItems.itemCode.name)
        val idAndCodeMaps = sysDictItemDao.inSearchProperties(SysDictItems.id.name, parentIds, returnProperties)
        dictItems.forEach { dictItem ->
            val idAndCodeMap = idAndCodeMaps.singleOrNull { it[SysDictItems.id.name] == dictItem.parentId }
            if (idAndCodeMap != null) {
                dictItem.parentCode = idAndCodeMap[SysDictItems.itemCode.name] as String
            }
        }

        return Pair(dictItems, totalCount)
    }

    override fun loadDirectChildrenForTree(
        parent: String?,
        isModule: Boolean,
        activeOnly: Boolean
    ): List<SysDictTreeNode> {
        return when {
            StringKit.isBlank(parent) -> { // 加载模块列表
                val modules = dao.allSearchProperty(SysDicts.module.name, Order.asc(SysDicts.module.name)).toSet() // distinct
                modules.map { SysDictTreeNode().apply {
                    code = it.toString()
                    id = code
                }}
            }
            isModule -> { // 加载SysDict数据
                val results = dao.oneSearch(SysDicts.module.name, parent, Order.asc(SysDicts.dictType.name))
                results.map {
                    val treeNode = BeanKit.copyProperties(
                        SysDictTreeNode::class, it, mapOf(
                            SysDict::id.name to SysDictTreeNode::id.name,
                            SysDict::dictType.name to SysDictTreeNode::code.name,
                        )
                    )
                    treeNode
                }
            }
            else -> { // 加载SysDictItem数据
                val searchPayload = SysDictSearchPayload().apply {
                    this.parentId = parent
                    this.active = if (activeOnly) true else null
                }
                dao.leftJoinSearch(searchPayload)
                    .orderBy(SysDictItems.seqNo.asc())
                    .map { row ->
                        SysDictTreeNode().apply {
                            id = row[SysDictItems.id]
                            code = row[SysDictItems.itemCode]
                        }
                    }
            }
        }
    }

    override fun loadDirectChildrenForList(searchPayload: SysDictSearchPayload): Pair<List<SysDictListRecord>, Int> {
        val activeOnly = searchPayload.active ?: false // 是否只加载启用状态的数据, 默认为是
        searchPayload.active = if (activeOnly) true else null
        val isModule = searchPayload.firstLevel ?: false // 是否parent代表模块名
        if (isModule) {
            searchPayload.module = searchPayload.parentId
            searchPayload.parentId = null
        }
        val records = dao.pagingSearch(searchPayload)
        val totalCount = dao.count(searchPayload)
        return Pair(records, totalCount)
    }

    override fun get(id: String, isDict: Boolean?): SysDictListRecord? {
        return if (isDict == true) {
            val dict = dao.getById(id) ?: return null
            SysDictListRecord(
                dict.module, id, dict.dictType, dict.dictName, null, null, null, null, null, null
            )
        } else {
            val searchPayload = SysDictSearchPayload().apply {
                this.id = id
                pageSize = 1
            }
            dao.pagingSearch(searchPayload).firstOrNull()
        }
    }

//endregion your codes 2

}