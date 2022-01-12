package io.kuark.service.user.provider.user.model.enums

import io.kuark.base.support.enums.IDictEnum

enum class UserAccountStatus(override val code: String, override val trans: String): IDictEnum {

    CANCELED("00", "注销"),
    NORMAL("10", "正常"),
    LOCKED("20", "锁定"),
    EXPIRED("30", "账号过期"),
    CREDENTIAL_EXPIRED("40", "凭证过期")

}