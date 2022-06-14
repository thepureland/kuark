package io.kuark.service.user.provider.rbac.biz.ibiz

import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.SysResourceSearchPayload
import io.kuark.service.user.common.user.vo.resourcepermission.ResourcePermissionSearchPayload
import io.kuark.service.user.common.user.vo.resourcepermission.ResourcePermissionRecord


interface IResourcePermissionBiz {

    fun search(payload: ResourcePermissionSearchPayload): List<ResourcePermissionRecord>

    fun loadDirectChildrenMenuForUser(userId: String, searchPayload: SysResourceSearchPayload): List<BaseMenuTreeNode>

}