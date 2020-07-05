package org.kuark.biz.user.dao

import me.liuwj.ktorm.schema.*
import org.kuark.biz.user.po.UserBizAuditLog
import org.kuark.data.jdbc.support.StringIdTable

/**
 * 用户审计日志数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object UserBizAuditLogs: StringIdTable<UserBizAuditLog>("user_biz_audit_log") {
//endregion your codes 1

	/** 外键，用户账号id，user_account表主键 */
	var userAccountId = varchar("user_account_id").bindTo { it.userAccountId }
	/** 身份类型代码 */
	var identityTypeDictCode = varchar("identity_type_dict_code").bindTo { it.identityTypeDictCode }
	/** 身份类型 */
	var identityType = varchar("identity_type").bindTo { it.identityType }
	/** 唯一身份标识 */
	var identifier = varchar("identifier").bindTo { it.identifier }
	/** 子系统代码 */
	var subSysDictCode = varchar("sub_sys_dict_code").bindTo { it.subSysDictCode }
	/** 子系统 */
	var subSys = varchar("sub_sys").bindTo { it.subSys }
	/** 模块 */
	var module = varchar("module").bindTo { it.module }
	/** 操作时间 */
	var operateTime = datetime("operate_time").bindTo { it.operateTime }
	/** 客户端ip，标准全格式ipv6 */
	var clientIp = varchar("client_ip").bindTo { it.clientIp }
	/** 客户端ip的isp */
	var clientIpIsp = varchar("client_ip_isp").bindTo { it.clientIpIsp }
	/** 客户端区域编码 */
	var clientRegionCode = varchar("client_region_code").bindTo { it.clientRegionCode }
	/** 客户端区域 */
	var clientRegionName = varchar("client_region_name").bindTo { it.clientRegionName }
	/** 客户端终端类型代码 */
	var clientTerminalDictCode = varchar("client_terminal_dict_code").bindTo { it.clientTerminalDictCode }
	/** 客户端终端类型 */
	var clientTerminal = varchar("client_terminal").bindTo { it.clientTerminal }
	/** 客户端操作系统 */
	var clientOs = varchar("client_os").bindTo { it.clientOs }
	/** 客户端浏览器 */
	var clientBrowser = varchar("client_browser").bindTo { it.clientBrowser }

	//region your codes 2

	//endregion your codes 2

}