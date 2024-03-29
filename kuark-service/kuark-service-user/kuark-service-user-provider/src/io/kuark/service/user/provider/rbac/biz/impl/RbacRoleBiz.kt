package io.kuark.service.user.provider.rbac.biz.impl

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.bean.BeanKit
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.lang.collections.CollectionKit
import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.api.ISysTenantApi
import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.user.common.rbac.vo.role.RbacRoleCacheItem
import io.kuark.service.user.common.rbac.vo.role.RbacRoleDetail
import io.kuark.service.user.common.user.vo.account.UserAccountCacheItem
import io.kuark.service.user.common.user.vo.account.UserAccountSearchPayload
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacRoleBiz
import io.kuark.service.user.provider.rbac.cache.*
import io.kuark.service.user.provider.rbac.dao.RbacRoleDao
import io.kuark.service.user.provider.rbac.dao.RbacRoleResourceDao
import io.kuark.service.user.provider.rbac.dao.RbacRoleUserDao
import io.kuark.service.user.provider.rbac.model.po.RbacRole
import io.kuark.service.user.provider.rbac.model.po.RbacRoleResource
import io.kuark.service.user.provider.rbac.model.po.RbacRoleUser
import io.kuark.service.user.provider.rbac.model.table.RbacRoleResources
import io.kuark.service.user.provider.rbac.model.table.RbacRoleUsers
import io.kuark.service.user.provider.user.biz.ibiz.IUserAccountBiz
import io.kuark.service.user.provider.user.cache.UserByIdCacheHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.reflect.KClass


/**
 * 角色服务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class RbacRoleBiz : IRbacRoleBiz, BaseCrudBiz<String, RbacRole, RbacRoleDao>() {
//endregion your codes 1

    //region your codes 2

    private val log = LogFactory.getLog(this::class)

    @Autowired
    private lateinit var resourceApi: ISysResourceApi

    @Autowired
    private lateinit var rbacRoleResourceDao: RbacRoleResourceDao

    @Autowired
    private lateinit var rbacRoleUserDao: RbacRoleUserDao

    @Autowired
    private lateinit var userAccountBiz: IUserAccountBiz

    @Autowired
    private lateinit var roleCacheHandler: RoleByIdCacheHandler

    @Autowired
    private lateinit var roleIdBySubSysAndTenantIdCacheHandler: RoleIdBySubSysAndTenantIdCacheHandler

    @Autowired
    private lateinit var userIdByRoleIdCacheHandler: UserIdsByRoleIdCacheHandler

    @Autowired
    private lateinit var resourceIdsByRoleIdCacheHandler: ResourceIdsByRoleIdCacheHandler

    @Autowired
    private lateinit var roleIdsByResourceIdCacheHandler: RoleIdsByResourceIdCacheHandler

    @Autowired
    private lateinit var userByIdCacheHandler: UserByIdCacheHandler

    @Autowired
    private lateinit var roleIdsByUserIdCacheHandler: RoleIdsByUserIdCacheHandler


    @Autowired
    private lateinit var sysTenantApi: ISysTenantApi

    override fun <R : Any> get(id: String, returnType: KClass<R>): R? {
        val result = super.get(id, returnType)
        if (returnType == RbacRoleDetail::class) {
            val tenantId = (result as RbacRoleDetail).tenantId
            result.tenantName = sysTenantApi.getTenant(tenantId!!)?.name
        }
        return result
    }

    override fun getRole(roleId: String): RbacRoleCacheItem? {
        return roleCacheHandler.getRoleById(roleId)
    }

    override fun getRoles(roleIds: Collection<String>): Map<String, RbacRoleCacheItem> {
        return roleCacheHandler.getRolesByIds(roleIds)
    }

    override fun getRoleIds(subSysDictCode: String, tenantId: String?): List<String> {
        return roleIdBySubSysAndTenantIdCacheHandler.getRoleIds(subSysDictCode, tenantId)
    }

    override fun getRoleIds(userId: String): List<String> {
        return roleIdsByUserIdCacheHandler.getRoleIdsByUserId(userId)
    }

    override fun getRoles(subSysDictCode: String, tenantId: String?): Map<String, RbacRoleCacheItem> {
        val roleIds = roleIdBySubSysAndTenantIdCacheHandler.getRoleIds(subSysDictCode, tenantId)
        return if (roleIds.isNotEmpty()) {
            roleCacheHandler.getRolesByIds(roleIds)
        } else {
            emptyMap()
        }
    }

    @Transactional
    override fun insert(any: Any): String {
        val id = super.insert(any)
        log.debug("新增id为${id}的角色。")
        // 同步缓存
        roleCacheHandler.syncOnInsert(id)
        roleIdBySubSysAndTenantIdCacheHandler.syncOnInsert(id)
        return id
    }

    @Transactional
    override fun update(any: Any): Boolean {
        val success = super.update(any)
        val id = BeanKit.getProperty(any, RbacRole::id.name) as String
        if (success) {
            log.debug("更新id为${id}的角色。")
            roleCacheHandler.syncOnUpdate(id) // 同步缓存
        } else {
            log.error("更新id为${id}的角色失败！")
        }
        return success
    }

    @Transactional
    override fun deleteById(id: String): Boolean {
        val role = roleCacheHandler.getRoleById(id)!!
        val success = super.deleteById(id)
        if (success) {
            log.debug("删除id为${id}的角色成功！")
            // 同步缓存
            roleCacheHandler.syncOnDelete(id)
            roleIdBySubSysAndTenantIdCacheHandler.syncOnDelete(role)
            userIdByRoleIdCacheHandler.syncOnDelete(id)
            resourceIdsByRoleIdCacheHandler.syncOnDelete(id)
        } else {
            log.error("删除id为${id}的角色失败！")
        }
        return success
    }

    @Transactional
    override fun batchDelete(ids: Collection<String>): Int {
        val roleMap = roleCacheHandler.getRolesByIds(ids)
        val count = super.batchDelete(ids)
        log.debug("批量删除角色，期望删除${ids.size}条，实际删除${count}条。")
        // 同步缓存
        roleCacheHandler.syncOnBatchDelete(ids)
        roleIdBySubSysAndTenantIdCacheHandler.syncOnBatchDelete(ids, roleMap)
        userIdByRoleIdCacheHandler.syncOnBatchDelete(ids)
        resourceIdsByRoleIdCacheHandler.syncOnBatchDelete(ids)
        return count
    }

    @Transactional
    override fun updateActive(roleId: String, active: Boolean): Boolean {
        val role = RbacRole {
            this.id = roleId
            this.active = active
        }
        val success = dao.update(role)
        if (success) {
            log.debug("更新id为${roleId}的角色的启用状态为${active}。")
            // 同步缓存
            roleCacheHandler.syncOnUpdateActive(roleId, active)
            roleIdBySubSysAndTenantIdCacheHandler.syncOnUpdateActive(roleId)
        } else {
            log.error("更新id为${roleId}的角色的启用状态为${active}失败！")
        }
        return success
    }

    override fun getRolePermissions(
        roleId: String,
        subSysDictCode: String,
        resourceType: ResourceType
    ): List<SysResourceCacheItem> {
        val resourceIds = resourceIdsByRoleIdCacheHandler.getResourceIdsByRoleId(roleId)
        if (resourceIds.isNotEmpty()) {
            return resourceApi.getResources(resourceIds).values.filter {
                it.resourceTypeDictCode == resourceType.code && it.subSysDictCode == subSysDictCode
            }
        }
        return emptyList()
    }

    @Transactional
    override fun setRolePermissions(roleId: String, resourceIds: Collection<String>): Boolean {
        val criteria = Criteria(RbacRoleResources.roleId.name, Operator.EQ, roleId)
        var success = rbacRoleResourceDao.batchDeleteCriteria(criteria) != 0
        if (resourceIds.isNotEmpty()) {
            val roleResources = resourceIds.map { RbacRoleResource { this.roleId = roleId; resourceId = it } }
            success = rbacRoleResourceDao.batchInsert(roleResources) == resourceIds.size
        }
        resourceIdsByRoleIdCacheHandler.syncOnUpdate(roleId, resourceIds)
        resourceIds.forEach { roleIdsByResourceIdCacheHandler.syncOnUpdate(it, setOf(roleId)) }
        return success
    }

    @Transactional
    override fun assignUser(roleId: String, userIds: Collection<String>): Boolean {
        val criteria = Criteria(RbacRoleUsers.roleId.name, Operator.EQ, roleId)
        var success = rbacRoleUserDao.batchDeleteCriteria(criteria) != 0
        if (userIds.isNotEmpty()) {
            val roleUsers = userIds.map { RbacRoleUser { this.roleId = roleId; userId = it } }
            success = rbacRoleUserDao.batchInsert(roleUsers) == userIds.size
        }
        userIdByRoleIdCacheHandler.syncOnUpdate(roleId, userIds)
        val roleIds = setOf(roleId)
        userIds.forEach { roleIdsByUserIdCacheHandler.syncOnUpdate(it, roleIds) }
        return success
    }

    @Transactional
    override fun assignRoleForResource(resourceId: String, roleIds: List<String>): Boolean {
        val criteria = Criteria(RbacRoleResources.resourceId.name, Operator.EQ, resourceId)
        var success = rbacRoleResourceDao.batchDeleteCriteria(criteria) != 0
        if (roleIds.isNotEmpty()) {
            val roleResources = roleIds.map { RbacRoleResource { roleId = it; this.resourceId = resourceId } }
            success = rbacRoleResourceDao.batchInsert(roleResources) == roleIds.size
        }
        roleIdsByResourceIdCacheHandler.syncOnUpdate(resourceId, roleIds)
        val resourceIds = setOf(resourceId)
        roleIds.forEach { resourceIdsByRoleIdCacheHandler.syncOnUpdate(it, resourceIds) }
        return success
    }

    override fun getAssignedUsers(roleId: String): Set<String> {
        val userIds = userIdByRoleIdCacheHandler.getUserIdsByRoleId(roleId)
        return userIds.toSet()
    }

    override fun getAssignedRoles(resourceId: String): Set<String> {
        return roleIdsByResourceIdCacheHandler.getRoleIdsByResourceId(resourceId).toSet()
    }

    override fun getCandidateUsers(subSysDictCode: String, tenantId: String?): LinkedHashMap<String, String> {
        val accounts = userAccountBiz.getAccounts(subSysDictCode, tenantId)  //TODO from cache
        val map = linkedMapOf<String, String>()
        accounts.forEach { map[it.id!!] = it.username!! }
        return map
    }

    override fun getCandidateRoles(subSysDictCode: String, tenantId: String?): LinkedHashMap<String, String> {
        val roleIds = roleIdBySubSysAndTenantIdCacheHandler.getRoleIds(subSysDictCode, tenantId)
        val roles = roleCacheHandler.getRolesByIds(roleIds)
        val map = linkedMapOf<String, String>()
        roles.values.forEach { map[it.id!!] = it.roleName!! }
        return map
    }

    override fun getUrlAccessRoleIdsFromCache(subSysDictCode: String, url: String): Collection<String> {
        val resourceId = resourceApi.getResourceId(subSysDictCode, url) ?: return emptyList()
        return roleIdsByResourceIdCacheHandler.getRoleIdsByResourceId(resourceId)
    }

    override fun getMenuPermissions(roleId: String): Pair<List<BaseMenuTreeNode>, List<String>> {
        val role = roleCacheHandler.getRoleById(roleId) ?: throw ObjectNotFoundException("找不到id为${roleId}的角色信息")
        val simpleMenus = resourceApi.getSimpleMenus(role.subSysDictCode!!)
        val resourceIds = resourceIdsByRoleIdCacheHandler.getResourceIdsByRoleId(roleId)
        return Pair(simpleMenus, resourceIds)
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun searchAssignedUsers(
        searchPayload: UserAccountSearchPayload, userIds: List<String>?
    ): List<UserAccountCacheItem> {
        var ids = userIds
        if (ids == null) {
            val roleId = searchPayload._roleId
            require(StringKit.isNotBlank(roleId)) { "角色id必须指定！" }
            ids = getAssignedUsers(roleId!!).toList()
        }
        return if (ids.isNotEmpty()) {
            userByIdCacheHandler.getUsersByIds(ids).values.toList()
        } else emptyList()
    }

    override fun getRoleIdsForResource(resourceId: String, subSysDictCode: String, tenantId: String?): Set<String> {
        val allRoleIds = roleIdBySubSysAndTenantIdCacheHandler.getRoleIds(subSysDictCode, tenantId)
        val roleIds = roleIdsByResourceIdCacheHandler.getRoleIdsByResourceId(resourceId)
        return allRoleIds.intersect(roleIds)
    }

    @Transactional
    override fun reassignRolesForResource(
        resourceId: String,
        subSysDictCode: String,
        tenantId: String?,
        roleIds: Set<String>?
    ): Boolean {
        // 先删除已经关联的角色-资源（必须属于subSysDictCode和tenantId的）
        val allRoleIds = roleIdBySubSysAndTenantIdCacheHandler.getRoleIds(subSysDictCode, tenantId)
        val criteria = Criteria.add(RbacRoleResource::resourceId.name, Operator.EQ, resourceId)
            .addAnd(RbacRoleResource::roleId.name, Operator.IN, allRoleIds)

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val affectRoleIds = rbacRoleResourceDao.searchProperty(criteria, RbacRoleResource::roleId.name) as List<String>

        val count = rbacRoleResourceDao.batchDeleteCriteria(criteria)
        if (count > 0) {
            roleIdsByResourceIdCacheHandler.syncOnDelete(resourceId)
            resourceIdsByRoleIdCacheHandler.syncOnBatchDelete(affectRoleIds.toSet())
        }

        // 重新关联角色-资源
        return if (CollectionKit.isEmpty(roleIds)) {
            true
        } else {
            val roleResources = roleIds!!.map {
                RbacRoleResource {
                    roleId = it
                    this.resourceId = resourceId
                }
            }
            val count = rbacRoleResourceDao.batchInsert(roleResources)
            roleIdsByResourceIdCacheHandler.syncOnUpdate(resourceId, roleIds)
            // resourceIdsByRoleIdCacheHandler由于不同角色可能关联有其它资源，所以无法在这里同步，下次使用到时将自动从数据库中同步
            count == roleIds.size
        }
    }

    //endregion your codes 2

}