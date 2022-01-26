package io.kuark.service.user.provider.rbac.api.frontend

import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.user.common.rbac.vo.group.RbacUserGroupDetail
import io.kuark.service.user.common.rbac.vo.group.RbacUserGroupPayload
import io.kuark.service.user.common.rbac.vo.group.RbacUserGroupRecord
import io.kuark.service.user.common.rbac.vo.group.RbacUserGroupSearchPayload
import io.kuark.service.user.provider.rbac.biz.ibiz.IRbacUserGroupBiz
import io.kuark.service.user.provider.rbac.model.po.RbacRole
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rbac/group")
@CrossOrigin
open class RbacUserGroupController :
    BaseCrudController<String, IRbacUserGroupBiz, RbacUserGroupSearchPayload, RbacUserGroupRecord, RbacUserGroupDetail, RbacUserGroupPayload>() {

    @GetMapping("/updateActive")
    fun updateActive(id: String, active: Boolean): Boolean {
        val param = RbacRole {
            this.id = id
            this.active = active
        }
        return biz.update(param)
    }

}