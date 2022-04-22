package io.kuark.service.sys.provider.biz.impl

import io.kuark.service.sys.provider.biz.ibiz.ISysDataSourceBiz
import io.kuark.service.sys.provider.model.po.SysDataSource
import io.kuark.service.sys.provider.dao.SysDataSourceDao
import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.bean.BeanKit
import io.kuark.base.log.LogFactory
import io.kuark.base.support.Consts
import io.kuark.base.support.payload.ListSearchPayload
import io.kuark.service.sys.common.api.ISysTenantApi
import io.kuark.service.sys.common.vo.datasource.SysDataSourceCacheItem
import io.kuark.service.sys.common.vo.datasource.SysDataSourceDetail
import io.kuark.service.sys.common.vo.datasource.SysDataSourceRecord
import io.kuark.service.sys.provider.cache.DataSourceByIdCacheHandler
import io.kuark.service.sys.provider.cache.DataSourceBySubSysAndTenantIdCacheHandler
import io.kuark.service.sys.provider.model.po.SysDomain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.reflect.KClass


/**
 * 数据源业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class SysDataSourceBiz : BaseCrudBiz<String, SysDataSource, SysDataSourceDao>(), ISysDataSourceBiz {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var sysTenantApi: ISysTenantApi

    @Autowired
    private lateinit var dataSourceByIdCacheHandler: DataSourceByIdCacheHandler

    @Autowired
    private lateinit var dataSourceBySubSysAndTenantIdCacheHandler: DataSourceBySubSysAndTenantIdCacheHandler

    private val log = LogFactory.getLog(this::class)

    override fun getDataSource(subSysDictCode: String, tenantId: String?): SysDataSourceCacheItem? {
        return dataSourceBySubSysAndTenantIdCacheHandler.getDataSource(subSysDictCode, tenantId)
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun pagingSearch(listSearchPayload: ListSearchPayload): Pair<List<*>, Int> {
        val pair = super.pagingSearch(listSearchPayload)

        // 根据租户id获取租户名称
        val records = pair.first as List<SysDataSourceRecord>
        val tenantIds = records.map { it.tenantId!! }
        val tenants = sysTenantApi.getTenants(tenantIds)
        records.forEach {
            it.tenantName = tenants[it.tenantId]?.name
        }

        return pair
    }

    override fun <R : Any> get(id: String, returnType: KClass<R>): R? {
        val result = super.get(id, returnType)
        if (returnType == SysDataSourceDetail::class) {
            val tenantId = (result as SysDataSourceDetail).tenantId
            result.tenantName = sysTenantApi.getTenant(tenantId!!)?.name
        }
        return result
    }

    @Transactional
    override fun insert(any: Any): String {
        val id = super.insert(any)
        log.debug("新增id为${id}的数据源。")
        // 同步缓存
        dataSourceByIdCacheHandler.syncOnInsert(id)
        dataSourceBySubSysAndTenantIdCacheHandler.syncOnInsert(any, id)
        return id
    }

    @Transactional
    override fun update(any: Any): Boolean {
        val success = super.update(any)
        val id = BeanKit.getProperty(any, SysDomain::id.name) as String
        if (success) {
            // 同步缓存
            dataSourceByIdCacheHandler.syncOnUpdate(id)
            dataSourceBySubSysAndTenantIdCacheHandler.syncOnUpdate(any, id)
        } else {
            log.error("更新id为${id}的数据源失败！")
        }
        return success
    }

    @Transactional
    override fun updateActive(id: String, active: Boolean): Boolean {
        val dataSource = SysDataSource {
            this.id = id
            this.active = active
        }
        val success = dao.update(dataSource)
        if (success) {
            // 同步缓存
            dataSourceByIdCacheHandler.syncOnUpdate(id)
            dataSourceBySubSysAndTenantIdCacheHandler.syncOnUpdateActive(id, active)
        } else {
            log.error("更新id为${id}的数据源的启用状态为${active}失败！")
        }
        return success
    }

    @Transactional
    override fun deleteById(id: String): Boolean {
        val dataSource = dao.get(id)!!
        val success = super.deleteById(id)
        if (success) {
            // 同步缓存
            dataSourceByIdCacheHandler.syncOnDelete(id)
            dataSourceBySubSysAndTenantIdCacheHandler.syncOnDelete(id, dataSource.subSysDictCode, dataSource.tenantId)
        } else {
            log.error("删除id为${id}的数据源失败！")
        }
        return success
    }

    @Transactional
    override fun batchDelete(ids: Collection<String>): Int {
        val props = setOf(SysDataSource::id.name, SysDataSource::subSysDictCode.name, SysDataSource::tenantId.name)
        val mapList = dao.inSearchPropertiesById(ids, props)
        val count = super.batchDelete(ids)
        log.debug("批量删除数据源，期望删除${ids.size}条，实际删除${count}条。")
        // 同步缓存
        dataSourceByIdCacheHandler.syncOnBatchDelete(ids)
        mapList.forEach {
            dataSourceBySubSysAndTenantIdCacheHandler.syncOnDelete(
                it[SysDataSource::id.name] as String,
                it[SysDataSource::subSysDictCode.name] as String,
                it[SysDataSource::tenantId.name] as String?
            )
        }
        return count
    }

    //endregion your codes 2

}