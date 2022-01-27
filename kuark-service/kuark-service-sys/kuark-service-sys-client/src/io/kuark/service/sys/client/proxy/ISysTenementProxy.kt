package io.kuark.service.sys.client.proxy

import io.kuark.service.sys.common.api.ISysTenementApi
import io.kuark.service.sys.client.fallback.SysTenementFallback
import org.springframework.cloud.openfeign.FeignClient


/**
 * 租户客户端代理接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
@FeignClient(name = "sys-tenement", fallback = SysTenementFallback::class)
interface ISysTenementProxy : ISysTenementApi {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}