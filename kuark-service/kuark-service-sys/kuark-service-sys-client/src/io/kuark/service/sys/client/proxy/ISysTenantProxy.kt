package io.kuark.service.sys.client.proxy

import io.kuark.service.sys.common.api.ISysTenantApi
import io.kuark.service.sys.client.fallback.SysTenantFallback
import org.springframework.cloud.openfeign.FeignClient


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

    //endregion your codes 2

}