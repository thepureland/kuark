package io.kuark.service.auth.provider.rbac.biz

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.service.auth.provider.rbac.dao.AuthPersistentLoginsDao
import io.kuark.service.auth.provider.rbac.ibiz.IAuthPersistentLoginsBiz
import io.kuark.service.auth.provider.rbac.model.po.AuthPersistentLogins
import org.springframework.stereotype.Service

/**
 * 登陆持久化业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class AuthPersistentLoginsBiz : BaseCrudBiz<String, AuthPersistentLogins, AuthPersistentLoginsDao>(),
    IAuthPersistentLoginsBiz {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}