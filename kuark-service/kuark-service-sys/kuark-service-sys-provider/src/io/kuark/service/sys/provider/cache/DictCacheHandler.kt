package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.base.support.payload.ListSearchPayload
import io.kuark.context.kit.SpringKit
import io.kuark.service.sys.common.vo.dict.SysDictCacheItem
import io.kuark.service.sys.provider.dao.SysDictDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class DictCacheHandler : AbstractCacheHandler<SysDictCacheItem>() {

    @Autowired
    private lateinit var sysDictDao: SysDictDao

    companion object {
        private const val SYS_DICT_BY_ID = "sys_dict_by_id"
        private val log = LogFactory.getLog(DictCacheHandler::class)
    }

    override fun cacheName(): String = SYS_DICT_BY_ID

    override fun doReload(key: String): SysDictCacheItem? {
        return getSelf().getDictFromCache(key)
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(cacheName())) {
            log.info("缓存未开启，不加载和缓存所有字典(主表)信息！")
            return
        }

        // 加载所有字典
        val searchPayload = ListSearchPayload().apply {
            returnEntityClass = SysDictCacheItem::class
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val dicts = sysDictDao.search(searchPayload) as List<SysDictCacheItem>
        log.debug("从数据库加载了${dicts.size}条字典(主表)信息。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存字典(主表)信息
        dicts.forEach {
            CacheKit.putIfAbsent(cacheName(), it.id!!, it)
        }
        log.debug("缓存了${dicts.size}条字典(主表)信息。")
    }

    @Cacheable(
        cacheNames = [SYS_DICT_BY_ID],
        key = "#dictId",
        unless = "#result == null"
    )
    open fun getDictFromCache(dictId: String): SysDictCacheItem? {
        if (CacheKit.isCacheActive(cacheName())) {
            log.debug("缓存中不存在id为${dictId}的字典，从数据库中加载...")
        }
        val result = sysDictDao.get(dictId, SysDictCacheItem::class)
        if (result == null) {
            log.warn("数据库中不存在id为${dictId}的字典！")
        } else {
            log.debug("数据库加载到id为${dictId}的字典.")
        }
        return result
    }

    fun syncOnInsert(id: String) {
        if (CacheKit.isCacheActive(cacheName()) && CacheKit.isWriteInTime(cacheName())) {
            log.debug("新增id为${id}的字典后，同步${cacheName()}缓存...")
            getSelf().getDictFromCache(id)
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun syncOnUpdate(id: String) {
        if (CacheKit.isCacheActive(cacheName())) {
            log.debug("更新id为${id}的字典后，同步${cacheName()}缓存...")
            CacheKit.evict(cacheName(), id) // 踢除缓存
            if (CacheKit.isWriteInTime(cacheName())) {
                getSelf().getDictFromCache(id) // 缓存
            }
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun syncOnDelete(id: String) {
        if (CacheKit.isCacheActive(cacheName())) {
            log.debug("删除id为${id}的字典后，同步${cacheName()}缓存...")
            CacheKit.evict(cacheName(), id) // 踢除缓存
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun getSelf(): DictCacheHandler {
        return SpringKit.getBean(DictCacheHandler::class)
    }

}