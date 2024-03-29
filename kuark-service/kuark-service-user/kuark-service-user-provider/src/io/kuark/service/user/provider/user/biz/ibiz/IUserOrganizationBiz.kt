package io.kuark.service.user.provider.user.biz.ibiz

import io.kuark.base.support.biz.IBaseCrudBiz
import io.kuark.service.user.common.user.vo.organization.BaseOrganizationTreeNode
import io.kuark.service.user.common.user.vo.organization.OrganizationTreeNode
import io.kuark.service.user.common.user.vo.organization.UserOrganizationSearchPayload
import io.kuark.service.user.provider.user.model.po.UserOrganization
import org.springframework.web.bind.annotation.RequestBody

/**
 * 组织机构业务接口
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface IUserOrganizationBiz: IBaseCrudBiz<String, UserOrganization> {
//endregion your codes 1

    //region your codes 2

    /**
     * 查询组织机构，返回树结点，不分页
     *
     * @param searchPayload 查询载体
     * @return List(组织机构树结点)
     * @author K
     * @since 1.0.0
     */
    fun searchTree(searchPayload: UserOrganizationSearchPayload): List<OrganizationTreeNode>

    /**
     * 懒加载组织机构，返回基础树结点
     *
     * @param searchPayload 查询载体
     * @return List(组织机构基础树结点)
     * @author K
     * @since 1.0.0
     */
    fun lazyLoadTree(searchPayload: UserOrganizationSearchPayload): List<BaseOrganizationTreeNode>

    /**
     * 加载组织机构，返回基础树结点
     *
     * @param subSysDictCode 子系统代码
     * @param tenantId 租户id
     * @return List(组织机构基础树结点)
     * @author K
     * @since 1.0.0
     */
    fun loadTree(subSysDictCode: String, tenantId: String?): List<BaseOrganizationTreeNode>

    fun fetchAllParentIds(orgId: String): List<String>

    fun cascadeDeleteChildren(id: String): Boolean

    //endregion your codes 2

}