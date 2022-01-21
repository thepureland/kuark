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
        rbacRoleResourceDao.batchDeleteCriteria(Criteria(RbacRoleResources.roleId.name, Operator.EQ, roleId))
        val roleResources = resourceIds.map { RbacRoleResource { this.roleId = roleId; resourceId = it } }
        return batchInsert(roleResources) == resourceIds.size
    }

    @Transactional
    override fun assignUser(roleId: String, userIds: Collection<String>): Boolean {
        rbacRoleUserDao.batchDeleteCriteria(Criteria(RbacRoleUsers.roleId.name, Operator.EQ, roleId))
        val roleUsers = userIds.map { RbacRoleUser { this.roleId = roleId; userId = it } }
        return batchInsert(roleUsers) == userIds.size
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