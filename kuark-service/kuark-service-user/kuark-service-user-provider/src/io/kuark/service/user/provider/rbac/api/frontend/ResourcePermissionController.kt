package io.kuark.service.user.provider.rbac.api.frontend

import io.kuark.service.user.common.user.vo.menupermission.ResourcePermissionSearchPayload
import io.kuark.service.user.common.user.vo.menupermission.MenuPermissionTreeNode
import io.kuark.service.user.provider.rbac.biz.ibiz.IResourcePermissionBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/rbac/resourcepermission")
open class ResourcePermissionController {

    @Autowired
    private lateinit var resourcePermissionBiz: IResourcePermissionBiz

    @PostMapping("/searchTree")
    fun searchTree(@RequestBody payload: ResourcePermissionSearchPayload): List<MenuPermissionTreeNode> {
        return resourcePermissionBiz.searchTree(payload)
    }

}