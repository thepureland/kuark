package org.kuark.biz.user.dao

import me.liuwj.ktorm.schema.*
import org.kuark.biz.user.po.UserAccountProtection
import org.kuark.data.jdbc.support.MaintainableTable

/**
 * 用户账号保护数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object UserAccountProtections: MaintainableTable<UserAccountProtection>("user_account_protection") {
//endregion your codes 1

    /** 问题１ */
    var question1 = varchar("question1").bindTo { it.question1 }

    /** 答案1 */
    var answer1 = varchar("answer1").bindTo { it.answer1 }

    /** 问题2 */
    var question2 = varchar("question2").bindTo { it.question2 }

    /** 答案2 */
    var answer2 = varchar("answer2").bindTo { it.answer2 }

    /** 问题3 */
    var question3 = varchar("question3").bindTo { it.question3 }

    /** 答案3 */
    var answer3 = varchar("answer3").bindTo { it.answer3 }

    /** 安全的联系方式id */
    var safeContactWayId = varchar("safe_contact_way_id").bindTo { it.safeContactWayId }

    /** 总的找回密码次数 */
    var totalValidateCount = int("total_validate_count").bindTo { it.totalValidateCount }

    /** 必须答对的问题数 */
    var matchQuestionCount = int("match_question_count").bindTo { it.matchQuestionCount }

    /** 错误次数 */
    var errorTimes = int("error_times").bindTo { it.errorTimes }


    //region your codes 2

	//endregion your codes 2

}