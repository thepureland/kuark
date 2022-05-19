package io.kuark.service.user.provider.rbac.biz.impl

import io.kuark.base.query.sort.Direction
import io.kuark.base.tree.TreeKit
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.user.common.user.vo.menupermission.MenuPermissionSearchPayload
import io.kuark.service.user.common.user.vo.menupermission.MenuPermissionTreeNode
import io.kuark.service.user.provider.rbac.biz.ibiz.IMenuPermissionBiz
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacRoleBiz
import io.kuark.service.user.provider.rbac.cache.RoleIdsByResourceIdCacheHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
open class MenuPermissionBiz : IMenuPermissionBiz {

    @Autowired
    private lateinit var sysResourceApi: ISysResourceApi

    @Autowired
    private lateinit var rbacRoleBiz: IRbacRoleBiz

    @Autowired
    private lateinit var roleIdsByResourceIdCacheHandler : RoleIdsByResourceIdCacheHandler


    override fun searchTree(payload: MenuPermissionSearchPayload): List<MenuPermissionTreeNode> {
        val subSysDictCode = payload.subSysDictCode!!
        val tenantId = payload.tenantId
        val menuCacheItems = sysResourceApi.getResources(subSysDictCode, ResourceType.MENU)
        val menuMap = menuCacheItems.associateBy { it.id!! }
        val resIds = menuCacheItems.map { it.id!! }
        val allRoleIds = roleIdsByResourceIdCacheHandler.getRoleIdsByResourceIds(resIds)
        val tenantRoleIds = rbacRoleBiz.getRoleIds(subSysDictCode, tenantId)
        val treeNodes = mutableListOf<MenuPermissionTreeNode>()
        allRoleIds.forEach { (resId, roleIds) ->
            val resCacheItem = menuMap[resId]!!
            val assignedRoleIds = roleIds.intersect(tenantRoleIds)
            val roles = rbacRoleBiz.getRoles(assignedRoleIds).values
            val roleNames = roles.map { it.roleName!! }
            val treeNode = MenuPermissionTreeNode().apply {
                id = resCacheItem.id
                name = resCacheItem.name
                url = resCacheItem.url
                this.roleNames = roleNames.joinToString()
                parentId = resCacheItem.parentId
                seqNo = resCacheItem.seqNo
            }
            treeNodes.add(treeNode)
        }
        return TreeKit.convertListToTree(treeNodes, Direction.ASC)
    }

}