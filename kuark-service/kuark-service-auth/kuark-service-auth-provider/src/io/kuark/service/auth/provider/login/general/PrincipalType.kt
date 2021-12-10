package io.kuark.service.auth.provider.login.general

import io.kuark.base.support.enums.IDictEnum

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
enum class PrincipalType(override val code: String, override val trans: String) : IDictEnum {

    SYS_ACCOUT("1-sys", "系统账号"),
    MOBILE("2-mobile", "手机号码"),
    EMAIL("2-email", "电子邮箱"),
    WECHAT("3-wechat", "微信"),
    QQ("3-qq", "QQ")

}