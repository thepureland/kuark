package io.kuark.service.user.provider.rbac.api.frontend

import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.base.lang.string.StringKit
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.user.common.rbac.vo.role.*
import io.kuark.service.user.common.user.vo.account.UserAccountRecord
import io.kuark.service.user.common.user.vo.account.UserAccountSearchPayload
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacRoleBiz
import io.kuark.service.user.provider.rbac.model.po.RbacRole
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

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
    fun setRolePermissions(@RequestBody @Valid payload: RoleResourceAssignmentPayload): Boolean {
        return biz.setRolePermissions(payload.roleId!!, payload.resourceIds!!)
    }

    /**
     * 为角色分配用户
     *
     * @param payload 角色关联用户载体，当关联的用户id列表为空时相当于解除所有关联用户
     * @return 是否分配成功
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/assignUser")
    fun assignUser(@RequestBody @Valid payload: RoleUserAssignmentPayload): Boolean {
        return biz.assignUser(payload.roleId!!, payload.userIds!!)
    }

    /**
     * 返回用户关联情况
     *
     * @param roleId 角色id
     * @param subSysDictCode 子系统代码
     * @param tenantId 租户id
     * @return List(RoleAssignUserResult)
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/getUserAssignment")
    fun getUserAssignment(roleId: String, subSysDictCode: String, tenantId: String?): List<RoleUserAssignmentResult> {
        val candidateUsers = biz.getCandidateUsers(subSysDictCode, tenantId)
        val assignedUsers = biz.getAssignedUsers(roleId)
        return candidateUsers.map {
            RoleUserAssignmentResult().apply {
                userId = it.key
                username = it.value
                assigned = assignedUsers.contains(userId)
            }
        }
    }

    /**
     * 分页查询已分配的用户
     *
     * @param searchPayload 查询载体，_roleId必须指定
     * @return Pair<List<UserAccountRecord>, 总记录数>
     * @throws IllegalArgumentException searchPayload._roleId为空时
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/searchAssignedUsers")
    fun searchAssignedUsers(@RequestBody searchPayload: UserAccountSearchPayload): Pair<List<UserAccountRecord>, Int> {
        val roleId = searchPayload._roleId
        require(StringKit.isNotBlank(roleId)) { "角色id必须指定！" }
        val userIds = biz.getAssignedUsers(roleId!!).toList()
        val users = biz.searchAssignedUsers(searchPayload, userIds)
        return Pair(users, userIds.size)
    }

}