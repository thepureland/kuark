package io.kuark.service.sys.provider.biz.impl

import io.kuark.ability.cache.context.CacheNames
import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.lang.string.StringKit
import io.kuark.service.sys.common.vo.dict.SysDictItemRecord
import io.kuark.service.sys.common.vo.dict.SysDictPayload
import io.kuark.service.sys.provider.dao.SysDictItemDao
import io.kuark.service.sys.provider.biz.ibiz.ISysDictBiz
import io.kuark.service.sys.provider.biz.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.model.po.SysDictItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
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
@CacheConfig(cacheNames = [CacheNames.SYS_DICT_ITEM])
open class SysDictItemBiz : BaseCrudBiz<String, SysDictItem, SysDictItemDao>(), ISysDictItemBiz {
//endregion your codes 1

    @Autowired
    private lateinit var sysDictBiz: ISysDictBiz

    //region yur codes 2

    @Cacheable(key = "#module.concat(':').concat(#type)", unless = "#result.isEmpty()")
    override fun getItemsByModuleAndType(module: String, type: String): List<SysDictItemRecord> {
        // 查出对应的dict id
        val dictId = sysDictBiz.getDictIdByModuleAndType(module, type)

        return if (dictId == null) {
            listOf()
        } else {
            // 查出dict id的所有字典项
            val items = dao.searchByDictId(dictId)
            items.map {
                SysDictItemRecord(
                    it.id!!,
                    it.itemCode,
                    it.itemName,
                    it.parentId,
                    it.seqNo
                )
            }
        }
    }

    override fun transDictCode(module: String, type: String, code: String): String? {
        val items = this.getItemsByModuleAndType(module, type)
        return items.firstOrNull { it.itemCode == code }?.itemName
    }

    @Transactional
    @CacheEvict(key = "#payload.module.concat(':').concat(#payload.dictType)")
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
            dao.insert(sysDictItem)
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
            dao.update(sysDictItem)
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
        val childItemIds = mutableListOf<String>()
        recursionFindAllChildId(id, childItemIds)
        if (childItemIds.isNotEmpty()) {
            dao.batchDelete(childItemIds)
        }
        if (CacheKit.isCacheActive()) {
            val dictIds = dao.oneSearchProperty(SysDictItem::id.name, id, SysDictItem::dictId.name)
            val dict = sysDictBiz.get(dictIds.first() as String)!!
            CacheKit.evict(CacheNames.SYS_DICT_ITEM, "${dict.module}:${dict.dictType}") // 字典的缓存粒度为字典类型
        }
        return dao.deleteById(id)
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