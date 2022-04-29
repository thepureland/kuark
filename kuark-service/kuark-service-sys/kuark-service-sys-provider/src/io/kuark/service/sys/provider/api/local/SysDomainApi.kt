package io.kuark.service.sys.provider.api.local

import io.kuark.service.sys.common.api.ISysDomainApi
import io.kuark.service.sys.common.vo.domain.SysDomainCacheItem
import io.kuark.service.sys.provider.biz.ibiz.ISysDomainBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


/**
 * 域名 API本地实现
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
@Service
open class SysDomainApi : ISysDomainApi {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var sysDomainBiz: ISysDomainBiz

    override fun getDomainByName(domainName: String): SysDomainCacheItem? {
        return sysDomainBiz.getDomainByName(domainName)
    }

    //endregion your codes 2


}