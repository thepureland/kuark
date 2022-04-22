package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.bean.BeanKit
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.context.kit.SpringKit
import io.kuark.service.sys.common.vo.domain.SysDomainCacheItem
import io.kuark.service.sys.common.vo.domain.SysDomainSearchPayload
import io.kuark.service.sys.provider.dao.SysDomainDao
import io.kuark.service.sys.provider.model.po.SysDomain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class DomainByNameCacheHandler : AbstractCacheHandler<SysDomainCacheItem>() {

    @Autowired
    private lateinit var dao: SysDomainDao

    companion object {
        private const val CACHE_NAME = "sys_domain_by_name"
        private val log = LogFactory.getLog(DomainByNameCacheHandler::class)
    }

    override fun cacheName() = CACHE_NAME

    override fun doReload(key: String): SysDomainCacheItem? {
        return getSelf().getDomain(key)
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(CACHE_NAME)) {
            log.info("缓存未开启，不加载和缓存所有域名信息！")
            return
        }

        // 加载所有可用的域名
        val searchPayload = SysDomainSearchPayload().apply {
            returnEntityClass = SysDomainCacheItem::class
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val domains = dao.search(searchPayload) as List<SysDomainCacheItem>
        log.debug("从数据库加载了${domains.size}条域名信息。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存域名
        domains.forEach {
            CacheKit.putIfAbsent(CACHE_NAME, it.domain!!, it)
        }
        log.debug("缓存了${domains.size}条域名信息。")
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#domain",
        unless = "#result == null"
    )
    open fun getDomain(domain: String): SysDomainCacheItem? {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("缓存中不存在域名${domain}，从数据库中加载...")
        }

        val searchPayload = SysDomainSearchPayload().apply {
            returnEntityClass = SysDomainCacheItem::class
            this.domain = domain
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val domains = dao.search(searchPayload) as List<SysDomainCacheItem>
        return if (domains.isEmpty()) {
            log.debug("从数据库找不到名为${domain}的域名信息。")
            null
        } else {
            log.debug("从数据库加载了名为${domain}的域名信息。")
            domains.first()
        }
    }

    fun syncOnInsert(any: Any, id: String) {
        if (CacheKit.isCacheActive(CACHE_NAME) && CacheKit.isWriteInTime(CACHE_NAME)) {
            log.debug("新增id为${id}的域名后，同步${CACHE_NAME}缓存...")
            val domain = BeanKit.getProperty(any, SysDomain::domain.name) as String
            CacheKit.evict(CACHE_NAME, domain) // 踢除缓存
            getSelf().getDomain(domain) // 重新缓存
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun syncOnUpdate(any: Any?, id: String) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("更新id为${id}的域名后，同步${CACHE_NAME}缓存...")
            val domain = if (any == null) {
                dao.get(id)!!.domain
            } else {
                BeanKit.getProperty(any, SysDomain::domain.name) as String
            }
            CacheKit.evict(CACHE_NAME, domain) // 踢除缓存
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                getSelf().getDomain(domain) // 重新缓存
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun syncOnDelete(sysDomain: SysDomain, id: String) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("删除id为${id}的域名后，同步从${CACHE_NAME}缓存中踢除...")
            CacheKit.evict(CACHE_NAME, sysDomain.domain) // 踢除缓存
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun syncOnBatchDelete(ids: Collection<String>, domainNames: Set<String>) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("批量删除id为${ids}的域名后，同步从${CACHE_NAME}缓存中踢除...")
            domainNames.forEach {
                CacheKit.evict(CACHE_NAME, it) // 踢除缓存
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    fun getSelf(): DomainByNameCacheHandler {
        return SpringKit.getBean(DomainByNameCacheHandler::class)
    }


}