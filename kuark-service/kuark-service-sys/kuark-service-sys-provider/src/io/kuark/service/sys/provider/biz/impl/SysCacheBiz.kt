package io.kuark.service.sys.provider.biz.impl

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.bean.BeanKit
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.cache.SysCacheDetail
import io.kuark.service.sys.common.vo.cache.SysCacheSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysCacheBiz
import io.kuark.service.sys.provider.cache.SysCacheNames
import io.kuark.service.sys.provider.dao.SysCacheDao
import io.kuark.service.sys.provider.model.po.SysCache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/**
 * 缓存业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
@CacheConfig(cacheNames = [SysCacheNames.SYS_CACHE])
open class SysCacheBiz : BaseCrudBiz<String, SysCache, SysCacheDao>(), ISysCacheBiz {
//endregion your codes 1

    //region your codes 2

    private val log = LogFactory.getLog(this::class)

    @Autowired
    private lateinit var self: ISysCacheBiz

    @Cacheable(key = "#name", unless = "#result == null")
    override fun getCacheFromCache(name: String): SysCacheDetail? {
        val searchPayload = SysCacheSearchPayload().apply {
            returnEntityClass = SysCacheDetail::class
            this.name = name
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        return dao.search(searchPayload).firstOrNull() as SysCacheDetail?
    }

    @Transactional
    override fun insert(any: Any): String {
        val id = super.insert(any)
        log.debug("新增id为${id}的缓存配置。")
        // 同步缓存
        if (CacheKit.isCacheActive()) {
            log.debug("新增id为${id}的缓存配置后，同步缓存...")
            val name = BeanKit.getProperty(any, SysCache::name.name) as String
            self.getCacheFromCache(name) // 缓存
            log.debug("缓存同步完成。")
        }
        return id
    }

    @Transactional
    override fun update(any: Any): Boolean {
        val success = super.update(any)
        val id = BeanKit.getProperty(any, SysCache::id.name) as String
        if (success) {
            log.debug("更新id为${id}的缓存配置。")
            if (CacheKit.isCacheActive()) {
                log.debug("更新id为${id}的缓存配置后，同步缓存...")
                val name = BeanKit.getProperty(any, SysCache::name.name) as String
                CacheKit.evict(SysCacheNames.SYS_CACHE, name) // 踢除缓存配置缓存
                self.getCacheFromCache(name) // 重新缓存
                log.debug("缓存同步完成。")
            }
        } else {
            log.error("更新id为${id}的缓存配置失败！")
        }
        return success
    }

    @Transactional
    override fun deleteById(id: String): Boolean {
        val sysCache = dao.get(id)!!
        val success = super.deleteById(id)
        if (success) {
            log.debug("删除id为${id}的缓存配置成功！")
            if (CacheKit.isCacheActive()) {
                log.debug("删除id为${id}的缓存配置后，同步从缓存中踢除...")
                CacheKit.evict(SysCacheNames.SYS_CACHE, sysCache.name) // 踢除缓存
                log.debug("缓存同步完成。")
            }
        } else {
            log.error("删除id为${id}的缓存配置失败！")
        }
        return success
    }

    @Transactional
    override fun batchDelete(ids: Collection<String>): Int {
        val count = super.batchDelete(ids)
        log.debug("批量删除缓存配置，期望删除${ids.size}条，实际删除${count}条。")
        if (CacheKit.isCacheActive()) {
            log.debug("批量删除id为${ids}的缓存配置后，同步从缓存中踢除...")
            val sysCaches = dao.inSearchById(ids)
            sysCaches.forEach {
                CacheKit.evict(SysCacheNames.SYS_CACHE, it.name) // 踢除缓存
            }
            log.debug("缓存同步完成。")
        }
        return count
    }


    //endregion your codes 2

}