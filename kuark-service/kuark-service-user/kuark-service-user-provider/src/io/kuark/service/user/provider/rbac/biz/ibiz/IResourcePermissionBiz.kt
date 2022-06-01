package io.kuark.service.user.provider.rbac.biz.ibiz

import io.kuark.service.user.common.user.vo.menupermission.ResourcePermissionSearchPayload
import io.kuark.service.user.common.user.vo.menupermission.MenuPermissionTreeNode


interface IResourcePermissionBiz {

    fun searchTree(payload: ResourcePermissionSearchPayload): List<MenuPermissionTreeNode>

}