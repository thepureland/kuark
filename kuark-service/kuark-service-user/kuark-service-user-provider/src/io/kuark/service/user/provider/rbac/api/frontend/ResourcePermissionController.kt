package io.kuark.service.user.provider.rbac.api.frontend

import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.SysResourceSearchPayload
import io.kuark.service.user.common.rbac.vo.role.RoleUserAssignmentPayload
import io.kuark.service.user.common.rbac.vo.role.RoleUserAssignmentResult
import io.kuark.service.user.common.user.vo.resourcepermission.*
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacRoleBiz
import io.kuark.service.user.provider.rbac.biz.ibiz.IResourcePermissionBiz
import io.kuark.service.user.provider.rbac.biz.impl.RbacRoleBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping("/rbac/resourcepermission")
open class ResourcePermissionController {

    @Autowired
    private lateinit var resourcePermissionBiz: IResourcePermissionBiz

    @Autowired
    private lateinit var rbacRoleBiz: IRbacRoleBiz

    @PostMapping("/search")
    fun search(@RequestBody @Valid payload: ResourcePermissionSearchPayload): List<ResourcePermissionRecord> {
        return resourcePermissionBiz.search(payload)
    }

    @PostMapping("/loadDirectChildrenForTree")
    fun loadDirectChildrenForTree(@RequestBody searchPayload: SysResourceSearchPayload): List<BaseMenuTreeNode> {
        val user = KuarkContextHolder.get().user!!
        return resourcePermissionBiz.loadDirectChildrenMenuForUser(user.id!!, searchPayload)
    }

    @PostMapping("/searchOnClickTree")
    fun searchOnClickTree(@RequestBody @Valid searchPayload: ResourcePermissionTreeSearchPayload): List<ResourcePermissionRecord> {
        return resourcePermissionBiz.searchOnClickTree(searchPayload)
    }

    /**
     * 返回资源所关联的角色情况
     *
     * @param resourceId 资源id
     * @param subSysDictCode 子系统代码
     * @param tenantId 租户id
     * @return List(ResourceRoleAssignmentResult)
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/getRoleAssignment")
    fun getRoleAssignment(
        resourceId: String,
        subSysDictCode: String,
        tenantId: String?
    ): List<ResourceRoleAssignmentResult> {
        val candidateRoles = rbacRoleBiz.getCandidateRoles(subSysDictCode, tenantId)
        val assignedRoles = rbacRoleBiz.getAssignedRoles(resourceId)
        return candidateRoles.map {
            ResourceRoleAssignmentResult().apply {
                roleId = it.key
                roleName = it.value
                assigned = assignedRoles.contains(roleId)
            }
        }
    }

    /**
     * 为资源分配角色
     *
     * @param payload 角色关联用户载体，当关联的用户id列表为空时相当于解除所有关联用户
     * @return 是否分配成功
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/assignRoleForResource")
    fun assignRoleForResource(@RequestBody @Valid payload: ResourceRoleAssignmentPayload): Boolean {
        return rbacRoleBiz.assignRoleForResource(payload.resourceId!!, payload.roleIds!!)
    }

}