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
    BaseCrudController<String, IUserOrganizationBiz, UserOrganizationSearchPayload, UserOrganizationRecord, UserOrganizationDetail, UserOrganizationPayload>() {
//endregion your codes 1

    //region your codes 2

    @GetMapping("/get")
    override fun get(id: String): UserOrganizationRecord {
        val record = super.get(id)
        record.parentIds = biz.fetchAllParentIds(id)
        return record
    }

    @PostMapping("/searchTree")
    fun searchTree(@RequestBody payload: UserOrganizationSearchPayload): List<OrganizationTreeNode> {
        return biz.searchTree(payload)
    }

    @PostMapping("/lazyLoadTree")
    fun lazyLoadTree(@RequestBody payload: UserOrganizationSearchPayload): List<BaseOrganizationTreeNode> {
        return biz.lazyLoadTree(payload)
    }

    /**
     * 加载组织机构，返回基础树结点
     *
     * @param subSysDictCode 子系统代码
     * @param tenantId 租户id
     * @return List(组织机构基础树结点)
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/loadTree")
    fun loadTree(
        @RequestParam("subSysDictCode") subSysDictCode: String,
        @RequestParam("tenantId") tenantId: String?
    ): List<BaseOrganizationTreeNode> {
        return biz.loadTree(subSysDictCode, tenantId)
    }

    //endregion your codes 2

}