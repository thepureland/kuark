package org.kuark.biz.user.po

import org.kuark.data.jdbc.support.DbEntityFactory
import org.kuark.data.jdbc.support.IMaintainableDbEntity
import java.time.LocalDateTime
import java.time.LocalDate

/**
 * 用户个人信息数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface UserPersonalInfo: IMaintainableDbEntity<String, UserPersonalInfo> {
//endregion your codes 1

    companion object : DbEntityFactory<UserPersonalInfo>()

    /** 真实姓名 */
    var realName: String

    /** 昵称 */
    var nickname: String

    /** 性别代码 */
    var sexDictCode: String

    /** 生日 */
    var birthday: LocalDate

    /** 身份证号 */
    var idCardNo: String

    /** 星座代码 */
    var constellationDictCode: String

    /** 国家id */
    var countryId: String

    /** 民族代码 */
    var nationDictCode: String

    /** 地区编码 */
    var regionCode: String

    /**  */
    var userStatusDictCode: String

    /**  */
    var userStatusReason: String

    /**  */
    var userTypeDictCode: String

    /** 头像url */
    var avatarUrl: String

    /**  */
    var subSysDictCode: String

    /**  */
    var freezeTimeStart: LocalDateTime

    /**  */
    var freezeTimeEnd: LocalDateTime

    /**  */
    var lastLoginTime: LocalDateTime

    /**  */
    var lastLogoutTime: LocalDateTime

    /**  */
    var lastLoginIp: String

    /**  */
    var lastLoginTerminalDictCode: String

    /**  */
    var totalOnlineTime: Float

    /**  */
    var registerIp: String

    /**  */
    var registerUrl: String

    /**  */
    var dynamicAuthKey: String


    //region your codes 2

	//endregion your codes 2

}