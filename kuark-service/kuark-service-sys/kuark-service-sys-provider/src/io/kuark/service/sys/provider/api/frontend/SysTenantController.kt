package io.kuark.service.sys.provider.api.frontend

import io.kuark.service.sys.common.vo.tenant.*
import io.kuark.service.sys.provider.biz.ibiz.ISysTenantBiz
import io.kuark.ability.web.springmvc.BaseCrudController
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping


/**
 * 租户前端控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
//region your codes 1
@RequestMapping("/sys/tenant")
@CrossOrigin
open class SysTenantController :
    BaseCrudController<String, ISysTenantBiz, SysTenantSearchPayload, SysTenantRecord, SysTenantDetail, SysTenantPayload>() {
//endregion your codes 1

    //region your codes 2

    @RequestMapping("/getAllActiveTenants")
    fun getAllActiveTenants(): Map<String, Map<String, String>> {
        val tenants = biz.getAllActiveTenants()
        return tenants.mapValues { v -> v.value.associate { it.id!! to it.name!! } }
    }

    //endregion your codes 2

}