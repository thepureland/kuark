package io.kuark.service.sys.common.vo.resource

import io.kuark.base.support.enums.IDictEnum

enum class ResourceType(override val code: String, override val trans: String): IDictEnum {

    MENU("1", "菜单"),
    FUNCTION("2", "功能"),
    ACTION("3", "请求")

}