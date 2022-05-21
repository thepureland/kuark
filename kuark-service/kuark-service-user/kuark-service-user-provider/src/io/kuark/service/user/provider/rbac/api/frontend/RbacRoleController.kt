package io.kuark.service.user.provider.rbac.api.frontend

import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.base.lang.string.StringKit
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.user.common.rbac.vo.role.*
import io.kuark.service.user.common.user.vo.account.UserAccountCacheItem
import io.kuark.service.user.common.user.vo.account.UserAccountSearchPayload
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacRoleBiz
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/rbac/role")
open class RbacRoleController :
    BaseCrudController<String, IRbacRoleBiz, RbacRoleSearchPayload, RbacRoleRecord, RbacRoleDetail, RbacRolePayload>() {

    @GetMapping("/updateActive")
    fun updateActive(roleId: String, active: Boolean): Boolean {
        return biz.updateActive(roleId, active)
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
     * @return Pair<List<UserAccountCacheItem>, 总记录数>
     * @throws IllegalArgumentException searchPayload._roleId为空时
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/searchAssignedUsers")
    fun searchAssignedUsers(@RequestBody searchPayload: UserAccountSearchPayload): Pair<List<UserAccountCacheItem>, Int> {
        val roleId = searchPayload._roleId
        require(StringKit.isNotBlank(roleId)) { "角色id必须指定！" }
        val userIds = biz.getAssignedUsers(roleId!!).toList()
        val users = biz.searchAssignedUsers(searchPayload, userIds)
        return Pair(users, userIds.size)
    }

    /**
     * 返回候选的角色
     *
     * @param payload ResourceRolesAssignmentPayload
     * @return Pair(List(角色id和名称对象), Set(已分配的角色id))
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/getResourceRoles")
    fun getResourceRoles(@RequestBody @Valid payload: ResourceRolesAssignmentPayload): Pair<List<RoleIdAndName>, Set<String>> {
        // 查询可选的角色
        val searchPayload = RbacRoleSearchPayload().apply {
            returnEntityClass = RoleIdAndName::class
            this.subSysDictCode = payload.subSysDictCode!!
            this.tenantId = payload.tenantId
            active = true
            pageNo = null
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val candidateRoles = biz.search(searchPayload) as List<RoleIdAndName>

        // 查询资源已分配的角色
        val roleIds = biz.getRoleIdsForResource(payload.resourceId!!, payload.subSysDictCode!!, payload.tenantId)

        return Pair(candidateRoles, roleIds)
    }

    /**
     * 为资源重新关联角色id
     *
     * @param payload ResourceRolesAssignmentPayload
     * @return 是否关联成功
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/reassignRolesForResource")
    fun reassignRolesForResource(@RequestBody @Valid payload: ResourceRolesAssignmentPayload): Boolean {
        return biz.reassignRolesForResource(
            payload.resourceId!!,
            payload.subSysDictCode!!,
            payload.tenantId,
            payload.roleIds
        )
    }

}