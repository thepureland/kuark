package io.kuark.service.user.provider.rbac.biz

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.service.user.provider.rbac.dao.RbacUserGroupDao
import io.kuark.service.user.provider.rbac.ibiz.IRbacUserGroupBiz
import io.kuark.service.user.provider.rbac.model.po.RbacUserGroup
import org.springframework.stereotype.Service

/**
 * 用户组服务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class RbacUserGroupBiz: IRbacUserGroupBiz, BaseCrudBiz<String, RbacUserGroup, RbacUserGroupDao>() {
//endregion your codes 1

    //region your codes 2

    override fun getGroupPermissions(groupCode: String, resourceType: io.kuark.service.sys.common.vo.resource.ResourceType): List<io.kuark.service.sys.common.vo.resource.SysResourceRecord> {
        TODO("Not yet implemented")
    }

    override fun setGroupPermissions(groupCode: String, resourceIds: List<String>): Boolean {
        TODO("Not yet implemented")
    }

    //endregion your codes 2

}