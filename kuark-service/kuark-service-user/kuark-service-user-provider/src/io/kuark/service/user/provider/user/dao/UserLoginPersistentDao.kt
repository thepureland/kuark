package io.kuark.service.user.provider.user.dao

import io.kuark.ability.data.rdb.support.BaseCrudDao
import org.springframework.stereotype.Repository

/**
 * 登陆持久化数据访问对象
 *
 * @author K
 * @since 1.0.0
 */
@Repository
//region your codes 1
open class UserLoginPersistentDao : BaseCrudDao<String, io.kuark.service.user.provider.user.model.po.UserLoginPersistent, io.kuark.service.user.provider.user.model.table.UserLoginPersistents>() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

}