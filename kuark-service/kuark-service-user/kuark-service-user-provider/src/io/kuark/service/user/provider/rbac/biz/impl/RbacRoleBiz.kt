package io.kuark.service.user.provider.rbac.biz.impl

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.bean.BeanKit
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import io.kuark.base.query.Criteria
import io.kuark.base.query.Criterion
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.user.common.rbac.vo.role.RbacRoleCacheItem
import io.kuark.service.user.common.user.vo.account.UserAccountRecord
import io.kuark.service.user.common.user.vo.account.UserAccountSearchPayload
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacRoleBiz
import io.kuark.service.user.provider.rbac.cache.RoleCacheManager
import io.kuark.service.user.provider.rbac.cache.RoleIdCacheManager
import io.kuark.service.user.provider.rbac.dao.RbacRoleDao
import io.kuark.service.user.provider.rbac.dao.RbacRoleResourceDao
import io.kuark.service.user.provider.rbac.dao.RbacRoleUserDao
import io.kuark.service.user.provider.rbac.model.po.RbacRole
import io.kuark.service.user.provider.rbac.model.po.RbacRoleResource
import io.kuark.service.user.provider.rbac.model.po.RbacRoleUser
import io.kuark.service.user.provider.rbac.model.table.RbacRoleResources
import io.kuark.service.user.provider.rbac.model.table.RbacRoleUsers
import io.kuark.service.user.provider.user.biz.ibiz.IUserAccountBiz
import io.kuark.service.user.provider.user.model.po.UserAccount
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


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
    private lateinit var roleCacheManager: RoleCacheManager

    @Autowired
    private lateinit var roleIdCacheManager: RoleIdCacheManager


    override fun getRoleFromCache(roleId: String): RbacRoleCacheItem? {
        return roleCacheManager.getRoleFromCache(roleId)
    }

    override fun getRolesFromCache(roleIds: Collection<String>): Map<String, RbacRoleCacheItem> {
        return roleCacheManager.getRolesFromCache(roleIds)
    }

    override fun getRoleIdsFromCache(subSysDictCode: String, tenantId: String?): List<String> {
        return roleIdCacheManager.getRoleIdsFromCache(subSysDictCode, tenantId)
    }

    override fun getRolesFromCache(subSysDictCode: String, tenantId: String?): Map<String, RbacRoleCacheItem> {
        val roleIds = roleIdCacheManager.getRoleIdsFromCache(subSysDictCode, tenantId)
        return if (roleIds.isNotEmpty()) {
            roleCacheManager.getRolesFromCache(roleIds)
        } else {
            emptyMap()
        }
    }

    @Transactional
    override fun insert(any: Any): String {
        val id = super.insert(any)
        log.debug("新增id为${id}的角色。")
        // 同步缓存
        roleCacheManager.syncOnInsert(id)
        roleIdCacheManager.syncOnInsert(id)
        return id
    }

    @Transactional
    override fun update(any: Any): Boolean {
        val success = super.update(any)
        val id = BeanKit.getProperty(any, RbacRole::id.name) as String
        if (success) {
            log.debug("更新id为${id}的角色。")
            roleCacheManager.syncOnUpdate(id) // 同步缓存
        } else {
            log.error("更新id为${id}的角色失败！")
        }
        return success
    }

    @Transactional
    override fun deleteById(id: String): Boolean {
        val role = roleCacheManager.getRoleFromCache(id)!!
        val success = super.deleteById(id)
        if (success) {
            log.debug("删除id为${id}的角色成功！")
            // 同步缓存
            roleCacheManager.syncOnDelete(id)
            roleIdCacheManager.syncOnDelete(role)
        } else {
            log.error("删除id为${id}的角色失败！")
        }
        return success
    }

    @Transactional
    override fun batchDelete(ids: Collection<String>): Int {
        val roleMap = roleCacheManager.getRolesFromCache(ids)
        val count = super.batchDelete(ids)
        log.debug("批量删除角色，期望删除${ids.size}条，实际删除${count}条。")
        // 同步缓存
        roleCacheManager.synchOnBatchDelete(ids)
        roleIdCacheManager.synchOnBatchDelete(ids, roleMap)
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
            roleCacheManager.syncOnUpdateActive(roleId, active)
            roleIdCacheManager.syncOnUpdateActive(roleId)
        } else {
            log.error("更新id为${roleId}的角色的启用状态为${active}失败！")
        }
        return success
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun getRolePermissions(roleId: String, subSysDictCode: String,resourceType: ResourceType): List<SysResourceCacheItem> {
        val resourceIds = rbacRoleResourceDao.oneSearchProperty(
            RbacRoleResources.roleId.name, roleId, RbacRoleResources.resourceId.name
        ) as List<String>
        if (resourceIds.isNotEmpty()) {
            return resourceApi.getResources(subSysDictCode, resourceType, *resourceIds.toTypedArray())
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
        return success
    }

    @Transactional
    override fun assignUser(roleId: String, userIds: Collection<String>): Boolean {
        var success = rbacRoleUserDao.batchDeleteCriteria(Criteria(RbacRoleUsers.roleId.name, Operator.EQ, roleId)) != 0
        if (userIds.isNotEmpty()) {
            val roleUsers = userIds.map { RbacRoleUser { this.roleId = roleId; userId = it } }
            success = rbacRoleUserDao.batchInsert(roleUsers) == userIds.size
        }
        return success
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun getAssignedUsers(roleId: String): Set<String> {
        val userIds = rbacRoleUserDao.oneSearchProperty(RbacRoleUser::roleId.name, roleId, RbacRoleUser::userId.name)
        return userIds.toSet() as Set<String>
    }

    override fun getCandidateUsers(subSysDictCode: String, tenantId: String?): LinkedHashMap<String, String> {
        val accounts = userAccountBiz.getAccounts(subSysDictCode, tenantId)
        val map = linkedMapOf<String, String>()
        accounts.forEach { map[it.id!!] = it.username!! }
        return map
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun getMenuPermissions(roleId: String): Pair<List<BaseMenuTreeNode>, List<String>> {
        val role = dao.get(roleId) ?: throw ObjectNotFoundException("找不到id为${roleId}的角色信息")
        val simpleMenus = resourceApi.getSimpleMenus(role.subSysDictCode ?: "")
        val resourceIds = rbacRoleResourceDao.oneSearchProperty(
            RbacRoleResources.roleId.name, roleId, RbacRoleResources.resourceId.name
        ) as List<String>
        return Pair(simpleMenus, resourceIds)
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun searchAssignedUsers(
        searchPayload: UserAccountSearchPayload, userIds: List<String>?
    ): List<UserAccountRecord> {
        var ids = userIds
        if (ids == null) {
            val roleId = searchPayload._roleId
            require(StringKit.isNotBlank(roleId)) { "角色id必须指定！" }
            ids = getAssignedUsers(roleId!!).toList()
        }
        return if (ids.isNotEmpty()) {
            searchPayload.criterions = listOf(Criterion(UserAccount::id.name, Operator.IN, ids))
            searchPayload._roleId = null
            userAccountBiz.search(searchPayload) as List<UserAccountRecord>
        } else emptyList()
    }

    //endregion your codes 2

}