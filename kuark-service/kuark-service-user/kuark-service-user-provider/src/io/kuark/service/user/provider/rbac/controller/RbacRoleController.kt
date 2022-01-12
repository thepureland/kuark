package io.kuark.service.user.provider.rbac.controller

import io.kuark.ability.web.common.WebResult
import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.user.common.rbac.vo.role.RbacRolePayload
import io.kuark.service.user.common.rbac.vo.role.RbacRoleRecord
import io.kuark.service.user.common.rbac.vo.role.RbacRoleSearchPayload
import io.kuark.service.user.provider.rbac.ibiz.IRbacRoleBiz

import io.kuark.service.user.provider.rbac.model.po.RbacRole
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rbac/role")
@CrossOrigin
open class RbacRoleController :
    BaseCrudController<String, IRbacRoleBiz, RbacRoleSearchPayload, RbacRoleRecord, RbacRolePayload>() {

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): WebResult<Boolean> {
        val param = RbacRole {
            this.id = id
            this.active = active
        }
        return WebResult(biz.update(param))
    }

}