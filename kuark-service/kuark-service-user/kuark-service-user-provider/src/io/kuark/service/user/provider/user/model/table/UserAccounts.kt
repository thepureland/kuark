package io.kuark.service.user.provider.user.model.table

import io.kuark.ability.data.rdb.support.UpdatableTable
import io.kuark.service.user.provider.user.model.po.UserAccount
import org.ktorm.schema.datetime
import org.ktorm.schema.float
import org.ktorm.schema.varchar

/**
 * 用户账号数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object UserAccounts : UpdatableTable<UserAccount>("user_account") {
//endregion your codes 1

    /** 用户名 */
    var username= varchar("username").bindTo { it.username }

    /** 密码 */
    var password= varchar("password").bindTo { it.password }

    /** 子系统代码 */
    var subSysDictCode = varchar("sub_sys_dict_code").bindTo { it.subSysDictCode }

    /** 用户状态代码 */
    var userStatusDictCode = varchar("user_status_dict_code").bindTo { it.userStatusDictCode }

    /** 用户状态原因 */
    var userStatusReason = varchar("user_status_reason").bindTo { it.userStatusReason }

    /** 用户类型代码 */
    var userTypeDictCode = varchar("user_type_dict_code").bindTo { it.userTypeDictCode }

    /** 账号锁定时间起 */
    var lockTimeStart = datetime("lock_time_start").bindTo { it.lockTimeStart }

    /** 账号锁定时间止 */
    var lockTimeEnd = datetime("lock_time_end").bindTo { it.lockTimeEnd }

    /** 最后一次登入时间 */
    var lastLoginTime = datetime("last_login_time").bindTo { it.lastLoginTime }

    /** 最后一次登出时间 */
    var lastLogoutTime = datetime("last_logout_time").bindTo { it.lastLogoutTime }

    /** 最后一次登入ip(标准ipv6全格式) */
    var lastLoginIp = varchar("last_login_ip").bindTo { it.lastLoginIp }

    /** 最后一次登入终端代码 */
    var lastLoginTerminalDictCode = varchar("last_login_terminal_dict_code").bindTo { it.lastLoginTerminalDictCode }

    /** 总在线时长(小时) */
    var totalOnlineTime = float("total_online_time").bindTo { it.totalOnlineTime }

    /** 注册ip(标准ipv6全格式) */
    var registerIp = varchar("register_ip").bindTo { it.registerIp }

    /** 注册url */
    var registerUrl = varchar("register_url").bindTo { it.registerUrl }

    /** 动态验证码的密钥 */
    var dynamicAuthKey = varchar("dynamic_auth_key").bindTo { it.dynamicAuthKey }

    /** 二级密码 */
    var secondPassword = varchar("second_password").bindTo { it.secondPassword }

    /** 租户id */
    var tenantId = varchar("tenant_id").bindTo { it.tenantId }

    /** 组织id */
    var organizationId = varchar("organization_id").bindTo { it.organizationId }


    //region your codes 2

    //endregion your codes 2

}