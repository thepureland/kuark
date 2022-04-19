package io.kuark.service.sys.provider.biz.impl

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.bean.BeanKit
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.dict.SysTenantCacheItem
import io.kuark.service.sys.common.vo.tenant.SysTenantRecord
import io.kuark.service.sys.common.vo.tenant.SysTenantSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysTenantBiz
import io.kuark.service.sys.provider.cache.TenantByIdCacheHandler
import io.kuark.service.sys.provider.cache.TenantsBySubSysCacheHandler
import io.kuark.service.sys.provider.dao.SysTenantDao
import io.kuark.service.sys.provider.model.po.SysParam
import io.kuark.service.sys.provider.model.po.SysTenant
import org.springframework.beans.factory.annotation.Autowired
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
open class SysTenantBiz : BaseCrudBiz<String, SysTenant, SysTenantDao>(), ISysTenantBiz {
//endregion your codes 1

    //region your codes 2

    private val log = LogFactory.getLog(this::class)

    @Autowired
    private lateinit var tenantByIdCacheHandler: TenantByIdCacheHandler

    @Autowired
    private lateinit var tenantBySubSysCacheHandler: TenantsBySubSysCacheHandler

    override fun getTenantFromCache(id: String): SysTenantCacheItem? {
        return tenantByIdCacheHandler.getTenantById(id)
    }

    override fun getTenantsFromCache(subSysDictCode: String): List<SysTenantCacheItem> {
        return tenantBySubSysCacheHandler.getTenantsFromCache(subSysDictCode)
    }

    @Transactional
    override fun insert(any: Any): String {
        val id = super.insert(any)
        log.debug("新增id为${id}的租户。")
        // 同步缓存
        tenantByIdCacheHandler.syncOnInsert(id)
        tenantBySubSysCacheHandler.syncOnInsert(any, id)
        return id
    }

    @Transactional
    override fun update(any: Any): Boolean {
        val success = super.update(any)
        val id = BeanKit.getProperty(any, SysTenant::id.name) as String
        if (success) {
            // 同步缓存
            tenantByIdCacheHandler.syncOnUpdate(id)
            tenantBySubSysCacheHandler.syncOnUpdate(any, id)
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
            // 同步缓存
            tenantByIdCacheHandler.syncOnUpdate(id)
            tenantBySubSysCacheHandler.syncOnUpdate(null, id)
        } else {
            log.error("更新id为${id}的租户的启用状态为${active}失败！")
        }
        return success
    }

    @Transactional
    override fun deleteById(id: String): Boolean {
        val sysTenant = tenantByIdCacheHandler.getTenantById(id)!!
        val success = super.deleteById(id)
        if (success) {
            // 同步缓存
            tenantByIdCacheHandler.syncOnDelete(id)
            tenantBySubSysCacheHandler.syncOnDelete(sysTenant)
        } else {
            log.error("删除id为${id}的租户失败！")
        }
        return success
    }

    @Transactional
    override fun batchDelete(ids: Collection<String>): Int {
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val subSysDictCodes = dao.inSearchPropertyById(ids, SysTenant::subSysDictCode.name).toSet() as Set<String>
        val count = super.batchDelete(ids)
        log.debug("批量删除租户，期望删除${ids.size}条，实际删除${count}条。")
        // 同步缓存
        tenantByIdCacheHandler.syncOnBatchDelete(ids)
        tenantBySubSysCacheHandler.synchOnBatchDelete(ids, subSysDictCodes)
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