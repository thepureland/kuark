package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheManagementSupport
import io.kuark.ability.cache.support.CacheNames
import io.kuark.service.sys.common.vo.dict.SysDictItemRecord
import io.kuark.service.sys.common.vo.dict.SysDictSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysDictItemBiz
import io.kuark.service.sys.provider.dao.SysDictDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class DictItemCacheManagementSupport : AbstractCacheManagementSupport<List<SysDictItemRecord>>() {

    @Autowired
    private lateinit var sysDictDao: SysDictDao

    @Autowired
    private lateinit var sysDictItemBiz: ISysDictItemBiz

    override fun cacheName(): String = CacheNames.SYS_DICT_ITEM

    override fun doReload(key: String): List<SysDictItemRecord> {
        require(key.contains(":")) { "缓存${cacheName()}的key格式必须是 模块代码:字典类型代码" }
        val moduleAndDictType = key.split(":")
        return sysDictItemBiz.getItemsFromCache(moduleAndDictType[0], moduleAndDictType[1])
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
            "${it.module}:${it.dictType}"
        }
        dictMap.forEach { (key, value) ->
            CacheKit.putIfAbsent(cacheName(), key, value)
            log.debug("缓存字典${key}，共${value.size}条字典项。")
        }
    }

}