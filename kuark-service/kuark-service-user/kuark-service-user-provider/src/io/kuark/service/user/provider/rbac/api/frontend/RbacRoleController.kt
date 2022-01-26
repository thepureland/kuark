package io.kuark.service.user.provider.rbac.api.frontend

import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.user.common.rbac.vo.role.*
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacRoleBiz

import io.kuark.service.user.provider.rbac.model.po.RbacRole
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rbac/role")
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

    @GetMapping("/getMenuPermissions")
    fun getMenuPermissions(roleId: String): Pair<List<BaseMenuTreeNode>, List<String>> {
        return biz.getMenuPermissions(roleId)
    }

    @PostMapping("/setRolePermissions")
    fun setRolePermissions(@RequestBody payload: RoleAuthorizationPayload): Boolean {
        return biz.setRolePermissions(payload.roleId!!, payload.resourceIds!!)
    }

}