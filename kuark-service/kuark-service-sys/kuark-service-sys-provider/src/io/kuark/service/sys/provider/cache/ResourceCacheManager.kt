package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheManager
import io.kuark.base.bean.BeanKit
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.common.vo.resource.SysResourceSearchPayload
import io.kuark.service.sys.provider.dao.SysResourceDao
import io.kuark.service.sys.provider.model.po.SysResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class ResourceCacheManager: AbstractCacheManager<List<SysResourceCacheItem>>() {

    @Autowired
    private lateinit var sysResourceDao: SysResourceDao

    @Autowired
    private lateinit var self:  ResourceCacheManager

    companion object {
        private const val SYS_RESOURCE_BY_SUB_SYS_AND_RES_TYPE = "sys_resource_by_sub_sys_and_res_type"
    }

    override fun cacheName(): String = SYS_RESOURCE_BY_SUB_SYS_AND_RES_TYPE

    override fun doReload(key: String): List<SysResourceCacheItem> {
        require(key.contains(":")) { "缓存${cacheName()}的key格式必须是 子系统代码::资源类型代码" }
        val subSysAndResType = key.split(":")
        return self.getResourcesFromCache(subSysAndResType[0], subSysAndResType[1])
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive()) {
            log.info("缓存未开启，不加载和缓存所有启用状态的资源！")
            return
        }

        // 加载所有可用的资源
        val searchPayload = SysResourceSearchPayload().apply {
            active = true
            returnEntityClass = SysResourceCacheItem::class
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val resources = sysResourceDao.search(searchPayload) as List<SysResourceCacheItem>
        log.debug("从数据库加载了${resources.size}条资源信息。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存资源
        val resMap = resources.groupBy { "${it.subSysDictCode}::${it.resourceTypeDictCode}" }
        resMap.forEach { (key, value) ->
            CacheKit.put(cacheName(), key, value)
            log.debug("缓存了key为${key}的${value.size}条资源。")
        }
    }

    @Cacheable(
        cacheNames = [SYS_RESOURCE_BY_SUB_SYS_AND_RES_TYPE],
        key = "#subSysDictCode.concat('::').concat(#resourceTypeDictCode)",
        unless = "#result == null || #result.isEmpty()"
    )
    open fun getResourcesFromCache(subSysDictCode: String, resourceTypeDictCode: String): List<SysResourceCacheItem> {
        if (CacheKit.isCacheActive()) {
            log.debug("缓存中不存在子系统为${subSysDictCode}且资源类型为${resourceTypeDictCode}的资源，从数据库中加载...")
        }
        require(subSysDictCode.isNotBlank()) { "获取资源时，子系统代码必须指定！" }
        require(resourceTypeDictCode.isNotBlank()) { "获取资源时，资源类型代码必须指定！" }
        val searchPayload = SysResourceSearchPayload().apply {
            returnEntityClass = SysResourceCacheItem::class
            active = true
            this.subSysDictCode = subSysDictCode
            this.resourceTypeDictCode = resourceTypeDictCode
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val results = sysResourceDao.search(searchPayload) as List<SysResourceCacheItem>
        log.debug("从数据库加载了${results.size}条的资源。")
        return results
    }

    fun syncOnInsert(any: Any, id: String) {
        if (CacheKit.isCacheActive() && CacheKit.isWriteInTime(cacheName())) {
            log.debug("新增id为${id}的资源后，同步${cacheName()}缓存...")
            val subSysDictCode = BeanKit.getProperty(any, SysResource::subSysDictCode.name) as String
            val resourceTypeDictCode = BeanKit.getProperty(any, SysResource::resourceTypeDictCode.name) as String
            self.getResourcesFromCache(subSysDictCode, resourceTypeDictCode) // 缓存
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun syncOnUpdate(any: Any, id: String) {
        if (CacheKit.isCacheActive()) {
            log.debug("更新id为${id}的资源后，同步${cacheName()}缓存...")
            val subSysDictCode = BeanKit.getProperty(any, SysResource::subSysDictCode.name) as String
            val resourceTypeDictCode = BeanKit.getProperty(any, SysResource::resourceTypeDictCode.name) as String
            val key = "${subSysDictCode}::${resourceTypeDictCode}"
            CacheKit.evict(cacheName(), key) // 踢除资源缓存
            if (CacheKit.isWriteInTime(cacheName())) {
                self.getResourcesFromCache(subSysDictCode, resourceTypeDictCode) // 重新缓存
            }
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun syncOnUpdateActive(id: String, active: Boolean) {
        if (CacheKit.isCacheActive()) {
            log.debug("更新id为${id}的资源的启用状态后，同步${cacheName()}缓存...")
            val sysRes = sysResourceDao.get(id)!!
            if (active) {
                if (CacheKit.isWriteInTime(cacheName())) {
                    self.getResourcesFromCache(sysRes.subSysDictCode, sysRes.resourceTypeDictCode) // 重新缓存
                }
            } else {
                val key = "${sysRes.subSysDictCode}::${sysRes.resourceTypeDictCode}"
                CacheKit.evict(cacheName(), key) // 踢除资源缓存
            }
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun syncOnDelete(id: String, subSysDictCode: String, resourceTypeDictCode: String) {
        if (CacheKit.isCacheActive()) {
            log.debug("删除id为${id}的资源后，同步从${cacheName()}缓存中踢除...")
            val key = "${subSysDictCode}::${resourceTypeDictCode}"
            CacheKit.evict(cacheName(), key) // 踢除缓存, 资源缓存的粒度到资源类型
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

}