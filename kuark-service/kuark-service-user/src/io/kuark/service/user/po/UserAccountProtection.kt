package io.kuark.service.user.po

import io.kuark.ability.data.jdbc.support.DbEntityFactory
import io.kuark.ability.data.jdbc.support.IMaintainableDbEntity

/**
 * 用户账号保护数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface UserAccountProtection: IMaintainableDbEntity<String, UserAccountProtection> {
//endregion your codes 1

    companion object : DbEntityFactory<UserAccountProtection>()

    /** 问题１ */
    var question1: String

    /** 答案1 */
    var answer1: String

    /** 问题2 */
    var question2: String?

    /** 答案2 */
    var answer2: String?

    /** 问题3 */
    var question3: String?

    /** 答案3 */
    var answer3: String?

    /** 安全的联系方式id */
    var safeContactWayId: String?

    /** 总的找回密码次数 */
    var totalValidateCount: Int

    /** 必须答对的问题数 */
    var matchQuestionCount: Int

    /** 错误次数 */
    var errorTimes: Int


    //region your codes 2

	//endregion your codes 2

}