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
open class UserIdBySubSysAndUsernameCacheHandler : AbstractCacheHandler<String>() {

    @Autowired
    private lateinit var userAccountDao: UserAccountDao

    companion object {
        private const val CACHE_NAME = "user_id_by_sub_sys_and_username"
        private val log = LogFactory.getLog(UserIdBySubSysAndUsernameCacheHandler::class)
    }


    override fun cacheName() = CACHE_NAME

    override fun doReload(key: String): String? {
        require(key.contains(":")) { "缓存${CACHE_NAME}的key格式必须是 子系统代码::用户名" }
        val subSysAndUsername = key.split("::")
        return getSelf().getUserId(subSysAndUsername[0], subSysAndUsername[1])
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(CACHE_NAME)) {
            log.info("${CACHE_NAME}缓存未开启，不加载和缓存所有用户id数据！")
            return
        }

        // 加载所有用户账号
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val results = userAccountDao.allSearchProperties(
            setOf(UserAccount::id.name, UserAccount::subSysDictCode.name, UserAccount::username.name)
        )
        log.debug("从数据库加载了${results.size}条用户id数据。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存
        results.forEach {
            val key = "${it[UserAccount::subSysDictCode.name]}::${it[UserAccount::username.name]}"
            CacheKit.putIfAbsent(CACHE_NAME, key, it[UserAccount::id.name])
        }
        log.debug("缓存了${results.size}条用户id数据。")
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#subSysDictCode.concat('::').concat(#username)",
        unless = "#result == null"
    )
    open fun getUserId(subSysDictCode: String, username: String): String? {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("${CACHE_NAME}缓存中不存在子系统为${subSysDictCode}且用户名为${username}的用户id，从数据库中加载...")
        }
        val ids = userAccountDao.oneSearchProperty(UserAccount::username.name, username, UserAccount::id.name)
        return if (ids.isEmpty()) {
            log.warn("数据库中找不到子系统为${subSysDictCode}且用户名为${username}的用户id！")
            null
        } else {
            log.debug("数据库中加载到子系统为${subSysDictCode}且用户名为${username}的用户id.")
            ids.first() as String
        }
    }

    private fun getSelf(): UserIdBySubSysAndUsernameCacheHandler {
        return SpringKit.getBean(UserIdBySubSysAndUsernameCacheHandler::class)
    }

}