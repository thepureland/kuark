package io.kuark.ability.sys.provider.reg.biz

import io.kuark.ability.cache.context.CacheNames
import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.data.rdb.biz.BaseBiz
import io.kuark.ability.sys.common.vo.reg.dict.RegDictItemRecord
import io.kuark.ability.sys.common.vo.reg.dict.RegDictPayload
import io.kuark.base.lang.string.StringKit
import io.kuark.ability.sys.provider.reg.dao.RegDictItemDao
import io.kuark.ability.sys.provider.reg.ibiz.IRegDictBiz
import io.kuark.ability.sys.provider.reg.ibiz.IRegDictItemBiz
import io.kuark.ability.sys.provider.reg.model.po.RegDictItem
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
@CacheConfig(cacheNames = [CacheNames.REG_DICT_ITEM])
open class RegDictItemBiz : BaseBiz<String, RegDictItem, RegDictItemDao>(), IRegDictItemBiz {
//endregion your codes 1

    @Autowired
    private lateinit var regDictBiz: IRegDictBiz

    //region yur codes 2

    @Cacheable(key = "#module.concat(':').concat(#type)", unless = "#result.isEmpty()")
    override fun getItemsByModuleAndType(module: String, type: String): List<RegDictItemRecord> {
        // 查出对应的dict id
        val dictId = regDictBiz.getDictIdByModuleAndType(module, type)

        return if (dictId == null) {
            listOf()
        } else {
            // 查出dict id的所有字典项
            val items = dao.searchByDictId(dictId)
            items.map {
                RegDictItemRecord(
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
    override fun saveOrUpdate(payload: RegDictPayload): String {
        return if (StringKit.isBlank(payload.id)) { // 新增
            val regDictItem = RegDictItem().apply {
                dictId = payload.dictId!!
                parentId = payload.parentId
                itemCode = payload.code!!
                itemName = payload.name!!
                seqNo = payload.seqNo
                remark = payload.remark
            }
            dao.insert(regDictItem)
        } else { // 更新
            val regDictItem = RegDictItem {
                id = payload.id
                dictId = payload.dictId!!
                parentId = payload.parentId
                itemCode = payload.code!!
                itemName = payload.name!!
                seqNo = payload.seqNo
                remark = payload.remark
            }
            dao.update(regDictItem)
            regDictItem.id!!
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
            val dictIds = dao.oneSearchProperty(RegDictItem::id.name, id, RegDictItem::dictId.name)
            val dict = regDictBiz.get(dictIds.first() as String)!!
            CacheKit.evict(CacheNames.REG_DICT_ITEM, "${dict.module}:${dict.dictType}") // 字典的缓存粒度为字典类型
        }
        return dao.deleteById(id)
    }

    private fun recursionFindAllParentId(itemId: String, results: MutableList<String>) {
        val list = dao.oneSearchProperty(RegDictItem::id.name, itemId, RegDictItem::parentId.name)
        if (list.isNotEmpty()) {
            val parentId = list.first() as String
            results.add(parentId)
            recursionFindAllParentId(parentId, results)
        }
    }

    private fun recursionFindAllChildId(itemId: String, results: MutableList<String>) {
        val itemIds = dao.oneSearchProperty(RegDictItem::parentId.name, itemId, RegDictItem::id.name)
        itemIds.forEach { id ->
            results.add(id as String)
            recursionFindAllChildId(id, results)
        }
    }


    //endregion your codes 2

}