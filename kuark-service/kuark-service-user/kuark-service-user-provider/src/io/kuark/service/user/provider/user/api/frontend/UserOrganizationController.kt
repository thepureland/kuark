package io.kuark.service.user.provider.user.api.frontend

import io.kuark.ability.web.springmvc.BaseCrudController
import io.kuark.service.user.common.user.vo.organization.*
import io.kuark.service.user.provider.user.biz.ibiz.IUserOrganizationBiz
import org.springframework.web.bind.annotation.*

/**
 * 组织机构控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
@RequestMapping("/user/organization")
@CrossOrigin
//region your codes 1
class UserOrganizationController :
    BaseCrudController<String, IUserOrganizationBiz, UserOrganizationSearchPayload, UserOrganizationDetail, UserOrganizationDetail, UserOrganizationPayload>() {
//endregion your codes 1

    //region your codes 2

    @PostMapping("/searchTree")
    fun searchTree(@RequestBody payload: UserOrganizationSearchPayload): List<OrganizationTreeNode> {
        return biz.searchTree(payload)
    }

    @PostMapping("/lazyLoadTree")
    fun lazyLoadTree(@RequestBody payload: UserOrganizationSearchPayload): List<BaseOrganizationTreeNode> {
        return biz.lazyLoadTree(payload)
    }

    //endregion your codes 2

}