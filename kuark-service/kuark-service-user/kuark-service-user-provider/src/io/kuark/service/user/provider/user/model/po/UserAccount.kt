package io.kuark.service.user.provider.user.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IUpdatableDbEntity
import java.time.LocalDateTime

/**
 * 用户账号数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface UserAccount : IUpdatableDbEntity<String, UserAccount> {
//endregion your codes 1

    companion object : DbEntityFactory<UserAccount>()

    /** 用户名 */
    var username: String

    /** 密码 */
    var password: String

    /** 子系统代码 */
    var subSysDictCode: String?

    /** 用户状态代码 */
    var userStatusDictCode: String?

    /** 用户状态原因 */
    var userStatusReason: String?

    /** 用户类型代码 */
    var userTypeDictCode: String?

    /** 账号锁定时间起 */
    var lockTimeStart: LocalDateTime?

    /** 账号锁定时间止 */
    var lockTimeEnd: LocalDateTime?

    /** 最后一次登入时间 */
    var lastLoginTime: LocalDateTime?

    /** 最后一次登出时间 */
    var lastLogoutTime: LocalDateTime?

    /** 最后一次登入ip(标准ipv6全格式) */
    var lastLoginIp: String?

    /** 最后一次登入终端代码 */
    var lastLoginTerminalDictCode: String?

    /** 总在线时长(小时) */
    var totalOnlineTime: Float?

    /** 注册ip(标准ipv6全格式) */
    var registerIp: String?

    /** 注册url */
    var registerUrl: String?

    /** 动态验证码的密钥 */
    var dynamicAuthKey: String?

    /** 二级密码 */
    var secondPassword: String?

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String?

    /** 组织id */
    var organizationId: String?


    //region your codes 2

    //endregion your codes 2

}