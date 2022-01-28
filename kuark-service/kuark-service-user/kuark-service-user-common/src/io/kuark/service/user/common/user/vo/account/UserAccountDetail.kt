package io.kuark.service.user.common.user.vo.account

import io.kuark.base.support.result.IdJsonResult
import java.time.LocalDateTime

open class UserAccountDetail : IdJsonResult<String>() {

    /** 用户名 */
    var username: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 用户状态代码 */
    var userStatusDictCode: String? = null

    /** 用户状态原因 */
    var userStatusReason: String? = null

    /** 用户类型代码 */
    var userTypeDictCode: String? = null

    /** 账号锁定时间起 */
    var lockTimeStart: LocalDateTime? = null

    /** 账号锁定时间止 */
    var lockTimeEnd: LocalDateTime? = null

    /** 最后一次登入时间 */
    var lastLoginTime: LocalDateTime? = null

    /** 最后一次登出时间 */
    var lastLogoutTime: LocalDateTime? = null

    /** 最后一次登入ip(标准ipv6全格式) */
    var lastLoginIp: String? = null

    /** 最后一次登入终端代码 */
    var lastLoginTerminalDictCode: String? = null

    /** 总在线时长(小时) */
    var totalOnlineTime: Float? = null

    /** 注册ip(标准ipv6全格式) */
    var registerIp: String? = null

    /** 注册url */
    var registerUrl: String? = null

    /** 动态验证码的密钥 */
    var dynamicAuthKey: String? = null

    /** 二级密码 */
    var secondPassword: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** 记录创建时间 */
    var createTime: LocalDateTime? = null

    /** 记录创建用户 */
    var createUser: String? = null

    /** 记录更新时间 */
    var updateTime: LocalDateTime? = null

    /** 记录更新用户 */
    var updateUser: String? = null

    /** 是否内置 */
    var builtIn: Boolean? = null

    /** 备注 */
    var remark: String? = null

}