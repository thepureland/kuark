package io.kuark.service.user.po

import io.kuark.ability.data.jdbc.support.DbEntityFactory
import io.kuark.ability.data.jdbc.support.IMaintainableDbEntity

/**
 * 用户账号第三方授权信息数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface UserAccountThirdParty: IMaintainableDbEntity<String, UserAccountThirdParty> {
//endregion your codes 1

    companion object : DbEntityFactory<UserAccountThirdParty>()

    /** 外键，用户账号id，user_account表主键 */
    var userAccountId: String

    /** 身份类型代码 */
    var identityTypeDictCode: String

    /** 唯一身份标识 */
    var identifier: String

    /** 子系统代码 */
    var subSysDictCode: String?

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String?


    //region your codes 2

	//endregion your codes 2

}