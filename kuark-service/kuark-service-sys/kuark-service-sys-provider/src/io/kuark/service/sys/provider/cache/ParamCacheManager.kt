package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheManager
import io.kuark.ability.data.rdb.kit.RdbKit
import io.kuark.base.bean.BeanKit
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.dict.SysParamCacheItem
import io.kuark.service.sys.common.vo.param.SysParamSearchPayload
import io.kuark.service.sys.provider.dao.SysParamDao
import io.kuark.service.sys.provider.model.po.SysParam
import io.kuark.service.sys.provider.model.table.SysParams
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class ParamCacheManager : AbstractCacheManager<SysParamCacheItem>() {

    @Autowired
    private lateinit var sysParamDao: SysParamDao

    @Autowired
    private lateinit var self: ParamCacheManager

    companion object {
        private const val SYS_PARAM_BY_MODULE_AND_NAME = "sys_param_by_module_and_name"
    }

    override fun cacheName(): String = SYS_PARAM_BY_MODULE_AND_NAME

    override fun doReload(key: String): SysParamCacheItem? {
        require(key.contains(":")) { "缓存${cacheName()}的key格式必须是 模块代码::参数名称" }
        val moduleAndParamName = key.split(":")
        return self.getParamFromCache(moduleAndParamName[0], moduleAndParamName[1])
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive()) {
            log.info("缓存未开启，不加载和缓存所有启用状态的参数！")
            return
        }

        // 加载所有可用的参数
        val searchPayload = SysParamSearchPayload().apply {
            active = true
            returnEntityClass = SysParamCacheItem::class
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val params = sysParamDao.search(searchPayload) as List<SysParamCacheItem>
        log.debug("从数据库加载了${params.size}条参数信息。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存参数
        params.forEach {
            val key = "${it.module}::${it.paramName}"
            CacheKit.putIfAbsent(cacheName(), key, it)
        }
        log.debug("缓存了${params.size}条参数信息。")
    }

    @Cacheable(
        value = [SYS_PARAM_BY_MODULE_AND_NAME],
        key = "#module.concat('::').concat(#name)",
        unless = "#result == null"
    )
    open fun getParamFromCache(module: String, name: String): SysParamCacheItem? {
        if (CacheKit.isCacheActive()) {
            log.debug("缓存中不存在模块为${module}且名称为${name}的参数，从数据库中加载...")
        }
        val paramList = RdbKit.getDatabase().from(SysParams)
            .select(SysParams.columns)
            .whereWithConditions {
                it += (SysParams.paramName eq name) and (SysParams.active eq true)
                if (module.isNotEmpty()) {
                    it += SysParams.module eq module
                }
            }
            .map { row ->
                val entity = SysParams.createEntity(row)
                BeanKit.copyProperties(entity, SysParamCacheItem())
            }
            .toList()
        return if (paramList.isEmpty()) {
            log.warn("数据库中不存在模块为${module}且名称为${name}的参数！")
            null
        } else {
            log.debug("数据库中加载到模块为${module}且名称为${name}的参数。")
            paramList.first()
        }
    }

    fun syncOnInsert(any: Any, id: String) {
        if (CacheKit.isCacheActive() && CacheKit.isWriteInTime(cacheName())) {
            log.debug("新增id为${id}的参数后，同步${cacheName()}缓存...")
            val module = BeanKit.getProperty(any, SysParam::module.name) as String
            val paramName = BeanKit.getProperty(any, SysParam::paramName.name) as String
            self.getParamFromCache(module, paramName) // 缓存
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun syncOnUpdate(any: Any, id: String) {
        if (CacheKit.isCacheActive()) {
            log.debug("更新id为${id}的参数后，同步${cacheName()}缓存...")
            val module = BeanKit.getProperty(any, SysParam::module.name) as String
            val paramName = BeanKit.getProperty(any, SysParam::paramName.name) as String
            val key = "${module}::${paramName}"
            CacheKit.evict(cacheName(), key) // 踢除参数缓存
            if (CacheKit.isWriteInTime(cacheName())) {
                self.getParamFromCache(module, paramName) // 重新缓存
            }
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun syncOnUpdateActive(id: String, active: Boolean) {
        if (CacheKit.isCacheActive()) {
            log.debug("更新id为${id}的参数的启用状态后，同步缓存...")
            val sysParam = sysParamDao.get(id)!!
            if (active) {
                if (CacheKit.isWriteInTime(cacheName())) {
                    self.getParamFromCache(sysParam.module!!, sysParam.paramName)
                }
            } else {
                val key = "${sysParam.module}::${sysParam.paramName}"
                CacheKit.evict(cacheName(), key) // 踢除参数缓存
            }
            log.debug("缓存同步完成。")
        }
    }

    fun syncOnDelete(param: SysParam) {
        if (CacheKit.isCacheActive()) {
            log.debug("删除id为${param.id}的参数后，同步从${cacheName()}缓存中踢除...")
            val key = "${param.module}::${param.paramName}"
            CacheKit.evict(cacheName(), key) // 踢除缓存
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

    fun synchOnBatchDelete(ids: Collection<String>, params: List<SysParam>) {
        if (CacheKit.isCacheActive()) {
            log.debug("批量删除id为${ids}的参数后，同步从${cacheName()}缓存中踢除...")
            params.forEach {
                val key = "${it.module}::${it.paramName}"
                CacheKit.evict(cacheName(), key) // 踢除缓存
            }
            log.debug("${cacheName()}缓存同步完成。")
        }
    }

}