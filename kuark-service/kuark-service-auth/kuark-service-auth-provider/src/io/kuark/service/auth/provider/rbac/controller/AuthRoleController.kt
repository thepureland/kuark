package io.kuark.service.auth.provider.rbac.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.auth.common.rbac.vo.role.AuthRolePayload
import io.kuark.service.auth.common.rbac.vo.role.AuthRoleRecord
import io.kuark.service.auth.common.rbac.vo.role.AuthRoleSearchPayload
import io.kuark.service.auth.provider.rbac.ibiz.IAuthRoleBiz
import io.kuark.service.auth.provider.rbac.model.po.AuthRole
import org.springframework.web.bind.annotation.GetMapping

class AuthRoleController: BaseCrudController<String, IAuthRoleBiz, AuthRoleSearchPayload, AuthRoleRecord, AuthRolePayload>() {

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): WebResult<Boolean> {
        val param = AuthRole {
            this.id = id
            this.active = active
        }
        return WebResult(biz.update(param))
    }

}