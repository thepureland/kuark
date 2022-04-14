package io.kuark.service.sys.provider.biz.impl

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.bean.BeanKit
import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import io.kuark.base.query.sort.Order
import io.kuark.base.support.payload.ListSearchPayload
import io.kuark.service.sys.common.vo.dict.*
import io.kuark.service.sys.provider.biz.ibiz.ISysDictBiz
import io.kuark.service.sys.provider.biz.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.cache.DictCacheHandler
import io.kuark.service.sys.provider.dao.SysDictDao
import io.kuark.service.sys.provider.model.po.SysDict
import io.kuark.service.sys.provider.model.table.SysDictItems
import io.kuark.service.sys.provider.model.table.SysDicts
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
open class SysDictBiz : BaseCrudBiz<String, SysDict, SysDictDao>(), ISysDictBiz {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var sysDictItemBiz: ISysDictItemBiz

    @Autowired
    private lateinit var dictCacheHandler: DictCacheHandler

    private val log = LogFactory.getLog(this::class)

    override fun getDictFromCache(dictId: String): SysDictCacheItem? {
        return dictCacheHandler.getDictFromCache(dictId)
    }

    override fun getDictIdByModuleAndType(module: String, type: String): String? {
        return dao.getDictIdByModuleAndType(module, type)
    }

    override fun pagingSearch(listSearchPayload: ListSearchPayload): Pair<List<SysDictRecord>, Int> {
        val dictItems = dao.pagingSearch(listSearchPayload as SysDictSearchPayload)
        val totalCount = if (dictItems.isNotEmpty()) {
            // 查询parentCode
            val parentIds = dictItems.filter { StringKit.isNotBlank(it.parentId) }.map { it.parentId }.toSet()
            val returnProperties = listOf(SysDictItems.id.name, SysDictItems.itemCode.name)
            val idAndCodeMaps = sysDictItemBiz.inSearchProperties(SysDictItems.id.name, parentIds, returnProperties)
            dictItems.forEach { dictItem ->
                val idAndCodeMap = idAndCodeMaps.singleOrNull { it[SysDictItems.id.name] == dictItem.parentId }
                if (idAndCodeMap != null) {
                    dictItem.parentCode = idAndCodeMap[SysDictItems.itemCode.name] as String?
                }
            }
            dao.count(listSearchPayload)
        } else 0
        return Pair(dictItems, totalCount)
    }

    override fun loadDirectChildrenForTree(
        parent: String?,
        isModule: Boolean,
        activeOnly: Boolean
    ): List<SysDictTreeNode> {
        return when {
            StringKit.isBlank(parent) -> { // 加载模块列表
                val items = sysDictItemBiz.getItemsFromCache("kuark:sys", "module")
                items.map {
                    SysDictTreeNode().apply {
                        code = it.itemCode
                        id = code
                    }
                }
            }
            isModule -> { // 加载RegDict数据
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

    override fun loadDirectChildrenForList(searchPayload: SysDictSearchPayload): Pair<List<SysDictRecord>, Int> {
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

    override fun get(id: String, isDict: Boolean?, fetchAllParentIds: Boolean): SysDictRecord? {
        return if (isDict == true) {
            val dict = dao.get(id) ?: return null
            val sysDictRecord = SysDictRecord()
            BeanKit.copyProperties(dict, sysDictRecord)
            sysDictRecord
        } else {
            val searchPayload = SysDictSearchPayload().apply {
                this.id = id
                pageSize = 1
            }
            val result = dao.pagingSearch(searchPayload).firstOrNull()
            if (result != null && fetchAllParentIds) {
                val parentId = result.parentId
                if (StringKit.isNotBlank(parentId)) {
                    var parentIds = sysDictItemBiz.fetchAllParentIds(parentId!!)
                    parentIds = parentIds.toMutableList()
                    parentIds.add(parentId)
                    result.parentIds = parentIds
                }
            }
            result
        }
    }

    @Transactional
    override fun saveOrUpdate(payload: SysDictPayload): String {
        return if (StringKit.isBlank(payload.id)) { // 新增
            if (StringKit.isBlank(payload.parentId)) { // 添加RegDict
                val sysDict = SysDict().apply {
                    module = payload.module
                    dictType = payload.code!!
                    dictName = payload.name
                    remark = payload.remark
                }
                val id = dao.insert(sysDict)
                dictCacheHandler.syncOnInsert(id) // 同步缓存
                id
            } else { // 添加RegDictItem
                sysDictItemBiz.saveOrUpdate(payload)
            }
        } else { // 更新
            if (StringKit.isBlank(payload.parentId)) { // RegDict
                val sysDict = SysDict {
                    id = payload.id
                    module = payload.module
                    dictType = payload.code!!
                    dictName = payload.name
                    remark = payload.remark
                }
                val success = dao.update(sysDict)
                if (success) {
                    dictCacheHandler.syncOnUpdate(sysDict.id!!) // 同步缓存
                } else {
                    log.error("删除id为${sysDict.id}的字典失败！")
                }

            } else { // SysDictItem
                sysDictItemBiz.saveOrUpdate(payload)
            }
            payload.id!!
        }
    }

    @Transactional
    override fun delete(id: String, isDict: Boolean): Boolean {
        return if (isDict) {
            dao.batchDeleteWhen { column, _ ->
                if (column.name == SysDictItems.dictId.name) {
                    column.eq(id)
                } else null
            }
            val success = dao.deleteById(id)
            if (success) {
                dictCacheHandler.syncOnDelete(id) // 同步缓存
            } else {
                log.error("删除id为${id}的字典失败！")
            }
            success
        } else {
            sysDictItemBiz.cascadeDeleteChildren(id)
        }
    }


//endregion your codes 2

}