package io.kuark.service.user.provider.rbac.biz.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.sys.common.vo.resource.SysResourceDetail
import io.kuark.service.sys.common.vo.resource.SysResourceRecord
import io.kuark.service.user.common.rbac.vo.role.RbacRoleCacheItem
import io.kuark.service.user.common.rbac.vo.role.RbacRoleDetail
import io.kuark.service.user.common.rbac.vo.role.RoleIdAndName
import io.kuark.service.user.common.user.vo.account.UserAccountCacheItem
import io.kuark.service.user.common.user.vo.account.UserAccountRecord
import io.kuark.service.user.common.user.vo.account.UserAccountSearchPayload
import io.kuark.service.user.provider.rbac.model.po.RbacRole

/**
 * 角色业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface IRbacRoleBiz : IBaseCrudBiz<String, RbacRole> {
//endregion your codes 1

    //region your codes 2


    /**
     * 从缓存中返回指定id的角色(启用的)，如果不存在，从数据库加载，并缓存。
     * 如果缓存未开启，只加载不缓存。
     *
     * @param roleId 角色id
     * @return 角色信息，找不到返回null
     * @author K
     * @since 1.0.0
     */
    fun getRole(roleId: String): RbacRoleCacheItem?

    /**
     * 批量从缓存中返回指定id集合的角色(启用的)，如果不存在，从数据库加载，并缓存。
     * 如果缓存未开启，只加载不缓存。
     *
     * @param roleIds 角色id集合，不能为空
     * @return Map(角色id，角色信息)
     * @author K
     * @since 1.0.0
     */
    fun getRoles(roleIds: Collection<String>): Map<String, RbacRoleCacheItem>

    /**
     * 从缓存中返回指定子系统和租户的角色id(启用的)，如果不存在，从数据库加载，并缓存。
     * 如果缓存未开启，只加载不缓存。
     *
     * @param subSysDictCode 子系统代码，不能为空
     * @param tenantId 租户id，为null时表示该子系统非多租户，将返回该子系统的所有角色id。当子系统为多租户时，务必要传该参数！
     * @return List(角色id)，找不到时返回空列表且不缓存
     * @author K
     * @since 1.0.0
     */
    fun getRoleIds(subSysDictCode: String, tenantId: String?): List<String>

    /**
     * 从缓存中返回指定用户id关联的角色id，如果不存在，从数据库加载，并缓存。
     * 如果缓存未开启，只加载不缓存。
     *
     * @param userId 用户id，不能为空
     * @return List(角色id)，找不到时返回空列表且不缓存
     * @author K
     * @since 1.0.0
     */
    fun getRoleIds(userId: String): List<String>

    /**
     * 从缓存中返回指定子系统和租户的角色(启用的)，如果不存在，从数据库加载，并缓存。
     * 如果缓存未开启，只加载不缓存。
     *
     * @param subSysDictCode 子系统代码，不能为空
     * @param tenantId 租户id，为null时表示该子系统非多租户，将返回该子系统的所有角色。当子系统为多租户时，务必要传该参数！
     * @return Map(角色id，角色信息)，找不到时返回空Map且不缓存
     * @author K
     * @since 1.0.0
     */
    fun getRoles(subSysDictCode: String, tenantId: String?): Map<String, RbacRoleCacheItem>

    /**
     * 更新启用状态，并同步缓存
     *
     * @param roleId 角色id
     * @param active 是否启用
     * @return 是否更新成功
     * @author K
     * @since 1.0.0
     */
    fun updateActive(roleId: String, active: Boolean): Boolean

    /**
     * 返回角色可操作的资源
     *
     * @param roleId 角色ID
     * @param subSysDictCode 子系统代码，不能为空
     * @param resourceType 资源类型
     * @return List(资源对象)
     * @author K
     * @since 1.0.0
     */
    fun getRolePermissions(
        roleId: String,
        subSysDictCode: String,
        resourceType: ResourceType
    ): List<SysResourceCacheItem>

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
     * 为资源分配角色
     *
     * @param resourceId 资源id
     * @param roleIds 待分配的角色id列表
     * @return 是否分配成功
     * @author K
     * @since 1.0.0
     */
    fun assignRoleForResource(resourceId: String, roleIds: List<String>): Boolean

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
     * 返回指定资源已关联的角色的id
     *
     * @param resourceId 资源id
     * @return Set(角色id)
     * @author K
     * @since 1.0.0
     */
    fun getAssignedRoles(resourceId: String): Set<String>

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
     * 返回候选的角色
     *
     * @param subSysDictCode 子系统代码
     * @param tenantId 租户id
     * @return LinkedHashMap(角色id， 角色名)
     * @author K
     * @since 1.0.0
     */
    fun getCandidateRoles(subSysDictCode: String, tenantId: String?): LinkedHashMap<String, String>

    /**
     * 返回指定url可以访问的角色的id
     *
     * @param subSysDictCode 子系统代码
     * @param url URL
     * @return Collection(角色id)
     * @author K
     * @since 1.0.0
     */
    fun getUrlAccessRoleIdsFromCache(subSysDictCode: String, url: String): Collection<String>

    /**
     * 返回角色最大权限的菜单和其当前已分配权限的菜单ID
     *
     * @param roleId 角色ID
     * @return Pair(最大权限的菜单列表，已分配权限的菜单ID列表)
     * @author K
     * @since 1.0.0
     */
    fun getMenuPermissions(roleId: String): Pair<List<BaseMenuTreeNode>, List<String>>

    /**
     * 分页查询已分配的用户
     *
     * @param searchPayload 查询载体
     * @param userIds 用户id列表，为null将会根据_roleId去查询
     * @return List(UserAccountCacheItem)
     * @throws IllegalArgumentException 用户id列表且searchPayload._roleId为空时
     * @author K
     * @since 1.0.0
     */
    fun searchAssignedUsers(searchPayload: UserAccountSearchPayload, userIds: List<String>?): List<UserAccountCacheItem>

    /**
     * 返回资源所关联的角色id
     *
     * @param resourceId 资源id
     * @param subSysDictCode 子系统代码
     * @param tenantId 租户id
     * @return Set(角色id)
     * @author K
     * @since 1.0.0
     */
    fun getRoleIdsForResource(resourceId: String, subSysDictCode: String, tenantId: String?): Set<String>

    /**
     * 为资源重新关联角色id
     *
     * @param resourceId 资源id
     * @param subSysDictCode 子系统代码
     * @param tenantId 租户id
     * @param roleIds Set(角色id)
     * @return 是否关联成功
     * @author K
     * @since 1.0.0
     */
    fun reassignRolesForResource(
        resourceId: String,
        subSysDictCode: String,
        tenantId: String?,
        roleIds: Set<String>?
    ): Boolean

    //endregion your codes 2

}