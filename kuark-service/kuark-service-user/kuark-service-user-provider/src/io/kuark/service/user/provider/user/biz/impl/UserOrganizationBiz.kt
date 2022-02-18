package io.kuark.service.user.provider.user.biz.impl

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.lang.string.StringKit
import io.kuark.base.query.sort.Direction
import io.kuark.base.support.Consts
import io.kuark.base.tree.TreeKit
import io.kuark.service.sys.common.api.ISysTenantApi
import io.kuark.service.user.common.user.vo.organization.BaseOrganizationTreeNode
import io.kuark.service.user.common.user.vo.organization.OrganizationTreeNode
import io.kuark.service.user.common.user.vo.organization.UserOrganizationSearchPayload
import io.kuark.service.user.provider.user.biz.ibiz.IUserOrganizationBiz
import io.kuark.service.user.provider.user.dao.UserOrganizationDao
import io.kuark.service.user.provider.user.model.po.UserOrganization
import io.kuark.service.user.provider.user.model.table.UserOrganizations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * 组织机构业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class UserOrganizationBiz : BaseCrudBiz<String, UserOrganization, UserOrganizationDao>(), IUserOrganizationBiz {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var sysTenantApi: ISysTenantApi

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun searchTree(searchPayload: UserOrganizationSearchPayload): List<OrganizationTreeNode> {
        searchPayload.returnEntityClass = OrganizationTreeNode::class
        if (searchPayload.active == false) { // 这里的active实为activeOnly
            searchPayload.active = null
        }
        val organizations = dao.search(searchPayload) as List<OrganizationTreeNode>
        return TreeKit.convertListToTree(organizations, Direction.ASC)
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun lazyLoadTree(searchPayload: UserOrganizationSearchPayload): List<BaseOrganizationTreeNode> {
        if (StringKit.isBlank(searchPayload.subSysDictCode)) {
            throw IllegalArgumentException("子系统未指定！")
        }

        val result = if (StringKit.isBlank(searchPayload.tenantId)) { // 加载租户
            val tenants = sysTenantApi.getTenants(searchPayload.subSysDictCode!!)
            if (tenants.isEmpty()) { // 该子系统没有租户，直接加载组织机构
                searchPayload.returnEntityClass = BaseOrganizationTreeNode::class
                searchPayload.active = true
                searchPayload.nullProperties = listOf(UserOrganizations.parentId.name)
                dao.search(searchPayload)
            } else { // 该子系统有租户，返回租户列表
                tenants.map {
                    BaseOrganizationTreeNode().apply {
                        this.id = it.id
                        this.name = it.name
                        this.organization = false
                    }
                }
            }
        } else { // 加载组织机构
            searchPayload.returnEntityClass = BaseOrganizationTreeNode::class
            searchPayload.active = true
            if (StringKit.isBlank(searchPayload.parentId)) { // 加载租户下的组织机构
                searchPayload.nullProperties = listOf(UserOrganizations.parentId.name)
            } else { // 加载组织机构的下级机构
                // do nothing
            }
            dao.search(searchPayload)
        }
        return result as List<BaseOrganizationTreeNode>
    }

    //endregion your codes 2

}