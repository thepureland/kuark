package io.kuark.service.user.po

import io.kuark.ability.data.jdbc.support.DbEntityFactory
import io.kuark.ability.data.jdbc.support.IDbEntity
import java.time.LocalDateTime

/**
 * 用户审计日志数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface UserBizAuditLog: IDbEntity<String, UserBizAuditLog> {
//endregion your codes 1

    companion object : DbEntityFactory<UserBizAuditLog>()

    /** 外键，用户账号id，user_account表主键 */
    var userAccountId: String

    /** 身份类型代码 */
    var identityTypeDictCode: String

    /** 身份类型 */
    var identityType: String

    /** 唯一身份标识 */
    var identifier: String

    /** 子系统代码 */
    var subSysDictCode: String

    /** 子系统 */
    var subSys: String

    /** 模块 */
    var module: String

    /** 操作时间 */
    var operateTime: LocalDateTime

    /** 客户端ip，标准全格式ipv6 */
    var clientIp: String

    /** 客户端ip的isp */
    var clientIpIsp: String

    /** 客户端区域编码 */
    var clientRegionCode: String

    /** 客户端区域 */
    var clientRegionName: String

    /** 客户端终端类型代码 */
    var clientTerminalDictCode: String

    /** 客户端终端类型 */
    var clientTerminal: String

    /** 客户端操作系统 */
    var clientOs: String

    /** 客户端浏览器 */
    var clientBrowser: String


    //region your codes 2

	//endregion your codes 2

}