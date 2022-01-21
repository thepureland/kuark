package io.kuark.service.user.provider.rbac.api.frontend

import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.ability.web.springmvc.FrontEndApi
import io.kuark.service.user.common.rbac.vo.role.RbacRoleDetail
import io.kuark.service.user.common.rbac.vo.role.RbacRolePayload
import io.kuark.service.user.common.rbac.vo.role.RbacRoleRecord
import io.kuark.service.user.common.rbac.vo.role.RbacRoleSearchPayload
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacRoleBiz

import io.kuark.service.user.provider.rbac.model.po.RbacRole
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rbac/role")
@FrontEndApi
@CrossOrigin
open class RbacRoleController :
    BaseCrudController<String, IRbacRoleBiz, RbacRoleSearchPayload, RbacRoleRecord, RbacRoleDetail, RbacRolePayload>() {

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): Boolean {
        val param = RbacRole {
            this.id = id
            this.active = active
        }
        return biz.update(param)
    }

}