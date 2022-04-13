package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheManager
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
open class DictItemCacheManager : AbstractCacheManager<List<SysDictItemCacheItem>>() {

    @Autowired
    private lateinit var sysDictDao: SysDictDao

    @Autowired
    private lateinit var sysDictBiz: ISysDictBiz

    @Autowired
    private lateinit var sysDictItemDao: SysDictItemDao

    @Autowired
    private lateinit var self: DictItemCacheManager

    private val log = LogFactory.getLog(this::class)

    companion object {
        private const val SYS_DICT_ITEM_BY_MODULE_AND_DICT_TYPE = "sys_dict_item_by_module_and_dict_type"
    }

    override fun cacheName(): String = SYS_DICT_ITEM_BY_MODULE_AND_DICT_TYPE

    override fun doReload(key: String): List<SysDictItemCacheItem> {
        require(key.contains(":")) { "缓存${cacheName()}的key格式必须是 模块代码::字典类型代码" }
        val moduleAndDictType = key.split(":")
        return self.getItemsFromCache(moduleAndDictType[0], moduleAndDictType[1])
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive()) {
            log.info("缓存未开启，不加载和缓存所有启用状态的字典项！")
            return
        }

        // 加载所有可用的字典
        val payload = SysDictSearchPayload().apply {
            active = true
        }
        val results = sysDictDao.pagingSearch(payload)

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
            CacheKit.putIfAbsent(cacheName(), key, valueItems)
            log.debug("缓存字典${key}，共${valueItems.size}条字典项。")
        }
    }

    @Cacheable(
        cacheNames = [SYS_DICT_ITEM_BY_MODULE_AND_DICT_TYPE],
        key = "#module.concat('::').concat(#type)",
        unless = "#result == null || #result.isEmpty()"
    )
    open fun getItemsFromCache(module: String, type: String): List<SysDictItemCacheItem> {
        if (CacheKit.isCacheActive()) {
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

    fun syncOnInsert(sysDictItem: SysDictItem) {
        if (CacheKit.isCacheActive()) {
            log.debug("新增id为${sysDictItem.id}的字典项后，同步${cacheName()}缓存...")
            val dict = sysDictBiz.getDictFromCache(sysDictItem.dictId)!!
            CacheKit.evict(cacheName(), "${dict.module}::${dict.dictType}") // 踢除缓存
            if (CacheKit.isWriteInTime(cacheName())) {
                self.getItemsFromCache(dict.module!!, dict.dictType!!)
            }
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun syncOnUpdate(sysDictItem: SysDictItem) {
        if (CacheKit.isCacheActive()) {
            log.debug("更新id为${sysDictItem.id}的字典项后，同步${cacheName()}缓存...")
            val dict = sysDictBiz.getDictFromCache(sysDictItem.dictId)!!
            CacheKit.evict(cacheName(), "${dict.module}::${dict.dictType}") // 踢除缓存
            if (CacheKit.isWriteInTime(cacheName())) {
                self.getItemsFromCache(dict.module!!, dict.dictType!!)
            }
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun syncOnUpdateActive(dictItemId: String) {
        if (CacheKit.isCacheActive()) {
            log.debug("更新id为${dictItemId}的字典项的启用状态后，同步${cacheName()}缓存...")
            val dictIds = sysDictItemDao.oneSearchProperty(SysDictItem::id.name, dictItemId, SysDictItem::dictId.name)
            val dict = sysDictBiz.get(dictIds.first() as String)!!
            CacheKit.evict(cacheName(), "${dict.module}::${dict.dictType}") // 字典的缓存粒度为字典类型
            if (CacheKit.isWriteInTime(cacheName())) {
                self.getItemsFromCache(dict.module!!, dict.dictType) // 重新缓存
            }
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun syncOnDelete(id: String, dictId: String) {
        if (CacheKit.isCacheActive()) {
            log.debug("删除id为${id}的租户后，同步从${cacheName()}缓存中踢除...")
            val dict = sysDictBiz.get(dictId)!!
            CacheKit.evict(cacheName(), "${dict.module}::${dict.dictType}") // 字典的缓存粒度为字典类型
            if (CacheKit.isWriteInTime(cacheName())) {
                self.getItemsFromCache(dict.module!!, dict.dictType) // 重新缓存
            }
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

}