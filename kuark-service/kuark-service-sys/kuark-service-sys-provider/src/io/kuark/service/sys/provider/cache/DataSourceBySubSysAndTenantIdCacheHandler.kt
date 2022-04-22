package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.bean.BeanKit
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.context.kit.SpringKit
import io.kuark.service.sys.common.vo.datasource.SysDataSourceCacheItem
import io.kuark.service.sys.common.vo.datasource.SysDataSourceSearchPayload
import io.kuark.service.sys.provider.dao.SysDataSourceDao
import io.kuark.service.sys.provider.model.po.SysDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class DataSourceBySubSysAndTenantIdCacheHandler : AbstractCacheHandler<SysDataSourceCacheItem>() {

    @Autowired
    private lateinit var sysDataSourceDao: SysDataSourceDao

    companion object {
        private const val CACHE_NAME = "sys_data_source_by_sub_sys_and_tenant_id"
        private val log = LogFactory.getLog(DataSourceBySubSysAndTenantIdCacheHandler::class)
    }

    override fun cacheName() = CACHE_NAME

    override fun doReload(key: String): SysDataSourceCacheItem? {
        require(key.contains(":")) { "缓存${CACHE_NAME}的key格式必须是 子系统代码::租户id" }
        val subSysAndTenantId = key.split("::")
        return getSelf().getDataSource(subSysAndTenantId[0], subSysAndTenantId[1])
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(CACHE_NAME)) {
            log.info("缓存未开启，不加载和缓存所有启用状态的数据源！")
            return
        }

        // 加载所有可用的数据源
        val payload = SysDataSourceSearchPayload().apply {
            returnEntityClass = SysDataSourceCacheItem::class
            active = true
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val results = sysDataSourceDao.search(payload) as List<SysDataSourceCacheItem>

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存数据
        results.forEach {
            val key = "${it.subSysDictCode}::${it.tenantId}"
            CacheKit.putIfAbsent(CACHE_NAME, key, it)
        }
        log.debug("缓存了数据源共${results.size}条。")
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#subSysDictCode.concat('::').concat(#tenantId)",
        unless = "#result == null"
    )
    open fun getDataSource(subSysDictCode: String, tenantId: String?): SysDataSourceCacheItem? {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("${CACHE_NAME}缓存中不存在子系统为${subSysDictCode}且租户id为${tenantId}的数据源，从数据库中加载...")
        }
        require(subSysDictCode.isNotBlank()) { "获取数据源时，子系统代码必须指定！" }
        val searchPayload = SysDataSourceSearchPayload().apply {
            returnEntityClass = SysDataSourceCacheItem::class
            active = true
            this.subSysDictCode = subSysDictCode
            this.tenantId = tenantId
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val results = sysDataSourceDao.search(searchPayload) as List<SysDataSourceCacheItem>
        return if (results.isEmpty()) {
            log.warn("数据库中找不到子系统为${subSysDictCode}且租户id为${tenantId}的数据源！")
            null
        } else {
            val result = results.first()
            if (results.size > 1) {
                log.warn("数据库中找到${results.size}条子系统为${subSysDictCode}且租户id为${tenantId}的数据源，任取一条！")
            } else {
                log.debug("从数据库加载到子系统为${subSysDictCode}且租户id为${tenantId}的数据源#${result.id}")
            }
            result
        }
    }

    fun syncOnInsert(any: Any, id: String) {
        if (CacheKit.isCacheActive(CACHE_NAME) && CacheKit.isWriteInTime(CACHE_NAME)) {
            log.debug("新增id为${id}的数据源后，同步${CACHE_NAME}缓存...")
            val subSysDictCode = BeanKit.getProperty(any, SysDataSource::subSysDictCode.name) as String
            val tenantId = BeanKit.getProperty(any, SysDataSource::tenantId.name) as String?
            getSelf().getDataSource(subSysDictCode, tenantId) // 缓存
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun syncOnUpdate(any: Any, id: String) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("更新id为${id}的数据源后，同步${CACHE_NAME}缓存...")
            val subSysDictCode = BeanKit.getProperty(any, SysDataSource::subSysDictCode.name) as String
            val tenantId = BeanKit.getProperty(any, SysDataSource::tenantId.name) as String?
            val key = "${subSysDictCode}::${tenantId}"
            CacheKit.evict(CACHE_NAME, key) // 踢除数据源缓存
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                getSelf().getDataSource(subSysDictCode, tenantId) // 重新缓存
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun syncOnUpdateActive(id: String, active: Boolean) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("更新id为${id}的数据源的启用状态后，同步${CACHE_NAME}缓存...")
            val dataSource = sysDataSourceDao.get(id)!!
            if (active) {
                if (CacheKit.isWriteInTime(CACHE_NAME)) {
                    getSelf().getDataSource(dataSource.subSysDictCode, dataSource.tenantId) // 重新缓存
                }
            } else {
                val key = "${dataSource.subSysDictCode}::${dataSource.tenantId}"
                CacheKit.evict(CACHE_NAME, key) // 踢除数据源缓存
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun syncOnDelete(id: String, subSysDictCode: String, tenantId: String?) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("删除id为${id}的数据源后，同步从${CACHE_NAME}缓存中踢除...")
            val key = "${subSysDictCode}::${tenantId}"
            CacheKit.evict(CACHE_NAME, key) // 踢除缓存, 数据源缓存的粒度到数据源类型
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun getSelf(): DataSourceBySubSysAndTenantIdCacheHandler {
        return SpringKit.getBean(DataSourceBySubSysAndTenantIdCacheHandler::class)
    }

}