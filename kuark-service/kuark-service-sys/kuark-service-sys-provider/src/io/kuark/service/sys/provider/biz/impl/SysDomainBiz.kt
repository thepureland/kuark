package io.kuark.service.sys.provider.biz.impl

import io.kuark.service.sys.provider.biz.ibiz.ISysDomainBiz
import io.kuark.service.sys.provider.model.po.SysDomain
import io.kuark.service.sys.provider.dao.SysDomainDao
import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.bean.BeanKit
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.base.support.payload.ListSearchPayload
import io.kuark.service.sys.common.api.ISysTenantApi
import io.kuark.service.sys.common.vo.domain.SysDomainCacheItem
import io.kuark.service.sys.common.vo.domain.SysDomainDetail
import io.kuark.service.sys.common.vo.domain.SysDomainRecord
import io.kuark.service.sys.provider.cache.DomainByNameCacheHandler
import io.kuark.service.sys.provider.model.po.SysDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.reflect.KClass


/**
 * 域名业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class SysDomainBiz : BaseCrudBiz<String, SysDomain, SysDomainDao>(), ISysDomainBiz {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var domainByNameCacheHandler: DomainByNameCacheHandler

    @Autowired
    private lateinit var sysTenantApi: ISysTenantApi

    private val log = LogFactory.getLog(this::class)


    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun pagingSearch(listSearchPayload: ListSearchPayload): Pair<List<*>, Int> {
        val pair = super.pagingSearch(listSearchPayload)

        // 根据租户id获取租户名称
        val records = pair.first as List<SysDomainRecord>
        if (records.isNotEmpty()) {
            val tenantIds = records.map { it.tenantId!! }
            val tenants = sysTenantApi.getTenants(tenantIds)
            records.forEach {
                it.tenantName = tenants[it.tenantId]?.name
            }
        }

        return pair
    }

    override fun <R : Any> get(id: String, returnType: KClass<R>): R? {
        val result = super.get(id, returnType)
        if (returnType == SysDomainDetail::class) {
            val tenantId = (result as SysDomainDetail).tenantId
            result.tenantName = sysTenantApi.getTenant(tenantId!!)?.name
        }
        return result
    }

    override fun getDomainByName(domainName: String): SysDomainCacheItem? {
        return domainByNameCacheHandler.getDomain(domainName)
    }

    @Transactional
    override fun insert(any: Any): String {
        val id = super.insert(any)
        log.debug("新增id为${id}的域名。")
        // 同步缓存
        domainByNameCacheHandler.syncOnInsert(any, id)
        return id
    }

    @Transactional
    override fun update(any: Any): Boolean {
        val success = super.update(any)
        val id = BeanKit.getProperty(any, SysDomain::id.name) as String
        if (success) {
            // 同步缓存
            domainByNameCacheHandler.syncOnUpdate(any, id)
        } else {
            log.error("更新id为${id}的域名失败！")
        }
        return success
    }

    @Transactional
    override fun updateActive(id: String, active: Boolean): Boolean {
        val param = SysDomain {
            this.id = id
            this.active = active
        }
        val success = dao.update(param)
        if (success) {
            // 同步缓存
            domainByNameCacheHandler.syncOnUpdate(null, id)
        } else {
            log.error("更新id为${id}的域名的启用状态为${active}失败！")
        }
        return success
    }

    @Transactional
    override fun deleteById(id: String): Boolean {
        val sysDomain = dao.get(id)!!
        val success = super.deleteById(id)
        if (success) {
            // 同步缓存
            domainByNameCacheHandler.syncOnDelete(sysDomain, id)
        } else {
            log.error("删除id为${id}的域名失败！")
        }
        return success
    }

    @Transactional
    override fun batchDelete(ids: Collection<String>): Int {
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val domainNames = dao.inSearchPropertyById(ids, SysDomain::domain.name).toSet() as Set<String>
        val count = super.batchDelete(ids)
        log.debug("批量删除域名，期望删除${ids.size}条，实际删除${count}条。")
        // 同步缓存
        domainByNameCacheHandler.syncOnBatchDelete(ids, domainNames)
        return count
    }

    //endregion your codes 2

}