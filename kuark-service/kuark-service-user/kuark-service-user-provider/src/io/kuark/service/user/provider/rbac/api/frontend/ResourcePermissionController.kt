package io.kuark.service.user.provider.rbac.api.frontend

import io.kuark.context.core.KuarkContextHolder
import io.kuark.service.sys.common.vo.resource.BaseMenuTreeNode
import io.kuark.service.sys.common.vo.resource.SysResourceSearchPayload
import io.kuark.service.user.common.user.vo.resourcepermission.ResourcePermissionSearchPayload
import io.kuark.service.user.common.user.vo.resourcepermission.ResourcePermissionRecord
import io.kuark.service.user.common.user.vo.resourcepermission.ResourcePermissionTreeSearchPayload
import io.kuark.service.user.provider.rbac.biz.ibiz.IResourcePermissionBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
@RequestMapping("/rbac/resourcepermission")
open class ResourcePermissionController {

    @Autowired
    private lateinit var resourcePermissionBiz: IResourcePermissionBiz

    @PostMapping("/search")
    fun search(@RequestBody @Valid payload: ResourcePermissionSearchPayload): List<ResourcePermissionRecord> {
        return resourcePermissionBiz.search(payload)
    }

    @PostMapping("/loadDirectChildrenForTree")
    fun loadDirectChildrenForTree(@RequestBody searchPayload: SysResourceSearchPayload): List<BaseMenuTreeNode> {
        val user = KuarkContextHolder.get().user!!
        return resourcePermissionBiz.loadDirectChildrenMenuForUser(user.id!!, searchPayload)
    }

    @PostMapping("/searchOnClickTree")
    fun searchOnClickTree(@RequestBody @Valid searchPayload: ResourcePermissionTreeSearchPayload): List<ResourcePermissionRecord> {
        return resourcePermissionBiz.searchOnClickTree(searchPayload)
    }

}