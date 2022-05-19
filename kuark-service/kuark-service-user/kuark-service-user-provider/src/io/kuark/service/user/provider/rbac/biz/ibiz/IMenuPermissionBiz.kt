package io.kuark.service.user.provider.rbac.biz.ibiz

import io.kuark.service.user.common.user.vo.menupermission.MenuPermissionSearchPayload
import io.kuark.service.user.common.user.vo.menupermission.MenuPermissionTreeNode


interface IMenuPermissionBiz {

    fun searchTree(payload: MenuPermissionSearchPayload): List<MenuPermissionTreeNode>

}