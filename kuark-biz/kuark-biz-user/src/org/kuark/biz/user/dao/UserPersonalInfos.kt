package org.kuark.biz.user.dao

import me.liuwj.ktorm.schema.*
import org.kuark.biz.user.po.UserPersonalInfo
import org.kuark.data.jdbc.support.MaintainableTable

/**
 * 用户个人信息数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object UserPersonalInfos: MaintainableTable<UserPersonalInfo>("user_personal_info") {
//endregion your codes 1

	/** 真实姓名 */
	var realName = varchar("real_name").bindTo { it.realName }
	/** 昵称 */
	var nickname = varchar("nickname").bindTo { it.nickname }
	/** 性别代码 */
	var sexDictCode = varchar("sex_dict_code").bindTo { it.sexDictCode }
	/** 生日 */
	var birthday = date("birthday").bindTo { it.birthday }
	/** 身份证号 */
	var idCardNo = varchar("id_card_no").bindTo { it.idCardNo }
	/** 星座代码 */
	var constellationDictCode = varchar("constellation_dict_code").bindTo { it.constellationDictCode }
	/** 国家id */
	var countryId = varchar("country_id").bindTo { it.countryId }
	/** 民族代码 */
	var nationDictCode = varchar("nation_dict_code").bindTo { it.nationDictCode }
	/** 地区编码 */
	var regionCode = varchar("region_code").bindTo { it.regionCode }
	/**  */
	var userStatusDictCode = varchar("user_status_dict_code").bindTo { it.userStatusDictCode }
	/**  */
	var userStatusReason = varchar("user_status_reason").bindTo { it.userStatusReason }
	/**  */
	var userTypeDictCode = varchar("user_type_dict_code").bindTo { it.userTypeDictCode }
	/** 头像url */
	var avatarUrl = varchar("avatar_url").bindTo { it.avatarUrl }
	/**  */
	var subSysDictCode = varchar("sub_sys_dict_code").bindTo { it.subSysDictCode }
	/**  */
	var freezeTimeStart = datetime("freeze_time_start").bindTo { it.freezeTimeStart }
	/**  */
	var freezeTimeEnd = datetime("freeze_time_end").bindTo { it.freezeTimeEnd }
	/**  */
	var lastLoginTime = datetime("last_login_time").bindTo { it.lastLoginTime }
	/**  */
	var lastLogoutTime = datetime("last_logout_time").bindTo { it.lastLogoutTime }
	/**  */
	var lastLoginIp = varchar("last_login_ip").bindTo { it.lastLoginIp }
	/**  */
	var lastLoginTerminalDictCode = varchar("last_login_terminal_dict_code").bindTo { it.lastLoginTerminalDictCode }
	/**  */
	var totalOnlineTime = float("total_online_time").bindTo { it.totalOnlineTime }
	/**  */
	var registerIp = varchar("register_ip").bindTo { it.registerIp }
	/**  */
	var registerUrl = varchar("register_url").bindTo { it.registerUrl }
	/**  */
	var dynamicAuthKey = varchar("dynamic_auth_key").bindTo { it.dynamicAuthKey }

	//region your codes 2

	//endregion your codes 2

}