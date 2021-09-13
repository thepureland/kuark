package io.kuark.ability.auth.rbac.biz

import io.kuark.ability.auth.rbac.dao.AuthPersistentLoginsDao
import org.springframework.stereotype.Service
import io.kuark.ability.auth.rbac.ibiz.IAuthPersistentLoginsBiz
import io.kuark.ability.auth.rbac.model.po.AuthPersistentLogins
import io.kuark.ability.data.rdb.biz.BaseBiz

/**
 * 登陆持久化业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class AuthPersistentLoginsBiz : BaseBiz<String, AuthPersistentLogins, AuthPersistentLoginsDao>(), IAuthPersistentLoginsBiz {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}