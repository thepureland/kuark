package io.kuark.service.user.common.user.vo.account

import io.kuark.base.support.IIdEntity
import java.io.Serializable

class UserAccountCacheItem: IIdEntity<String>, Serializable {

    companion object {
        private const val serialVersionUID = 5090913885757034710L
    }

    /** 主键 */
    override var id: String? = null

    /** 用户名 */
    var username: String? = null

    /** 密码 */
    var password: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 用户状态代码 */
    var userStatusDictCode: String? = null

    /** 用户状态原因 */
    var userStatusReason: String? = null

    /** 用户类型代码 */
    var userTypeDictCode: String? = null

    /** 注册ip(标准ipv6全格式) */
    var registerIp: String? = null

    /** 注册url */
    var registerUrl: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** 是否内置 */
    var builtIn: Boolean? = null

    /** 备注 */
    var remark: String? = null

}