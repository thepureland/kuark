package org.kuark.biz.user.po

import org.kuark.data.jdbc.support.DbEntityFactory
import org.kuark.data.jdbc.support.IMaintainableDbEntity

/**
 * 用户账号授权数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface UserAccountAuth: IMaintainableDbEntity<String, UserAccountAuth> {
//endregion your codes 1

    companion object : DbEntityFactory<UserAccountAuth>()

    /** 外键，用户账号id，user_account表主键 */
    var userAccountId: String

    /** 身份类型代码 */
    var identityTypeDictCode: String

    /** 唯一身份标识 */
    var identifier: String

    /** 子系统代码 */
    var subSysDictCode: String

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String

    /** 本系统账号是密码、第三方的是Token */
    var credential: String

    /** 授权账号是否被验证 */
    var isVerified: Boolean


    //region your codes 2

	//endregion your codes 2

}