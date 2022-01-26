package io.kuark.service.user.provider.user.biz.impl

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.base.query.sort.Direction
import io.kuark.base.support.Consts
import io.kuark.base.tree.TreeKit
import io.kuark.service.user.common.user.vo.organization.BaseOrganizationTreeNode
import io.kuark.service.user.common.user.vo.organization.OrganizationTreeNode
import io.kuark.service.user.common.user.vo.organization.UserOrganizationSearchPayload
import io.kuark.service.user.provider.user.biz.ibiz.IUserOrganizationBiz
import io.kuark.service.user.provider.user.dao.UserOrganizationDao
import io.kuark.service.user.provider.user.model.po.UserOrganization
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

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun searchTree(activeOnly: Boolean?): List<OrganizationTreeNode> {
        val payload = UserOrganizationSearchPayload().apply {
            returnEntityClass = OrganizationTreeNode::class
            if (activeOnly == true) {
                active = true
            }
        }
        val organizations = dao.search(payload) as List<OrganizationTreeNode>
        return TreeKit.convertListToTree(organizations, Direction.ASC)
    }

    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    override fun loadTree(): List<BaseOrganizationTreeNode> {
        val payload = UserOrganizationSearchPayload().apply {
            returnEntityClass = BaseOrganizationTreeNode::class
            active = true
        }
        val organizations = dao.search(payload) as List<BaseOrganizationTreeNode>
        return TreeKit.convertListToTree(organizations, Direction.ASC)
    }

    //endregion your codes 2

}