package io.kuark.service.user.provider.user.biz.impl

import io.kuark.ability.data.rdb.biz.BaseCrudBiz
import io.kuark.service.user.provider.user.dao.UserLoginPersistentDao
import io.kuark.service.user.provider.user.biz.ibiz.IUserLoginPersistentBiz
import io.kuark.service.user.provider.user.model.po.UserLoginPersistent
import org.springframework.stereotype.Service

/**
 * 登陆持久化业务
 *
 * @author K
 * @since 1.0.0
 */
@Service
//region your codes 1
open class UserLoginPersistentBiz : BaseCrudBiz<String, UserLoginPersistent, UserLoginPersistentDao>(),
    IUserLoginPersistentBiz {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}