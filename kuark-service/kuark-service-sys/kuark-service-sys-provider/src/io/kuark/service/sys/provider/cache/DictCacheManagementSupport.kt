package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheManagementSupport
import io.kuark.base.support.Consts
import io.kuark.base.support.payload.ListSearchPayload
import io.kuark.service.sys.common.vo.dict.SysDictDetail
import io.kuark.service.sys.provider.biz.ibiz.ISysDictBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class DictCacheManagementSupport : AbstractCacheManagementSupport<SysDictDetail>() {

    @Autowired
    private lateinit var sysDictBiz: ISysDictBiz

    override fun cacheName(): String = SysCacheNames.SYS_DICT

    override fun doReload(key: String): SysDictDetail? {
        return sysDictBiz.getDictFromCache(key)
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive()) {
            log.info("缓存未开启，不加载和缓存所有字典(主表)信息！")
            return
        }

        // 加载所有字典
        val searchPayload = ListSearchPayload().apply {
            returnEntityClass = SysDictDetail::class
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val dicts = sysDictBiz.search(searchPayload) as List<SysDictDetail>
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

}