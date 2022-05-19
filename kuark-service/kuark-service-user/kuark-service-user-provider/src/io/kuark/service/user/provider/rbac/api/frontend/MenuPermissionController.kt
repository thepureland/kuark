package io.kuark.service.user.provider.rbac.api.frontend

import io.kuark.service.user.common.user.vo.menupermission.MenuPermissionSearchPayload
import io.kuark.service.user.common.user.vo.menupermission.MenuPermissionTreeNode
import io.kuark.service.user.provider.rbac.biz.ibiz.IMenuPermissionBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/rbac/menuPermission")
open class MenuPermissionController {

    @Autowired
    private lateinit var menuPermissionBiz: IMenuPermissionBiz

    @PostMapping("/searchTree")
    fun searchTree(@RequestBody payload: MenuPermissionSearchPayload): List<MenuPermissionTreeNode> {
        return menuPermissionBiz.searchTree(payload)
    }

}