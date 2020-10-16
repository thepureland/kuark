package io.kuark.service.user.provider.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IMaintainableDbEntity

/**
 * 用户联系方式数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface UserContactWay: IMaintainableDbEntity<String, UserContactWay> {
//endregion your codes 1

    companion object : DbEntityFactory<UserContactWay>()

    /** 外键，用户账号id，user_account表主键 */
    var userId: String

    /** 联系方式代码 */
    var contactWayDictCode: String

    /** 联系方式值 */
    var contactWayValue: String

    /** 联系方式状态代码 */
    var contactWayStatusDictCode: String

    /** 优先级 */
    var priority: Int?


    //region your codes 2

	//endregion your codes 2

}