package io.kuark.service.user.provider.rbac.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.service.user.provider.rbac.model.po.RbacUserGroup

/**
 * 用户组业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface IRbacUserGroupBiz: IBaseCrudBiz<String, RbacUserGroup> {
//endregion your codes 1

    //region your codes 2

    /**
     * 返回组可操作的资源
     *
     * @param groupCode 组编码
     * @param resourceType 资源类型
     * @return List(资源对象)
     * @author K
     * @since 1.0.0
     */
    fun getGroupPermissions(groupCode: String, resourceType: io.kuark.service.sys.common.vo.resource.ResourceType): List<io.kuark.service.sys.common.vo.resource.SysResourceRecord>

    /**
     * 设置组可操作的资源
     *
     * @param groupCode 组编码
     * @param resourceIds 资源id列表
     * @return 是否设置成功
     * @author K
     * @since 1.0.0
     */
    fun setGroupPermissions(groupCode: String, resourceIds: List<String>): Boolean

    //endregion your codes 2

}