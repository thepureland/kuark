package io.kuark.service.sys.provider.biz.impl

import io.kuark.ability.cache.context.CacheNames
import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import io.kuark.service.sys.common.vo.dict.SysDictItemRecord
import io.kuark.service.sys.common.vo.dict.SysDictPayload
import io.kuark.service.sys.common.vo.dict.SysDictSearchPayload
import io.kuark.service.sys.provider.dao.SysDictItemDao
import io.kuark.service.sys.provider.biz.ibiz.ISysDictBiz
import io.kuark.service.sys.provider.biz.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.dao.SysDictDao
import io.kuark.service.sys.provider.model.po.SysDictItem
import org.springframework.beans.factory.InitializingBean
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
open class SysDictItemBiz : BaseCrudBiz<String, SysDictItem, SysDictItemDao>(), ISysDictItemBiz, InitializingBean {
//endregion your codes 1


    //region yur codes 2

    @Autowired
    private lateinit var sysDictBiz: ISysDictBiz

    @Autowired
    private lateinit var sysDictDao: SysDictDao

    @Autowired
    private lateinit var self: ISysDictItemBiz // 由于缓存注解的底层实现为AOP，本类间方法必须通过Bean调用，否则缓存操作不生效

    private val log = LogFactory.getLog(this::class)

    override fun afterPropertiesSet() {
        cacheAllActiveItems()
    }

    /**
     * 缓存所有启用状态的字典项信息。
     * 如果缓存未开启，什么也不做。
     *
     * @author K
     * @since 1.0.0
     */
    protected fun cacheAllActiveItems() {
        if (!CacheKit.isCacheActive()) {
            log.info("缓存未开启，不加载和缓存所有启用状态的字典！")
            return
        }

        // 加载所有可用的字典
        val payload = SysDictSearchPayload().apply {
            active = true
        }
        val results = sysDictDao.pagingSearch(payload)
        val dictMap = results.groupBy {
            "${it.module}:${it.dictType}"
        }
        dictMap.forEach { (key, value) ->
            CacheKit.putIfAbsent(CacheNames.SYS_DICT_ITEM, key, value)
            log.debug("缓存字典${key}，共${value.size}条字典项。")
        }
    }

    @Cacheable(key = "#module.concat(':').concat(#type)", unless = "#result == null || #result.isEmpty()")
    override fun getItemsFromCache(module: String, type: String): List<SysDictItemRecord> {
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
        val items = self.getItemsFromCache(module, type)
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
            // 同步缓存
            if (CacheKit.isCacheActive()) {
                log.debug("新增id为${id}的字典项后，同步缓存...")
                val dict = sysDictBiz.getDictFromCache(sysDictItem.dictId)!!
                CacheKit.evict(CacheNames.SYS_DICT_ITEM, "${dict.module}:${dict.dictType}") // 踢除缓存
                self.getItemsFromCache(dict.module!!, dict.dictType!!)
                log.debug("缓存同步完成。")
            }
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
                // 同步缓存
                if (CacheKit.isCacheActive()) {
                    log.debug("更新id为${sysDictItem.id}的字典项后，同步缓存...")
                    val dict = sysDictBiz.getDictFromCache(sysDictItem.dictId)!!
                    CacheKit.evict(CacheNames.SYS_DICT_ITEM, "${dict.module}:${dict.dictType}") // 踢除缓存
                    self.getItemsFromCache(dict.module!!, dict.dictType!!)
                    log.debug("缓存同步完成。")
                }
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
        val childItemIds = mutableListOf<String>()
        recursionFindAllChildId(id, childItemIds)
        if (childItemIds.isNotEmpty()) {
            dao.batchDelete(childItemIds)
        }
        val success = dao.deleteById(id)
        if (success) {
            // 同步缓存
            if (CacheKit.isCacheActive()) {
                val dictIds = dao.oneSearchProperty(SysDictItem::id.name, id, SysDictItem::dictId.name)
                val dict = sysDictBiz.get(dictIds.first() as String)!!
                CacheKit.evict(CacheNames.SYS_DICT_ITEM, "${dict.module}:${dict.dictType}") // 字典的缓存粒度为字典类型
                self.getItemsFromCache(dict.module!!, dict.dictType) // 重新缓存
            }
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
            if (CacheKit.isCacheActive()) {
                log.debug("更新id为${dictItemId}的字典项的启用状态后，同步缓存...")
                val dictIds = dao.oneSearchProperty(SysDictItem::id.name, dictItemId, SysDictItem::dictId.name)
                val dict = sysDictBiz.get(dictIds.first() as String)!!
                CacheKit.evict(CacheNames.SYS_DICT_ITEM, "${dict.module}:${dict.dictType}") // 字典的缓存粒度为字典类型
                self.getItemsFromCache(dict.module!!, dict.dictType) // 重新缓存
                log.debug("缓存同步完成。")
            }
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