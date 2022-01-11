package io.kuark.service.auth.provider.rbac.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.auth.common.rbac.vo.role.AuthRolePayload
import io.kuark.service.auth.common.rbac.vo.role.AuthRoleRecord
import io.kuark.service.auth.common.rbac.vo.role.AuthRoleSearchPayload
import io.kuark.service.auth.provider.rbac.ibiz.IAuthUserGroupBiz
import io.kuark.service.auth.provider.rbac.model.po.AuthRole
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/role")
@CrossOrigin
open class AuthRoleController :
    BaseCrudController<String, IAuthUserGroupBiz, AuthRoleSearchPayload, AuthRoleRecord, AuthRolePayload>() {

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): WebResult<Boolean> {
        val param = AuthRole {
            this.id = id
            this.active = active
        }
        return WebResult(biz.update(param))
    }

}