package io.kuark.service.sys.provider.cache

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.cache.support.AbstractCacheHandler
import io.kuark.base.bean.BeanKit
import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import io.kuark.base.query.Criteria
import io.kuark.base.query.Criterion
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.service.sys.provider.dao.SysResourceDao
import io.kuark.service.sys.provider.model.po.SysResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
open class ResourceIdBySubSysAndUrlCacheHandler : AbstractCacheHandler<String>() {

    @Autowired
    private lateinit var sysResourceDao: SysResourceDao

    @Autowired
    private lateinit var self: ResourceIdBySubSysAndUrlCacheHandler

    private val log = LogFactory.getLog(ResourceIdBySubSysAndUrlCacheHandler::class)

    companion object {
        private const val CACHE_NAME = "sys_resource_id_by_sub_sys_and_url"
    }

    override fun cacheName(): String = CACHE_NAME

    override fun doReload(key: String): String? {
        require(key.contains(Consts.CACHE_KEY_DEFALT_DELIMITER)) {
            "缓存${CACHE_NAME}的key格式必须是 子系统代码${Consts.CACHE_KEY_DEFALT_DELIMITER}URL"
        }
        val subSysAndUrl = key.split(Consts.CACHE_KEY_DEFALT_DELIMITER)
        return self.getResourceId(subSysAndUrl[0], subSysAndUrl[1])
    }

    override fun reloadAll(clear: Boolean) {
        if (!CacheKit.isCacheActive(CACHE_NAME)) {
            log.info("缓存未开启，不加载和缓存所有包含url的资源id！")
            return
        }

        // 加载所有包含url的资源id
        val criteria = Criteria(Criterion(SysResource::url.name, Operator.IS_NOT_NULL))

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val returnProperties = listOf(SysResource::id.name, SysResource::url.name, SysResource::subSysDictCode.name)
        val ids = sysResourceDao.searchProperties(criteria, returnProperties)
        log.debug("从数据库加载了${ids.size}条包含url的资源id。")

        // 清除缓存
        if (clear) {
            clear()
        }

        // 缓存资源id
        ids.forEach {
            val key = getKey(it[SysResource::subSysDictCode.name] as String, it[SysResource::url.name] as String)
            CacheKit.put(CACHE_NAME, key, it[SysResource::id.name])
        }
        log.debug("缓存了${ids.size}条包含url的资源id。")
    }

    @Cacheable(
        cacheNames = [CACHE_NAME],
        key = "#subSysDictCode.concat('${Consts.CACHE_KEY_DEFALT_DELIMITER}').concat(#url)",
        unless = "#result == null"
    )
    open fun getResourceId(subSysDictCode: String, url: String): String? {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("缓存中不存在子系统为${subSysDictCode}且URL为${url}的资源id，从数据库中加载...")
        }
        require(subSysDictCode.isNotBlank()) { "获取资源id时，子系统代码必须指定！" }
        require(url.isNotBlank()) { "获取资源id时，url必须指定！" }
        val criteria = Criteria.add(SysResource::subSysDictCode.name, Operator.EQ, subSysDictCode)
            .addAnd(SysResource::url.name, Operator.EQ, url)
        val ids = sysResourceDao.searchProperty(criteria, SysResource::id.name)
        return if (ids.isEmpty()) {
            log.debug("数据库中不存在子系统为${subSysDictCode}且URL为${url}的资源id！")
            null
        } else {
            val id = ids.first() as String
            log.debug("数据库中找到子系统为${subSysDictCode}且URL为${url}的资源id：$id！")
            id
        }
    }

    open fun syncOnInsert(any: Any, id: String) {
        if (CacheKit.isCacheActive(CACHE_NAME) && CacheKit.isWriteInTime(CACHE_NAME)) {
            val url = BeanKit.getProperty(any, SysResource::url.name) as String?
            if (StringKit.isNotBlank(url)) {
                log.debug("新增id为${id}的资源后，同步${CACHE_NAME}缓存...")
                val subSysDictCode = BeanKit.getProperty(any, SysResource::subSysDictCode.name) as String
                self.getResourceId(subSysDictCode, url!!) // 缓存
                log.debug("${CACHE_NAME}缓存同步完成。")
            }
        }
    }

    open fun syncOnUpdate(any: Any, id: String, oldUrl: String?) {
        if (CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("更新id为${id}的资源后，同步${CACHE_NAME}缓存...")
            val subSysDictCode = BeanKit.getProperty(any, SysResource::subSysDictCode.name) as String
            if (StringKit.isNotBlank(oldUrl)) {
                CacheKit.evict(CACHE_NAME, getKey(subSysDictCode, oldUrl!!))
            }
            if (CacheKit.isWriteInTime(CACHE_NAME)) {
                val url = BeanKit.getProperty(any, SysResource::url.name) as String?
                if (StringKit.isNotBlank(url)) {
                    self.getResourceId(subSysDictCode, url!!) // 重新缓存
                }
            }
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    open fun syncOnDelete(id: String, subSysDictCode: String, url: String?) {
        if (StringKit.isNotBlank(url) && CacheKit.isCacheActive(CACHE_NAME)) {
            log.debug("删除id为${id}的资源后，同步从${CACHE_NAME}缓存中踢除...")
            CacheKit.evict(CACHE_NAME, getKey(subSysDictCode, url!!))
            log.debug("${CACHE_NAME}缓存同步完成。")
        }
    }

    private fun getKey(subSysDictCode: String, url: String): String {
        return "${subSysDictCode}${Consts.CACHE_KEY_DEFALT_DELIMITER}${url}"
    }

}