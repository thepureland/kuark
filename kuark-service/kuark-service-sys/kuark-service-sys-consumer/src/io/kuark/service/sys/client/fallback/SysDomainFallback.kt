package io.kuark.service.sys.client.fallback

import io.kuark.service.sys.client.proxy.ISysDomainProxy
import io.kuark.service.sys.common.vo.domain.SysDomainCacheItem
import org.springframework.stereotype.Component


/**
 * 域名容错处理
 *
 * @author K
 * @since 1.0.0
 */
@Component
//region your codes 1
class SysDomainFallback : ISysDomainProxy {
//endregion your codes 1

    //region your codes 2

    override fun getDomainByName(domainName: String): SysDomainCacheItem? {
        TODO("Not yet implemented")
    }

    //endregion your codes 2

}