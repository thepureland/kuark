package io.kuark.service.sys.client.fallback

import io.kuark.service.sys.client.proxy.ISysTenantProxy
import io.kuark.service.sys.common.vo.dict.SysTenantCacheItem
import io.kuark.service.sys.common.vo.tenant.SysTenantRecord
import org.springframework.stereotype.Component


/**
 * 租户容错处理
 *
 * @author K
 * @since 1.0.0
 */
@Component
//region your codes 1
interface SysTenantFallback : ISysTenantProxy {
//endregion your codes 1

    //region your codes 2

    override fun getTenant(id: String): SysTenantCacheItem? {
        TODO("Not yet implemented")
    }

    override fun getTenants(ids: Collection<String>): Map<String, SysTenantCacheItem> {
        TODO("Not yet implemented")
    }

    override fun getTenants(subSysDictCode: String): List<SysTenantCacheItem> {
        TODO("Not yet implemented")
    }

    //endregion your codes 2

}