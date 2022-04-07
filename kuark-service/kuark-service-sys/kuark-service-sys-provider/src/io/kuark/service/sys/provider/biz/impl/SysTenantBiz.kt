package io.kuark.service.sys.provider.biz.impl

import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.bean.BeanKit
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.tenant.SysTenantDetail
import io.kuark.service.sys.common.vo.tenant.SysTenantRecord
import io.kuark.service.sys.common.vo.tenant.SysTenantSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysTenantBiz
import io.kuark.service.sys.provider.cache.SysCacheNames
import io.kuark.service.sys.provider.dao.SysTenantDao
import io.kuark.service.sys.provider.model.po.SysParam
import io.kuark.service.sys.provider.model.po.SysTenant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/**
 * 租户业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
@CacheConfig(cacheNames = [SysCacheNames.SYS_TENANT])
open class SysTenantBiz : BaseCrudBiz<String, SysTenant, SysTenantDao>(), ISysTenantBiz {
//endregion your codes 1

    //region your codes 2

    private val log = LogFactory.getLog(this::class)

    @Autowired
    private lateinit var self: ISysTenantBiz

    @Cacheable(key = "#subSysDictCode", unless = "#result == null || #result.isEmpty()")
    override fun getTenantsFromCache(subSysDictCode: String): List<SysTenantDetail> {
        if (CacheKit.isCacheActive()) {
            log.debug("缓存中不存在子系统为${subSysDictCode}的租户，从数据库中加载...")
        }
        val searchPayload = SysTenantSearchPayload().apply {
            returnEntityClass = SysTenantDetail::class
            active = true
            this.subSysDictCode = subSysDictCode
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val tenants = dao.search(searchPayload) as List<SysTenantDetail>
        log.debug("从数据库加载了子系统为${subSysDictCode}的${tenants.size}条租户信息。")
        return tenants
    }

    @Transactional
    override fun insert(any: Any): String {
        val id = super.insert(any)
        log.debug("新增id为${id}的租户。")
        // 同步缓存
        if (CacheKit.isCacheActive()) {
            log.debug("新增id为${id}的租户后，同步缓存...")
            val subSysDictCode = BeanKit.getProperty(any, SysTenant::subSysDictCode.name) as String
            CacheKit.evict(SysCacheNames.SYS_TENANT, subSysDictCode) // 踢除缓存，因为缓存的粒度为子系统
            self.getTenantsFromCache(subSysDictCode) // 重新缓存
            log.debug("缓存同步完成。")
        }
        return id
    }

    @Transactional
    override fun update(any: Any): Boolean {
        val success = super.update(any)
        val id = BeanKit.getProperty(any, SysTenant::id.name) as String
        if (success) {
            log.debug("更新id为${id}的租户。")
            if (CacheKit.isCacheActive()) {
                log.debug("更新id为${id}的租户后，同步缓存...")
                val subSysDictCode = BeanKit.getProperty(any, SysTenant::subSysDictCode.name) as String
                CacheKit.evict(SysCacheNames.SYS_TENANT, subSysDictCode) // 踢除缓存，因为缓存的粒度为子系统
                self.getTenantsFromCache(subSysDictCode) // 重新缓存
                log.debug("缓存同步完成。")
            }
        } else {
            log.error("更新id为${id}的租户失败！")
        }
        return success
    }

    @Transactional
    override fun updateActive(id: String, active: Boolean): Boolean {
        val param = SysParam {
            this.id = id
            this.active = active
        }
        val success = dao.update(param)
        if (success) {
            log.debug("更新id为${id}的租户的启用状态为${active}。")
            if (CacheKit.isCacheActive()) {
                log.debug("更新id为${id}的租户的启用状态后，同步缓存...")
                val sysTenant = dao.get(id)!!
                val subSysDictCode = sysTenant.subSysDictCode
                CacheKit.evict(SysCacheNames.SYS_TENANT, subSysDictCode) // 踢除缓存，缓存的粒度为子系统
                self.getTenantsFromCache(subSysDictCode) // 重新缓存
                log.debug("缓存同步完成。")
            }
        } else {
            log.error("更新id为${id}的租户的启用状态为${active}失败！")
        }
        return success
    }

    @Transactional
    override fun deleteById(id: String): Boolean {
        val sysTenant = dao.get(id)!!
        val success = super.deleteById(id)
        if (success) {
            log.debug("删除id为${id}的租户成功！")
            if (CacheKit.isCacheActive()) {
                log.debug("删除id为${id}的租户后，同步从缓存中踢除...")
                val subSysDictCode = sysTenant.subSysDictCode
                CacheKit.evict(SysCacheNames.SYS_TENANT, subSysDictCode) // 踢除缓存，缓存的粒度为子系统
                self.getTenantsFromCache(subSysDictCode) // 重新缓存
                log.debug("缓存同步完成。")
            }
        } else {
            log.error("删除id为${id}的租户失败！")
        }
        return success
    }

    @Transactional
    override fun batchDelete(ids: Collection<String>): Int {
        val count = super.batchDelete(ids)
        log.debug("批量删除租户，期望删除${ids.size}条，实际删除${count}条。")
        if (CacheKit.isCacheActive()) {
            log.debug("批量删除id为${ids}的租户后，同步从缓存中踢除...")
            val subSysDictCodes = dao.inSearchPropertyById(ids, SysTenant::subSysDictCode.name).toSet()
            subSysDictCodes.forEach {
                CacheKit.evict(SysCacheNames.SYS_TENANT, it as String) // 踢除缓存，缓存的粒度为子系统
                self.getTenantsFromCache(it) // 重新缓存
            }
            log.debug("缓存同步完成。")
        }
        return count
    }


    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun getAllActiveTenants(): Map<String, List<SysTenantRecord>> {
        val searchPayload = SysTenantSearchPayload().apply { active = true }
        val records = dao.search(searchPayload) as List<SysTenantRecord>
        return records.groupBy { it.subSysDictCode!! }
    }

    //endregion your codes 2

}