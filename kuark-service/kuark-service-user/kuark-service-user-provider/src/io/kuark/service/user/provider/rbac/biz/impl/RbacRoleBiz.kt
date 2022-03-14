package io.kuark.service.user.provider.rbac.biz.impl

import io.kuark.ability.cache.context.CacheNames
import io.kuark.ability.cache.core.BatchCacheable
import io.kuark.ability.cache.kit.CacheKit
import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.bean.BeanKit
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.lang.string.StringKit
import io.kuark.base.log.LogFactory
import io.kuark.base.query.Criteria
import io.kuark.base.query.Criterion
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.context.kit.SpringKit
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.sys.common.vo.resource.SysResourceRecord
import io.kuark.service.user.common.rbac.vo.role.RbacRoleDetail
import io.kuark.service.user.common.rbac.vo.role.RbacRoleSearchPayload
import io.kuark.service.user.common.user.vo.account.UserAccountRecord
import io.kuark.service.user.common.user.vo.account.UserAccountSearchPayload
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacRoleBiz
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
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
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
@CacheConfig(cacheNames = [CacheNames.RBAC_ROLE])
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

    override fun cacheAllActiveRoles() {
        if (!CacheKit.isCacheActive()) {
            log.info("缓存未开启，不加载和缓存所有启用状态的角色！")
            return
        }

        // 加载所有可用的角色
        val searchPayload = RbacRoleSearchPayload().apply {
            active = true
            pageNo = null
            returnEntityClass = RbacRoleDetail::class
        }
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val roles = dao.search(searchPayload) as List<RbacRoleDetail>
        log.debug("从数据库加载了${roles.size}条角色信息。")

        // 清空缓存（如果有的话）
        CacheKit.clear(CacheNames.RBAC_ROLE)
        log.debug("清空所有角色缓存。")

        // 缓存角色
        roles.forEach {
            CacheKit.put(CacheNames.RBAC_ROLE, it.id!!, it)
        }
        log.debug("缓存了${roles.size}条角色信息。")
    }

    @Cacheable(key = "#roleId", unless = "#result == null")
    override fun getRoleFromCache(roleId: String): RbacRoleDetail? {
        if (CacheKit.isCacheActive()) log.debug("缓存中不存在id为${roleId}的角色，从数据库中加载...")
        val result = dao.get(roleId, RbacRoleDetail::class)
        if (result == null) {
            log.warn("数据库中不存在id为${roleId}的角色！")
        } else {
            log.debug("从数据库中加载id为${roleId}的角色成功。")
            if (CacheKit.isCacheActive()) log.debug("缓存从数据库加载的角色。")
        }
        return result
    }

    @BatchCacheable(valueClass = RbacRoleDetail::class)
    override fun getRolesFromCache(roleIds: Collection<String>): Map<String, RbacRoleDetail> {
        if (!CacheKit.isCacheActive()) {
            log.info("缓存未开启，不批量加载和缓存所有启用状态的角色！")
            return emptyMap()
        }

        // 加载所有可用的角色
        val searchPayload = RbacRoleSearchPayload().apply {
            active = true
            pageNo = null
            criterions = listOf(Criterion(RbacRole::id.name, Operator.IN, roleIds))
            returnEntityClass = RbacRoleDetail::class
        }
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val roles = dao.search(searchPayload) as List<RbacRoleDetail>
        log.debug("从数据库加载了${roles.size}条角色信息。")

        return roles.associateBy { it.id!! }
    }

    @Transactional
    override fun insert(any: Any): String {
        val id = super.insert(any)
        log.debug("新增id为${id}的角色。")
        // 同步缓存
        if (CacheKit.isCacheActive()) {
            log.debug("新增id为${id}的角色后，同步缓存...")
            SpringKit.getBean(IRbacRoleBiz::class).getRoleFromCache(id) // 由于缓存注解的底层实现为AOP，必须通过Bean调用，否则缓存操作不生效
        }
        return id
    }

    @Transactional
    override fun update(any: Any): Boolean {
        val success = super.update(any)
        val id = BeanKit.getProperty(any, "id") as String
        if (success) {
            log.debug("更新id为${id}的角色。")
            if (CacheKit.isCacheActive()) {
                log.debug("更新id为${id}的角色后，同步缓存...")
                CacheKit.evict(CacheNames.RBAC_ROLE, id) // 踢除缓存
                SpringKit.getBean(IRbacRoleBiz::class).getRoleFromCache(id) // 由于缓存注解的底层实现为AOP，必须通过Bean调用，否则缓存操作不生效
            }
        } else {
            log.error("更新id为${id}的角色失败！")
        }
        return success
    }

    @Transactional
    override fun deleteById(id: String): Boolean {
        val success = super.deleteById(id)
        if (success) {
            log.debug("删除id为${id}的角色成功！")
            if (CacheKit.isCacheActive()) {
                CacheKit.evict(CacheNames.RBAC_ROLE, id) // 踢除缓存
                log.debug("删除id为${id}的角色后，同步从缓存中踢除。")
            }
        } else {
            log.error("删除id为${id}的角色失败！")
        }
        return success
    }

    @Transactional
    override fun batchDelete(ids: Collection<String>): Int {
        val count = super.batchDelete(ids)
        log.debug("批量删除角色，期望删除${ids.size}条，实际删除${count}条。")
        if (CacheKit.isCacheActive()) {
            ids.forEach {
                CacheKit.evict(CacheNames.RBAC_ROLE, it) // 踢除缓存
            }
            log.debug("从角色缓存中，踢除以下id对应的角色：${ids}")
        }
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
            if (CacheKit.isCacheActive()) {
                log.debug("更新id为${roleId}的角色的启用状态后，同步缓存...")
                if (active) {
                    SpringKit.getBean(IRbacRoleBiz::class)
                        .getRoleFromCache(roleId) // 由于缓存注解的底层实现为AOP，必须通过Bean调用，否则缓存操作不生效
                } else {
                    CacheKit.evict(CacheNames.RBAC_ROLE, roleId) // 踢除缓存
                    log.debug("将id为${roleId}的角色从缓存中踢除。")
                }
            }
        } else {
            log.error("更新id为${roleId}的角色的启用状态为${active}失败！")
        }
        return success
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun getRolePermissions(roleId: String, resourceType: ResourceType): List<SysResourceRecord> {
        val resourceIds = rbacRoleResourceDao.oneSearchProperty(
            RbacRoleResources.roleId.name, roleId, RbacRoleResources.resourceId.name
        ) as List<String>
        if (resourceIds.isNotEmpty()) {
            return resourceApi.getResources(resourceType, *resourceIds.toTypedArray())
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