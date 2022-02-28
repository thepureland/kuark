package io.kuark.service.user.provider.rbac.biz.impl

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.error.ObjectNotFoundException
import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.Consts
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.sys.common.vo.resource.SysResourceRecord
import io.kuark.service.user.provider.rbac.dao.RbacRoleDao
import io.kuark.service.user.provider.rbac.dao.RbacRoleResourceDao
import io.kuark.service.user.provider.rbac.dao.RbacRoleUserDao
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacRoleBiz
import io.kuark.service.user.provider.rbac.model.po.RbacRole
import io.kuark.service.user.provider.rbac.model.po.RbacRoleResource
import io.kuark.service.user.provider.rbac.model.po.RbacRoleUser
import io.kuark.service.user.provider.rbac.model.table.RbacRoleResources
import io.kuark.service.user.provider.rbac.model.table.RbacRoleUsers
import io.kuark.service.user.provider.user.biz.ibiz.IUserAccountBiz
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

    @Autowired
    private lateinit var resourceApi: ISysResourceApi

    @Autowired
    private lateinit var rbacRoleResourceDao: RbacRoleResourceDao

    @Autowired
    private lateinit var rbacRoleUserDao: RbacRoleUserDao

    @Autowired
    private lateinit var userAccountBiz: IUserAccountBiz

    //region your codes 2

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun getRolePermissions(roleId: String, resourceType: ResourceType): List<SysResourceRecord> {
        val resourceIds = rbacRoleResourceDao.oneSearchProperty(
            RbacRoleResources.roleId.name, roleId, RbacRoleResources.resourceId.name
        ) as List<String>
        if (resourceIds.isNotEmpty()) {
            return resourceApi.getResources(*resourceIds.toTypedArray())
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

    //endregion your codes 2

}