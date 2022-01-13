package io.kuark.service.user.common.user.vo.account

import java.io.Serializable
import java.time.LocalDateTime

class UserAccountRecord : Serializable {

    /** 主键 */
    var id: String? = null

    /** 用户名 */
    var username: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 用户状态代码 */
    var userStatusDictCode: String? = null

    /** 用户类型代码 */
    var userTypeDictCode: String? = null

    /** 最后一次登入时间 */
    var lastLoginTime: LocalDateTime? = null

    /** 创建时间 */
    var createTime: LocalDateTime? = null

}