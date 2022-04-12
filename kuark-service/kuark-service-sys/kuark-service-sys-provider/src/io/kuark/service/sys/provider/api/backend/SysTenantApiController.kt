package io.kuark.service.sys.provider.api.backend

import io.kuark.service.sys.common.api.ISysTenantApi
import io.kuark.service.sys.common.vo.dict.SysTenantCacheItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * 租户后端控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
//region your codes 1
@RequestMapping("/sys/tenant/api")
open class SysTenantApiController {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var sysTenantApi: ISysTenantApi


    @PostMapping("/getTenant")
    fun getTenant(id: String): SysTenantCacheItem? {
        return sysTenantApi.getTenant(id)
    }

    @PostMapping("/getTenants")
    fun getTenants(subSysDictCode: String): List<SysTenantCacheItem> {
        return sysTenantApi.getTenants(subSysDictCode)
    }

    //endregion your codes 2

}