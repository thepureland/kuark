package io.kuark.service.sys.provider.api.frontend

import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.sys.common.vo.tenant.SysTenantDetail
import io.kuark.service.sys.common.vo.tenant.SysTenantPayload
import io.kuark.service.sys.common.vo.tenant.SysTenantRecord
import io.kuark.service.sys.common.vo.tenant.SysTenantSearchPayload
import io.kuark.service.sys.provider.biz.ibiz.ISysTenantBiz
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


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

    /**
     * 返回所有启用的租户
     *
     * @return Map(子系统代码，Map(租户id，租户名称))
     * @author K
     * @since 1.0.0
     */
    @RequestMapping("/getAllActiveTenants")
    fun getAllActiveTenants(): Map<String, Map<String, String>> {
        val tenants = biz.getAllActiveTenants()
        return tenants.mapValues { v -> v.value.associate { it.id!! to it.name!! } }
    }

    //endregion your codes 2

}