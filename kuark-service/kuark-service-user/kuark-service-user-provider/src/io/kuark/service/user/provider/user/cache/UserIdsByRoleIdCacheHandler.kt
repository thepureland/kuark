package io.kuark.service.user.provider.user.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.context.kit.SpringKit
import io.kuark.service.user.provider.rbac.dao.RbacRoleUserDao
import io.kuark.service.user.provider.rbac.model.po.RbacRoleUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class UserIdsByRoleIdCacheHandler : AbstractCacheHandler<List<String>>() {

    @Autowired
    private lateinit var rbacRoleUserDao: RbacRoleUserDao

    companion object {
        private const val CACHE_NAME = "user_ids_by_role_id"
        private val log = LogFactory.getLog(UserIdsByRoleIdCacheHandler::class)
    }


    override fun cacheName(): String = CACHE_NAME

    override fun doReload(key: String): List<String> {
        return getSelf().getUserIdsByRoleId(key)
    }

    override fun reloadAll(clear: Boolean) {
//        if (!CacheKit.isCacheActive()) {
//            log.info("缓存未开启，不加载和缓存所有缓存配置信息！")
//            return
//        }
//
//        // 加载所有可用的缓存配置
//        val searchPayload = ListSearchPayload().apply {
//            returnEntityClass = SysCacheCacheItem::class
//        }
//
//        @Suppress(Consts.Suppress.UNCHECKED_CAST)
//        val cacheConfigs = sysCacheDao.search(searchPayload) as List<SysCacheCacheItem>
//        log.debug("从数据库加载了${cacheConfigs.size}条缓存配置信息。")
//
//        // 清除缓存
//        if (clear) {
//            clear()
//        }
//
//        // 缓存缓存配置
//        cacheConfigs.forEach {
//            CacheKit.putIfAbsent(cacheName(), it.name!!, it)
//        }
//        log.debug("缓存了${cacheConfigs.size}条缓存配置。")
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#roleId",
        unless = "#result == null || #result.size() == 0"
    )
    open fun getUserIdsByRoleId(roleId: String): List<String> {
        if (CacheKit.isCacheActive(cacheName())) {
            log.debug("${cacheName()}缓存中不存在key为${roleId}的数据，从数据库中加载...")
        }
        val userIds = rbacRoleUserDao.oneSearchProperty(RbacRoleUser::roleId.name, roleId, RbacRoleUser::userId.name)
        log.debug("从数据库中加载roleId为${roleId}的userId共${userIds.size}条。")
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        return userIds as List<String>
    }

    private fun getSelf(): UserIdsByRoleIdCacheHandler {
        return SpringKit.getBean(UserIdsByRoleIdCacheHandler::class)
    }

}