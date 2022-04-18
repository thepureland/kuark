package io.kuark.service.sys.provider.biz.impl

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import io.kuark.service.sys.common.vo.dict.SysDictItemCacheItem
import io.kuark.service.sys.common.vo.dict.SysDictPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.cache.DictItemsByModuleAndTypeCacheHandler
import io.kuark.service.sys.provider.dao.SysDictItemDao
import io.kuark.service.sys.provider.model.po.SysDictItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 字典子表业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class SysDictItemBiz : BaseCrudBiz<String, SysDictItem, SysDictItemDao>(), ISysDictItemBiz {
//endregion your codes 1


    //region yur codes 2

    @Autowired
    private lateinit var dictItemCacheHandler: DictItemsByModuleAndTypeCacheHandler

    private val log = LogFactory.getLog(this::class)


    override fun getItemsFromCache(module: String, type: String): List<SysDictItemCacheItem> {
        return dictItemCacheHandler.getItemsFromCache(module, type)
    }

    override fun transDictCode(module: String, type: String, code: String): String? {
        val items = dictItemCacheHandler.getItemsFromCache(module, type)
        return items.firstOrNull { it.itemCode == code }?.itemName
    }

    @Transactional
    override fun saveOrUpdate(payload: SysDictPayload): String {
        return if (StringKit.isBlank(payload.id)) { // 新增
            val sysDictItem = SysDictItem().apply {
                dictId = payload.dictId!!
                parentId = payload.parentId
                itemCode = payload.code!!
                itemName = payload.name!!
                seqNo = payload.seqNo
                remark = payload.remark
            }
            val id = dao.insert(sysDictItem)
            dictItemCacheHandler.syncOnInsert(sysDictItem) // 同步缓存
            id
        } else { // 更新
            val sysDictItem = SysDictItem {
                id = payload.id
                dictId = payload.dictId!!
                parentId = payload.parentId
                itemCode = payload.code!!
                itemName = payload.name!!
                seqNo = payload.seqNo
                remark = payload.remark
            }
            val success = dao.update(sysDictItem)
            if (success) {
                dictItemCacheHandler.syncOnUpdate(sysDictItem) // 同步缓存
            } else {
                log.error("新增id为${sysDictItem.id}的字典项失败！")
            }
            sysDictItem.id!!
        }
    }

    override fun fetchAllParentIds(itemId: String): List<String> {
        val results = mutableListOf<String>()
        recursionFindAllParentId(itemId, results)
        results.reverse()
        return results
    }

    @Transactional
    override fun cascadeDeleteChildren(id: String): Boolean {
        val dictIds = dao.oneSearchProperty(SysDictItem::id.name, id, SysDictItem::dictId.name)
        val childItemIds = mutableListOf<String>()
        recursionFindAllChildId(id, childItemIds)
        if (childItemIds.isNotEmpty()) {
            dao.batchDelete(childItemIds)
        }
        val success = dao.deleteById(id)
        if (success) {
            dictItemCacheHandler.syncOnDelete(id, dictIds.first() as String) // 同步缓存
        } else {
            log.error("删除id为${id}的字典项失败！")
        }
        return success
    }

    @Transactional
    override fun updateActive(dictItemId: String, active: Boolean): Boolean {
        val dictItem = SysDictItem {
            this.id = dictItemId
            this.active = active
        }
        val success = dao.update(dictItem)
        if (success) {
            log.debug("更新id为${dictItemId}的字典项的启用状态为${active}。")
            dictItemCacheHandler.syncOnUpdateActive(dictItemId)
        } else {
            log.error("更新id为${dictItemId}的字典项的启用状态为${active}失败！")
        }
        return success
    }

    private fun recursionFindAllParentId(itemId: String, results: MutableList<String>) {
        val list = dao.oneSearchProperty(SysDictItem::id.name, itemId, SysDictItem::parentId.name)
        if (list.isNotEmpty()) {
            val parentId = list.first() as String
            results.add(parentId)
            recursionFindAllParentId(parentId, results)
        }
    }

    private fun recursionFindAllChildId(itemId: String, results: MutableList<String>) {
        val itemIds = dao.oneSearchProperty(SysDictItem::parentId.name, itemId, SysDictItem::id.name)
        itemIds.forEach { id ->
            results.add(id as String)
            recursionFindAllChildId(id, results)
        }
    }

    //endregion your codes 2

}