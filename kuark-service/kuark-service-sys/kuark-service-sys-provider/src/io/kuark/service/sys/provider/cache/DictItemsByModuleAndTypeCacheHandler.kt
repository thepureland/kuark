package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.log.LogFactory
import io.kuark.service.sys.common.vo.dict.SysDictItemCacheItem
import io.kuark.service.sys.common.vo.dict.SysDictSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysDictBiz
import io.kuark.service.sys.provider.dao.SysDictDao
import io.kuark.service.sys.provider.dao.SysDictItemDao
import io.kuark.service.sys.provider.model.po.SysDictItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class DictItemsByModuleAndTypeCacheHandler : AbstractCacheHandler<List<SysDictItemCacheItem>>() {

    @Autowired
    private lateinit var sysDictDao: SysDictDao

    @Autowired
    private lateinit var sysDictBiz: ISysDictBiz

    @Autowired
    private lateinit var sysDictItemDao: SysDictItemDao
    
    @Autowired
    private lateinit var self: DictItemsByModuleAndTypeCacheHandler

    private val log = LogFactory.getLog(DictItemsByModuleAndTypeCacheHandler::class)

    companion object {
        private const val CACHE_NAME = "sys_dict_items_by_module_and_type"
    }

    override fun cacheName(): String = CACHE_NAME

    override fun doReload(key: String): List<SysDictItemCacheItem> {
        require(key.contains(":")) { "缓存${CACHE_NAME}的key格式必须是 模块代码::字典类型代码" }
        val moduleAndDictType = key.split(":")
        return self.getItemsFromCache(moduleAndDictType[0], moduleAndDictType[1])
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(CACHE_NAME)) {
            log.info("缓存未开启，不加载和缓存所有启用状态的字典项！")
            return
        }

        // 加载所有可用的字典
        val payload = SysDictSearchPayload().apply {
            active = true
        }
        val results = sysDictDao.pagingSearch(payload)

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存数据
        val dictMap = results.groupBy {
            "${it.module}::${it.dictType}"
        }
        dictMap.forEach { (key, value) ->
            val valueItems = value.map {
                SysDictItemCacheItem().apply {
                    itemCode = it.itemCode
                    itemName = it.itemName
                    parentId = it.parentId
                    seqNo = it.seqNo
                }
            }
            CacheKit.putIfAbsent(CACHE_NAME, key, valueItems)
            log.debug("缓存字典${key}，共${valueItems.size}条字典项。")
        }
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#module.concat('::').concat(#type)",
        unless = "#result == null || #result.isEmpty()"
    )
    open fun getItemsFromCache(module: String, type: String): List<SysDictItemCacheItem> {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("缓存中不存在模块为${module}且字典类型为${type}的字典项，从数据库中加载...")
        }
        // 查出对应的dict id
        val dictId = sysDictBiz.getDictIdByModuleAndType(module, type)

        return if (dictId == null) {
            log.warn("数据库中不存在模块为${module}且字典类型为${type}的字典项！")
            listOf()
        } else {
            // 查出dict id的所有字典项
            val items = sysDictItemDao.searchByDictId(dictId)
            log.debug("数据库中加载到模块为${module}且字典类型为${type}的字典项共${items.size}条.")
            items.map {
                SysDictItemCacheItem().apply {
                    itemCode = it.itemCode
                    itemName = it.itemName
                    parentId = it.parentId
                    seqNo = it.seqNo
                }
            }
        }
    }

    open fun syncOnInsert(sysDictItem: SysDictItem) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("新增id为${sysDictItem.id}的字典项后，同步${CACHE_NAME}缓存...")
            val dict = sysDictBiz.getDictFromCache(sysDictItem.dictId)!!
            CacheKit.evict(CACHE_NAME, "${dict.module}::${dict.dictType}") // 踢除缓存
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                self.getItemsFromCache(dict.module!!, dict.dictType!!)
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    open fun syncOnUpdate(sysDictItem: SysDictItem) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("更新id为${sysDictItem.id}的字典项后，同步${CACHE_NAME}缓存...")
            val dict = sysDictBiz.getDictFromCache(sysDictItem.dictId)!!
            CacheKit.evict(CACHE_NAME, "${dict.module}::${dict.dictType}") // 踢除缓存
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                self.getItemsFromCache(dict.module!!, dict.dictType!!)
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    open fun syncOnUpdateActive(dictItemId: String) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("更新id为${dictItemId}的字典项的启用状态后，同步${CACHE_NAME}缓存...")
            val dictIds = sysDictItemDao.oneSearchProperty(SysDictItem::id.name, dictItemId, SysDictItem::dictId.name)
            val dict = sysDictBiz.get(dictIds.first() as String)!!
            CacheKit.evict(CACHE_NAME, "${dict.module}::${dict.dictType}") // 字典的缓存粒度为字典类型
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                self.getItemsFromCache(dict.module!!, dict.dictType) // 重新缓存
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    open fun syncOnDelete(id: String, dictId: String) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("删除id为${id}的租户后，同步从${CACHE_NAME}缓存中踢除...")
            val dict = sysDictBiz.get(dictId)!!
            CacheKit.evict(CACHE_NAME, "${dict.module}::${dict.dictType}") // 字典的缓存粒度为字典类型
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                self.getItemsFromCache(dict.module!!, dict.dictType) // 重新缓存
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

}