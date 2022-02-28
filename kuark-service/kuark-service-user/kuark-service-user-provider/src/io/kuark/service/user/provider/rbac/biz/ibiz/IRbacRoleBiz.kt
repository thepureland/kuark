package io.kuark.service.user.provider.rbac.biz.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.sys.common.vo.resource.SysResourceRecord
import io.kuark.service.user.provider.rbac.model.po.RbacRole

/**
 * 角色业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface IRbacRoleBiz: IBaseCrudBiz<String, RbacRole> {
//endregion your codes 1

    //region your codes 2

    /**
     * 返回角色可操作的资源
     *
     * @param roleId 角色ID
     * @param resourceType 资源类型
     * @return List(资源对象)
     * @author K
     * @since 1.0.0
     */
    fun getRolePermissions(roleId: String, resourceType: ResourceType): List<SysResourceRecord>

    /**
     * 设置角色可操作的资源
     *
     * @param roleId 角色ID
     * @param resourceIds 资源id集合，为空时相当于解除所有关联资源
     * @return 是否设置成功
     * @author K
     * @since 1.0.0
     */
    fun setRolePermissions(roleId: String, resourceIds: Collection<String>): Boolean

    /**
     * 为角色分配用户
     *
     * @param roleId 角色ID
     * @param userIds 用户id集合，为空时相当于解除所有关联用户
     * @return 是否分配成功
     * @author K
     * @since 1.0.0
     */
    fun assignUser(roleId: String, userIds: Collection<String>): Boolean

    /**
     * 返回已关联的用户的id
     *
     * @param roleId 角色id
     * @return Set(用户id)
     * @author K
     * @since 1.0.0
     */
    fun getAssignedUsers(roleId: String): Set<String>

    /**
     * 返回候选的用户
     *
     * @param subSysDictCode 子系统代码
     * @param tenantId 租户id
     * @return LinkedHashMap(用户id， 用户名)
     * @author K
     * @since 1.0.0
     */
    fun getCandidateUsers(subSysDictCode: String, tenantId: String?): LinkedHashMap<String, String>

    /**
     * 返回角色最大权限的菜单和其当前已分配权限的菜单ID
     *
     * @param roleId 角色ID
     * @return Pair(最大权限的菜单列表，已分配权限的菜单ID列表)
     */
    fun getMenuPermissions(roleId: String): Pair<List<BaseMenuTreeNode>, List<String>>

    //endregion your codes 2

}