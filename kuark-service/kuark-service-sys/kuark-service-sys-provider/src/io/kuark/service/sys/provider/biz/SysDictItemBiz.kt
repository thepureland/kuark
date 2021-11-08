package io.kuark.service.sys.provider.biz

import io.kuark.ability.cache.context.CacheNames
import io.kuark.ability.data.rdb.biz.BaseBiz
import io.kuark.base.lang.string.StringKit
import io.kuark.service.sys.common.model.dict.SysDictPayload
import io.kuark.service.sys.provider.dao.SysDictItemDao
import io.kuark.service.sys.provider.ibiz.ISysDictBiz
import io.kuark.service.sys.provider.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.model.po.SysDictItem
import io.kuark.service.sys.provider.model.table.SysDictItems
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
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
open class SysDictItemBiz : BaseBiz<String, SysDictItem, SysDictItemDao>(), ISysDictItemBiz {
//endregion your codes 1

    @Autowired
    private lateinit var sysDictBiz: ISysDictBiz

    //region yur codes 2

    /**
     * 根据模块和字典类型，取得对应字典项(仅包括处于启用状态的)，并将结果缓存，查不到不缓存
     *
     * @param module 如果没有请传入空串，此时请保证type的惟一性，否则结果将不确定是哪条记录
     * @param type 字典类型
     * @return 字典项列表。如果module为空串，且存在多个同名type，将任意返回一个type对应的字典项。查无结果返回空列表。
     */
    @Cacheable(key = "#module.concat(':').concat(#type)", unless = "#result.isEmpty()")
    override fun getItemsByModuleAndType(module: String, type: String): List<SysDictItem> {
        // 查出对应的dict id
        val dictId = sysDictBiz.getDictIdByModuleAndType(module, type)

        return if (dictId == null) {
            listOf()
        } else {
            // 查出dict id的所有字典项
            dao.searchByDictId(dictId)
        }
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
    override fun cascadeDelete(id: String): Boolean {
        val childItemIds = mutableListOf<String>()
        recursionFindAllChildId(id, childItemIds)
        if (childItemIds.isNotEmpty()) {
            dao.batchDelete(childItemIds)
        }
        return dao.deleteById(id)
    }

    private fun recursionFindAllParentId(itemId: String, results: MutableList<String>) {
        val list = dao.oneSearchProperty(SysDictItems.id.name, itemId, SysDictItems.parentId.name)
        if (list.isNotEmpty()) {
            val parentId = list.first() as String
            results.add(parentId)
            recursionFindAllParentId(parentId, results)
        }
    }

    private fun recursionFindAllChildId(itemId: String, results: MutableList<String>) {
        val itemIds = dao.oneSearchProperty(SysDictItems.parentId.name, itemId, SysDictItems.id.name)
        itemIds.forEach { id ->
            results.add(id as String)
            recursionFindAllChildId(id, results)
        }
    }


    //endregion your codes 2

}