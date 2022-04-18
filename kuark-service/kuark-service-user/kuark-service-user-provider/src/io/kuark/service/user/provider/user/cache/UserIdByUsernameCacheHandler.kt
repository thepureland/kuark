package io.kuark.service.user.provider.user.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.context.kit.SpringKit
import io.kuark.service.user.provider.user.dao.UserAccountDao
import io.kuark.service.user.provider.user.model.po.UserAccount
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class UserIdByUsernameCacheHandler : AbstractCacheHandler<String>() {

    @Autowired
    private lateinit var userAccountDao: UserAccountDao

    companion object {
        private const val CACHE_NAME = "user_id_by_username"
        private val log = LogFactory.getLog(UserIdByUsernameCacheHandler::class)
    }


    override fun cacheName() = CACHE_NAME

    override fun doReload(key: String): String? {
        return getSelf().getUserIdByUsername(key)
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(CACHE_NAME)) {
            log.info("${CACHE_NAME}缓存未开启，不加载和缓存所有用户名数据！")
            return
        }

        // 加载所有用户名
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val results = userAccountDao.allSearchProperties(setOf(UserAccount::id.name, UserAccount::username.name))
        log.debug("从数据库加载了${results.size}条用户名数据。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存
        results.forEach {
            CacheKit.putIfAbsent(CACHE_NAME, it[UserAccount::username.name]!!, it[UserAccount::id.name])
        }
        log.debug("缓存了${results.size}条用户名数据。")
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#username",
        unless = "#result == null"
    )
    open fun getUserIdByUsername(username: String): String? {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("${CACHE_NAME}缓存中不存在key为${username}的用户名，从数据库中加载...")
        }
        val ids = userAccountDao.oneSearchProperty(UserAccount::username.name, username, UserAccount::id.name)
        return if (ids.isEmpty()) {
            log.warn("数据库中找不到username为${username}的用户账号！")
            null
        } else {
            log.debug("数据库中加载到username为${username}的用户账号信息.")
            ids.first() as String
        }
    }

    private fun getSelf(): UserIdByUsernameCacheHandler {
        return SpringKit.getBean(UserIdByUsernameCacheHandler::class)
    }

}