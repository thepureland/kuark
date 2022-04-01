package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheManagementSupport
import io.kuark.ability.cache.support.CacheNames
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.param.SysParamDetail
import io.kuark.service.sys.common.vo.param.SysParamSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysParamBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class ParamCacheManagementSupport: AbstractCacheManagementSupport<SysParamDetail>() {

    @Autowired
    private lateinit var sysParamBiz: ISysParamBiz

    override fun cacheName(): String = CacheNames.SYS_PARAM

    override fun doReload(key: String): SysParamDetail? {
        require(key.contains(":")) { "缓存${cacheName()}的key格式必须是 模块代码:参数名称" }
        val moduleAndParamName = key.split(":")
        return sysParamBiz.getParamFromCache(moduleAndParamName[0], moduleAndParamName[1])
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive()) {
            log.info("缓存未开启，不加载和缓存所有启用状态的参数！")
            return
        }

        // 加载所有可用的参数
        val searchPayload = SysParamSearchPayload().apply {
            active = true
            returnEntityClass = SysParamDetail::class
        }
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val params = sysParamBiz.search(searchPayload) as List<SysParamDetail>
        log.debug("从数据库加载了${params.size}条参数信息。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存参数
        params.forEach {
            val key = "${it.module}:${it.paramName}"
            CacheKit.putIfAbsent(cacheName(), key, it)
        }
        log.debug("缓存了${params.size}条参数信息。")
    }

}