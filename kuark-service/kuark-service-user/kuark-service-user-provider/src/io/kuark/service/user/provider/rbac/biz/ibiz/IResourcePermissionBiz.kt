package io.kuark.service.user.provider.rbac.biz.ibiz

import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.SysResourceSearchPayload
import io.kuark.service.user.common.user.vo.resourcepermission.ResourcePermissionSearchPayload
import io.kuark.service.user.common.user.vo.resourcepermission.ResourcePermissionRecord
import io.kuark.service.user.common.user.vo.resourcepermission.ResourcePermissionTreeSearchPayload
import org.springframework.web.bind.annotation.RequestBody


interface IResourcePermissionBiz {

    fun search(payload: ResourcePermissionSearchPayload): List<ResourcePermissionRecord>

    fun searchOnClickTree(searchPayload: ResourcePermissionTreeSearchPayload): List<ResourcePermissionRecord>

    fun loadDirectChildrenMenuForUser(userId: String, searchPayload: SysResourceSearchPayload): List<BaseMenuTreeNode>

}