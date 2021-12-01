package io.kuark.service.sys.provider.reg.biz

import io.kuark.ability.data.rdb.biz.BaseBiz
import io.kuark.base.bean.BeanKit
import io.kuark.base.lang.string.StringKit
import io.kuark.base.query.sort.Order
import io.kuark.service.sys.common.vo.reg.dict.RegDictPayload
import io.kuark.service.sys.common.vo.reg.dict.RegDictRecord
import io.kuark.service.sys.common.vo.reg.dict.RegDictSearchPayload
import io.kuark.service.sys.common.vo.reg.dict.RegDictTreeNode
import io.kuark.service.sys.provider.reg.dao.RegDictDao
import io.kuark.service.sys.provider.reg.ibiz.IRegDictBiz
import io.kuark.service.sys.provider.reg.ibiz.IRegDictItemBiz
import io.kuark.service.sys.provider.reg.model.po.RegDict
import io.kuark.service.sys.provider.reg.model.table.RegDictItems
import io.kuark.service.sys.provider.reg.model.table.RegDicts
import org.ktorm.dsl.asc
import org.ktorm.dsl.eq
import org.ktorm.dsl.map
import org.ktorm.dsl.orderBy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 字典主表业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class RegDictBiz : BaseBiz<String, RegDict, RegDictDao>(), IRegDictBiz {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var regDictItemBiz: IRegDictItemBiz

    override fun getDictIdByModuleAndType(module: String, type: String): String? {
        return dao.getDictIdByModuleAndType(module, type)
    }

    override fun pagingSearch(searchPayload: RegDictSearchPayload): Pair<List<RegDictRecord>, Int> {
        val dictItems = dao.pagingSearch(searchPayload)
        val totalCount = if (dictItems.isNotEmpty()) {
            // 查询parentCode
            val parentIds = dictItems.filter { StringKit.isNotBlank(it.parentId) }.map { it.parentId }.toSet()
            val returnProperties = listOf(RegDictItems.id.name, RegDictItems.itemCode.name)
            val idAndCodeMaps = regDictItemBiz.inSearchProperties(RegDictItems.id.name, parentIds, returnProperties)
            dictItems.forEach { dictItem ->
                val idAndCodeMap = idAndCodeMaps.singleOrNull { it[RegDictItems.id.name] == dictItem.parentId }
                if (idAndCodeMap != null) {
                    dictItem.parentCode = idAndCodeMap[RegDictItems.itemCode.name] as String?
                }
            }
            dao.count(searchPayload)
        } else 0
        return Pair(dictItems, totalCount)
    }

    override fun loadDirectChildrenForTree(
        parent: String?,
        isModule: Boolean,
        activeOnly: Boolean
    ): List<RegDictTreeNode> {
        return when {
            StringKit.isBlank(parent) -> { // 加载模块列表
                val items = regDictItemBiz.getItemsByModuleAndType("kuark:sys", "module")
                items.map {
                    RegDictTreeNode().apply {
                        code = it.itemCode
                        id = code
                    }
                }
            }
            isModule -> { // 加载RegDict数据
                val results = dao.oneSearch(RegDicts.module.name, parent, Order.asc(RegDicts.dictType.name))
                results.map {
                    val treeNode = BeanKit.copyProperties(
                        RegDictTreeNode::class, it, mapOf(
                            RegDict::id.name to RegDictTreeNode::id.name,
                            RegDict::dictType.name to RegDictTreeNode::code.name,
                        )
                    )
                    treeNode
                }
            }
            else -> { // 加载RegDictItem数据
                val searchPayload = RegDictSearchPayload().apply {
                    this.parentId = parent
                    this.active = if (activeOnly) true else null
                }
                dao.leftJoinSearch(searchPayload)
                    .orderBy(RegDictItems.seqNo.asc())
                    .map { row ->
                        RegDictTreeNode().apply {
                            id = row[RegDictItems.id]
                            code = row[RegDictItems.itemCode]
                        }
                    }
            }
        }
    }

    override fun loadDirectChildrenForList(searchPayload: RegDictSearchPayload): Pair<List<RegDictRecord>, Int> {
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

    override fun get(id: String, isDict: Boolean?, fetchAllParentIds: Boolean): RegDictRecord? {
        return if (isDict == true) {
            val dict = dao.get(id) ?: return null
            RegDictRecord(
                dict.module, id, dict.dictType, dict.dictName, null, null, null, null, null, null, dict.remark
            )
        } else {
            val searchPayload = RegDictSearchPayload().apply {
                this.id = id
                pageSize = 1
            }
            val result = dao.pagingSearch(searchPayload).firstOrNull()
            if (result != null && fetchAllParentIds) {
                val parentId = result.parentId
                if (StringKit.isNotBlank(parentId)) {
                    var parentIds = regDictItemBiz.fetchAllParentIds(parentId!!)
                    parentIds = parentIds.toMutableList()
                    parentIds.add(parentId)
                    result.parentIds = parentIds
                }
            }
            result
        }
    }

    @Transactional
    override fun saveOrUpdate(payload: RegDictPayload): String {
        return if (StringKit.isBlank(payload.id)) { // 新增
            if (StringKit.isBlank(payload.parentId)) { // 添加RegDict
                val regDict = RegDict().apply {
                    module = payload.module
                    dictType = payload.code!!
                    dictName = payload.name
                    remark = payload.remark
                }
                dao.insert(regDict)
            } else { // 添加RegDictItem
                regDictItemBiz.saveOrUpdate(payload)
            }
        } else { // 更新
            if (StringKit.isBlank(payload.parentId)) { // RegDict
                val regDict = RegDict {
                    id = payload.id
                    module = payload.module
                    dictType = payload.code!!
                    dictName = payload.name
                    remark = payload.remark
                }
                dao.update(regDict)
            } else { // SysDictItem
                regDictItemBiz.saveOrUpdate(payload)
            }
            payload.id!!
        }
    }

    @Transactional
    override fun delete(id: String, isDict: Boolean): Boolean {
        return if (isDict) {
            regDictItemBiz.batchDeleteWhen { column, _ ->
                if (column.name == RegDictItems.dictId.name) {
                    column.eq(id)
                } else null
            }
            dao.deleteById(id)
        } else {
            regDictItemBiz.cascadeDeleteChildren(id)
        }
    }


//endregion your codes 2

}