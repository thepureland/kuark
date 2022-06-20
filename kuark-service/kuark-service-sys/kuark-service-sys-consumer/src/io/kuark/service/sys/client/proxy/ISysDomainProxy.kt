package io.kuark.service.sys.client.proxy

import io.kuark.service.sys.common.api.ISysDomainApi
import io.kuark.service.sys.client.fallback.SysDomainFallback
import io.kuark.service.sys.common.vo.domain.SysDomainCacheItem
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping


/**
 * 域名客户端代理接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
@FeignClient(name = "sys-domain", fallback = SysDomainFallback::class)
interface ISysDomainProxy : ISysDomainApi {
//endregion your codes 1

    //region your codes 2

    @GetMapping("/sys/domain/api/getDomainByName")
    override fun getDomainByName(domainName: String): SysDomainCacheItem?

    //endregion your codes 2

}