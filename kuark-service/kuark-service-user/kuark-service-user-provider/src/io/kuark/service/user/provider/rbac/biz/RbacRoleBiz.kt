package io.kuark.service.user.provider.rbac.biz

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.service.user.provider.rbac.dao.RbacRoleDao
import io.kuark.service.user.provider.rbac.ibiz.IRbacRoleBiz
import io.kuark.service.user.provider.rbac.model.po.RbacRole
import org.springframework.stereotype.Service

/**
 * 角色服务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class RbacRoleBiz: IRbacRoleBiz, BaseCrudBiz<String, RbacRole, RbacRoleDao>() {
//endregion your codes 1

    //region your codes 2

	//endregion your codes 2

}