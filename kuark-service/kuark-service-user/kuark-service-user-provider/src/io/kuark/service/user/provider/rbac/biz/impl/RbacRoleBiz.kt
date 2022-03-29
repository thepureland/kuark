package io.kuark.service.user.provider.rbac.biz.impl

import io.kuark.ability.cache.support.CacheNames
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
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.sys.common.vo.resource.SysResourceDetail
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
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
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
open class RbacRoleBiz : IRbacRoleBiz, InitializingBean, BaseCrudBiz<String, RbacRole, RbacRoleDao>() {
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
    private lateinit var self: IRbacRoleBiz // 由于缓存注解的底层实现为AOP，本类间方法必须通过Bean调用，否则缓存操作不生效

    override fun afterPropertiesSet() {
        cacheAllActiveRoles()
    }

    /**
     * 缓存所有启用状态的角色信息。
     * 如果缓存未开启，什么也不做。
     *
     * @author K
     * @since 1.0.0
     */
    protected fun cacheAllActiveRoles() {
        if (!CacheKit.isCacheActive()) {
            log.info("缓存未开启，不加载和缓存所有启用状态的角色！")
            return
        }

        // 加载所有可用的角色
        val searchPayload = RbacRoleSearchPayload().apply {
            active = true
            returnEntityClass = RbacRoleDetail::class
        }

        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val roles = dao.search(searchPayload) as List<RbacRoleDetail>
        log.debug("从数据库加载了${roles.size}条角色信息。")

        // 缓存角色
        roles.forEach {
            CacheKit.putIfAbsent(CacheNames.RBAC_ROLE, it.id!!, it)
        }
        log.debug("缓存了${roles.size}条角色信息。")

        // 缓存角色id
        val map = mutableMapOf<String, MutableList<String>>()
        roles.forEach {
            val key = "${it.subSysDictCode}:${it.tenantId}"
            var roleIds = map[key]
            if (roleIds == null) {
                roleIds = mutableListOf()
                map[key] = roleIds
            }
            roleIds.add(it.id!!)
        }
        map.forEach { (key, value) ->
            CacheKit.putIfAbsent(CacheNames.RBAC_ROLE, key, value)
            log.debug("缓存了key为${key}的${value.size}条角色id。")
        }
    }

    @Cacheable(
        cacheNames = [CacheNames.RBAC_ROLE],
        key = "#roleId",
        unless = "#result == null"
    )
    override fun getRoleFromCache(roleId: String): RbacRoleDetail? {
        require(roleId.isNotBlank()) { log.error("从缓存中获取角色时必须指定角色id！") }
        if (CacheKit.isCacheActive()) log.debug("缓存中不存在id为${roleId}的角色，从数据库中加载...")
        var result = dao.get(roleId, RbacRoleDetail::class)
        if (result == null) {
            log.warn("数据库中不存在id为${roleId}的角色！")
        } else {
            log.debug("从数据库中加载id为${roleId}的角色成功。")
            if (result.active == true) {
                if (CacheKit.isCacheActive()) log.debug("缓存从数据库加载的角色。")
            } else {
                log.debug("从数据库中加载id为${roleId}的角色为未启用状态，返回null，且不缓存。")
                result = null
            }
        }
        return result
    }

    @BatchCacheable(
        cacheNames = [CacheNames.RBAC_ROLE],
        valueClass = RbacRoleDetail::class
    )
    override fun getRolesFromCache(roleIds: Collection<String>): Map<String, RbacRoleDetail> {
        require(roleIds.isNotEmpty()) { log.error("批量从缓存中获取角色时，角色id集合不能为空！") }

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

    @Cacheable(
        cacheNames = [CacheNames.RBAC_ROLE_ID],
        key = "#subSysDictCode.concat(':').concat(#tenantId)",
        unless = "#result == null || #result.size() == 0"
    )
    override fun getRoleIdsFromCache(subSysDictCode: String, tenantId: String?): List<String> {
        require(subSysDictCode.isNotBlank()) { log.error("从缓存中获取角色id时，必须指定子系统代码！") }
        if (CacheKit.isCacheActive()) {
            log.debug("缓存中不存在子系统为${subSysDictCode}且租户id为${tenantId}的角色id，从数据库中加载...")
        }
        val criteria = Criteria(RbacRole::subSysDictCode.name, Operator.EQ, subSysDictCode)
        criteria.addAnd(RbacRole::active.name, Operator.EQ, true)
        if (StringKit.isNotBlank(tenantId)) {
            criteria.addAnd(RbacRole::tenantId.name, Operator.EQ, tenantId)
        }
        @Suppress(Consts.Suppress.UNCHECKED_CAST)
        val roleIds = dao.searchProperty(criteria, RbacRole::id.name) as List<String>
        log.debug("从数据库中加载到的角色id数量为${roleIds.size}。")
        return roleIds
    }

    override fun getRolesFromCache(subSysDictCode: String, tenantId: String?): Map<String, RbacRoleDetail> {
        val roleIds = self.getRoleIdsFromCache(subSysDictCode, tenantId)
        return if (roleIds.isNotEmpty()) {
            self.getRolesFromCache(roleIds)
        } else {
            emptyMap()
        }
    }

    @Transactional
    override fun insert(any: Any): String {
        val id = super.insert(any)
        log.debug("新增id为${id}的角色。")
        // 同步缓存
        if (CacheKit.isCacheActive()) {
            log.debug("新增id为${id}的角色后，同步缓存...")
            val role = self.getRoleFromCache(id)!! // 缓存角色
            CacheKit.evict(CacheNames.RBAC_ROLE_ID, "${role.subSysDictCode}:${role.tenantId}") // 踢除角色id的缓存
            self.getRolesFromCache(role.subSysDictCode!!, role.tenantId)  // 缓存角色id
            log.debug("缓存同步完成。")
        }
        return id
    }

    @Transactional
    override fun update(any: Any): Boolean {
        val success = super.update(any)
        val id = BeanKit.getProperty(any, RbacRole::id.name) as String
        if (success) {
            log.debug("更新id为${id}的角色。")
            if (CacheKit.isCacheActive()) {
                log.debug("更新id为${id}的角色后，同步缓存...")
                CacheKit.evict(CacheNames.RBAC_ROLE, id) // 踢除角色缓存
                self.getRoleFromCache(id) // 缓存角色
                log.debug("缓存同步完成。")
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
                log.debug("删除id为${id}的角色后，同步从缓存中踢除...")
                val role = self.getRoleFromCache(id)!!
                CacheKit.evict(CacheNames.RBAC_ROLE, id) // 踢除缓存
                CacheKit.evict(CacheNames.RBAC_ROLE_ID, "${role.subSysDictCode}:${role.tenantId}") // 踢除角色id的缓存
                self.getRolesFromCache(role.subSysDictCode!!, role.tenantId)  // 缓存角色id
                log.debug("缓存同步完成。")
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
            log.debug("批量删除id为${ids}的角色后，同步从缓存中踢除...")
            val roleMap = self.getRolesFromCache(ids)
            val keys = roleMap.map { "${it.value.subSysDictCode}:${it.value.tenantId}" }.toSet()
            keys.forEach {
                CacheKit.evict(CacheNames.RBAC_ROLE, it) // 踢除角色id缓存
            }
            ids.forEach {
                CacheKit.evict(CacheNames.RBAC_ROLE, it) // 踢除角色缓存
            }
            log.debug("缓存同步完成。")
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
                val r = self.getRoleFromCache(roleId)!!
                if (!active) {
                    CacheKit.evict(CacheNames.RBAC_ROLE, roleId) // 踢除角色缓存
                }
                CacheKit.evict(CacheNames.RBAC_ROLE_ID, "${r.subSysDictCode}:${r.tenantId}") // 踢除角色id的缓存
                self.getRolesFromCache(r.subSysDictCode!!, r.tenantId)  // 缓存角色id
                log.debug("缓存同步完成。")
            }
        } else {
            log.error("更新id为${roleId}的角色的启用状态为${active}失败！")
        }
        return success
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun getRolePermissions(roleId: String, subSysDictCode: String,resourceType: ResourceType): List<SysResourceDetail> {
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