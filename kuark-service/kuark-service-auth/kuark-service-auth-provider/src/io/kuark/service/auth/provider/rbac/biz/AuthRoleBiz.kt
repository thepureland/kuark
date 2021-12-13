package io.kuark.service.auth.provider.rbac.biz

import io.kuark.ability.data.rdb.biz.BaseBiz
import io.kuark.service.auth.provider.rbac.dao.AuthRoleDao
import io.kuark.service.auth.provider.rbac.ibiz.IAuthRoleBiz
import io.kuark.service.auth.provider.rbac.model.po.AuthRole
import org.springframework.stereotype.Service

/**
 * 角色服务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
class AuthRoleBiz: IAuthRoleBiz, BaseBiz<String, AuthRole, AuthRoleDao>() {
//endregion your codes 1

    //region your codes 2

	//endregion your codes 2

}