package io.kuark.service.user.common.user.vo.account

import io.kuark.base.support.enums.IDictEnum

enum class UserType(override val code: String, override val trans: String): IDictEnum {

    MAIN_ACCOUNT("00", "主账号"),
    SUB_ACCOUNT("01", "子账号")

}