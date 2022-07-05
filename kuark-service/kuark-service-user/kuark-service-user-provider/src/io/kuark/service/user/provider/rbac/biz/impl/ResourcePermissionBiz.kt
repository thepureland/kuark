package io.kuark.service.user.provider.rbac.biz.impl

import io.kuark.base.lang.EnumKit
import io.kuark.base.lang.string.StringKit
import io.kuark.service.sys.common.api.ISysDictApi
import io.kuark.service.sys.common.api.ISysResourceApi
import io.kuark.service.sys.common.vo.dict.DictModuleAndTypePayload
import io.kuark.service.sys.common.vo.dict.SysResourceCacheItem
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.ResourceType
import io.kuark.service.sys.common.vo.resource.SysResourceSearchPayload
import io.kuark.service.user.common.user.vo.resourcepermission.ResourcePermissionSearchPayload
import io.kuark.service.user.common.user.vo.resourcepermission.ResourcePermissionRecord
import io.kuark.service.user.common.user.vo.resourcepermission.ResourcePermissionTreeSearchPayload
import io.kuark.service.user.provider.rbac.biz.ibiz.IResourcePermissionBiz
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacRoleBiz
import io.kuark.service.user.provider.rbac.cache.ResourceIdsByRoleIdCacheHandler
import io.kuark.service.user.provider.rbac.cache.RoleIdsByResourceIdCacheHandler
import io.kuark.service.user.provider.rbac.cache.RoleIdsByUserIdCacheHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
open class ResourcePermissionBiz : IResourcePermissionBiz {

    @Autowired
    private lateinit var sysResourceApi: ISysResourceApi

    @Autowired
    private lateinit var sysDictApi: ISysDictApi

    @Autowired
    private lateinit var rbacRoleBiz: IRbacRoleBiz

    @Autowired
    private lateinit var roleIdsByResourceIdCacheHandler: RoleIdsByResourceIdCacheHandler

    @Autowired
    private lateinit var roleIdsByUserIdCacheHandler: RoleIdsByUserIdCacheHandler

    @Autowired
    private lateinit var resourceIdsByRoleIdCacheHandler: ResourceIdsByRoleIdCacheHandler


    override fun search(payload: ResourcePermissionSearchPayload): List<ResourcePermissionRecord> {
        // 取得子系统的所有指定资源
        val subSysDictCode = payload.subSysDictCode!!
        val resourceTypeDictCode = EnumKit.enumOf(ResourceType::class, payload.resourceTypeDictCode!!)!!
        var resCacheItems = sysResourceApi.getResources(subSysDictCode, resourceTypeDictCode)
        if (StringKit.isNotBlank(payload.name)) {
            val name = payload.name!!.lowercase()
            resCacheItems = resCacheItems.filter { it.name!!.lowercase().contains(name) }
        }
        val resMap = resCacheItems.associateBy { it.id!! }
        val resIds = resCacheItems.map { it.id!! }

        return searchResPermissions(payload.subSysDictCode!!, payload.tenantId, resIds, resMap)
    }

    private fun searchResPermissions(
        subSysDictCode: String,
        tenantId: String?,
        resIds: Collection<String>,
        resMap: Map<String, SysResourceCacheItem>
    ): List<ResourcePermissionRecord> {
        // 取得子系统所有指定资源已分配的角色id
        val allRoleIds = roleIdsByResourceIdCacheHandler.getRoleIdsByResourceIds(resIds)

        // 取得租户的所有角色id
        val tenantRoleIds = rbacRoleBiz.getRoleIds(subSysDictCode, tenantId).toSet()

        val records = mutableListOf<ResourcePermissionRecord>()
        allRoleIds.forEach { (resId, roleIds) ->
            val resCacheItem = resMap[resId]!!
            val assignedRoleIds = roleIds.intersect(tenantRoleIds) // 租户下该资源已分配的角色id
            val roles = rbacRoleBiz.getRoles(assignedRoleIds).values
            val roleNames = roles.map { it.roleName!! }
            val treeNode = ResourcePermissionRecord().apply {
                id = resCacheItem.id
                name = resCacheItem.name
                url = resCacheItem.url
                this.roleNames = roleNames.joinToString()
                parentId = resCacheItem.parentId
            }
            records.add(treeNode)
        }
        return records
    }

    override fun searchOnClickTree(searchPayload: ResourcePermissionTreeSearchPayload): List<ResourcePermissionRecord> {
        val resIds = mutableSetOf<String>()
        if (searchPayload.leaf == true) {
            resIds.add(searchPayload.id!!)
        } else {
            val resourceType = EnumKit.enumOf(ResourceType::class, searchPayload.resourceTypeDictCode!!)!!
            val menus = sysResourceApi.getDirectChildrenResources(
                searchPayload.subSysDictCode!!, resourceType, searchPayload.id
            )
            resIds.addAll(menus.map { it.id!! })
        }

        val resMap = sysResourceApi.getResources(resIds)

        return searchResPermissions(searchPayload.subSysDictCode!!, searchPayload.tenantId, resIds, resMap)
    }

    override fun loadDirectChildrenMenuForUser(
        userId: String, searchPayload: SysResourceSearchPayload
    ): List<BaseMenuTreeNode> {
        return when (if (searchPayload.level == null) 0 else searchPayload.level) {
            0 -> { // 资源类型
                val dictItems = sysDictApi.getDictItems(DictModuleAndTypePayload("kuark:sys", "resource_type"))
                dictItems.map {
                    BaseMenuTreeNode().apply { this.id = it.itemCode; this.title = it.itemName }
                }
            }
            else -> { // 菜单
                val menus = sysResourceApi.getDirectChildrenResources(
                    searchPayload.subSysDictCode!!, ResourceType.MENU, searchPayload.parentId
                )
                val allResIds = menus.map { it.id!! }

                // 过滤出指定用户拥有权限的菜单id
                val resIds = mutableSetOf<String>()
                val roleIds = roleIdsByUserIdCacheHandler.getRoleIdsByUserId(userId)
                roleIds.forEach {
                    val resourceIds = resourceIdsByRoleIdCacheHandler.getResourceIdsByRoleId(it)
                    val permitResIds = allResIds.intersect(resourceIds)
                    if (permitResIds.isNotEmpty()) {
                        resIds.addAll(permitResIds)
                    }
                }

                menus.filter { it.id in resIds }
                    .map { BaseMenuTreeNode().apply { this.id = it.id; this.title = it.name } }
            }
        }
    }

}