package io.kuark.service.sys.client.proxy

import io.kuark.service.sys.common.api.ISysTenantApi
import io.kuark.service.sys.client.fallback.SysTenantFallback
import io.kuark.service.sys.common.vo.tenant.SysTenantRecord
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping


/**
 * 租户客户端代理接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
@FeignClient(name = "sys-tenant", fallback = SysTenantFallback::class)
interface ISysTenantProxy : ISysTenantApi {
//endregion your codes 1

    //region your codes 2

    @PostMapping("/sys/tenant/api/getTenant")
    override fun getTenant(id: String): SysTenantRecord?

    @PostMapping("/sys/tenant/api/getTenants")
    override fun getTenants(subSysDictCode: String): List<SysTenantRecord>

    //endregion your codes 2

}