package io.kuark.service.sys.provider.api.backend

import io.kuark.service.sys.common.api.ISysDictApi
import io.kuark.service.sys.common.api.ISysDomainApi
import io.kuark.service.sys.common.vo.domain.SysDomainCacheItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping


/**
 * 域名后端控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
//region your codes 1
@RequestMapping("/sys/domain/api")
open class SysDomainApiController {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var sysDomainApi: ISysDomainApi

    @GetMapping("/getDomainByName")
    fun getDomainByName(domainName: String): SysDomainCacheItem? {
        return sysDomainApi.getDomainByName(domainName)
    }

    //endregion your codes 2

}