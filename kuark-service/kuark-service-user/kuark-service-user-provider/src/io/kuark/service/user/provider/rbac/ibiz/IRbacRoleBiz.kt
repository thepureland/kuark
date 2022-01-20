package io.kuark.service.user.provider.rbac.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
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
     * @param resourceIds 资源id集合
     * @return 是否设置成功
     * @author K
     * @since 1.0.0
     */
    fun setRolePermissions(roleId: String, resourceIds: Collection<String>): Boolean

    /**
     * 为角色分配用户
     *
     * @param roleId 角色ID
     * @param userIds 用户id集合
     * @return 是否分配成功
     * @author K
     * @since 1.0.0
     */
    fun assignUser(roleId: String, userIds: Collection<String>): Boolean

    //endregion your codes 2

}