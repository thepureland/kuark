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
open class DictByIdCacheHandler : AbstractCacheHandler<SysDictCacheItem>() {

    @Autowired
    private lateinit var sysDictDao: SysDictDao

    companion object {
        private const val CACHE_NAME = "sys_dict_by_id"
        private val log = LogFactory.getLog(DictByIdCacheHandler::class)
    }

    override fun cacheName(): String = CACHE_NAME

    override fun doReload(key: String): SysDictCacheItem? {
        return getSelf().getDictFromCache(key)
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(CACHE_NAME)) {
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
            CacheKit.putIfAbsent(CACHE_NAME, it.id!!, it)
        }
        log.debug("缓存了${dicts.size}条字典(主表)信息。")
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#dictId",
        unless = "#result == null"
    )
    open fun getDictFromCache(dictId: String): SysDictCacheItem? {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
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
        if (CacheKit.isCacheActive(CACHE_NAME) && CacheKit.isWriteInTime(CACHE_NAME)) {
            log.debug("新增id为${id}的字典后，同步${CACHE_NAME}缓存...")
            getSelf().getDictFromCache(id)
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun syncOnUpdate(id: String) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("更新id为${id}的字典后，同步${CACHE_NAME}缓存...")
            CacheKit.evict(CACHE_NAME, id) // 踢除缓存
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                getSelf().getDictFromCache(id) // 缓存
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun syncOnDelete(id: String) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("删除id为${id}的字典后，同步${CACHE_NAME}缓存...")
            CacheKit.evict(CACHE_NAME, id) // 踢除缓存
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun getSelf(): DictByIdCacheHandler {
        return SpringKit.getBean(DictByIdCacheHandler::class)
    }

}