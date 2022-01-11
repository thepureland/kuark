package io.kuark.service.auth.provider.rbac.biz

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.service.auth.provider.rbac.dao.AuthUserGroupDao
import io.kuark.service.auth.provider.rbac.ibiz.IAuthUserGroupBiz
import io.kuark.service.auth.provider.rbac.model.po.AuthUserGroup
import org.springframework.stereotype.Service

/**
 * 用户组服务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class AuthUserGroupBiz: IAuthUserGroupBiz, BaseCrudBiz<String, AuthUserGroup, AuthUserGroupDao>() {
//endregion your codes 1

    //region your codes 2

	//endregion your codes 2

}