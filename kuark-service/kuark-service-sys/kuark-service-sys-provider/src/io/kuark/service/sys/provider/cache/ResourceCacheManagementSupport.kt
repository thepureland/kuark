package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheManagementSupport
import io.kuark.ability.cache.support.CacheNames
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.resource.SysResourceDetail
import io.kuark.service.sys.common.vo.resource.SysResourceSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysResourceBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class ResourceCacheManagementSupport: AbstractCacheManagementSupport<List<SysResourceDetail>>() {

    @Autowired
    private lateinit var sysResourceBiz: ISysResourceBiz

    override fun cacheName(): String = CacheNames.SYS_RESOURCE

    override fun doReload(key: String): List<SysResourceDetail> {
        require(key.contains(":")) { "缓存${cacheName()}的key格式必须是 子系统代码:资源类型代码" }
        val subSysAndResType = key.split(":")
        return sysResourceBiz.getResourcesFromCache(subSysAndResType[0], subSysAndResType[1])
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive()) {
            log.info("缓存未开启，不加载和缓存所有启用状态的资源！")
            return
        }

        // 加载所有可用的资源
        val searchPayload = SysResourceSearchPayload().apply {
            active = true
            returnEntityClass = SysResourceDetail::class
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val resources = sysResourceBiz.search(searchPayload) as List<SysResourceDetail>
        log.debug("从数据库加载了${resources.size}条资源信息。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存资源
        val resMap = resources.groupBy { "${it.subSysDictCode}:${it.resourceTypeDictCode}" }
        resMap.forEach { (key, value) ->
            CacheKit.putIfAbsent(cacheName(), key, value)
            log.debug("缓存了key为${key}的${value.size}条资源。")
        }
    }

}