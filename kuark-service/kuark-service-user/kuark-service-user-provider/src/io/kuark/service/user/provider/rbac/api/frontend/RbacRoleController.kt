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

//    /**
//     * 为角色分配用户
//     *
//     * @param roleId 角色ID
//     * @param userIds 用户id集合
//     * @return 是否分配成功
//     * @author K
//     * @since 1.0.0
//     */
//    @PostMapping("/assignUser")
//    fun assignUser(@RequestBody payload: RoleAssignUserPayload): Boolean {
//        return biz.assignUser(payload.roleId, payload.userIds)
//    }
//
//    /**
//     * 返回已关联的用户的id
//     *
//     * @param roleId 角色id
//     * @return Set(用户id)
//     * @author K
//     * @since 1.0.0
//     */
//    fun getAssignUsers(roleId: String): Set<String>
//
//    /**
//     * 返回候选的用户
//     *
//     * @param subSysDictCode 子系统代码
//     * @return LinkedHashMap(用户id， 用户名)
//     * @author K
//     * @since 1.0.0
//     */
//    fun getCandidateUsers(subSysDictCode: String): LinkedHashMap<String, String>

}