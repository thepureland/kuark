package io.kuark.service.sys.common.vo.reg.resource

import io.kuark.base.support.enums.IDictEnum

enum class ResourceTypeEnum(override val code: String, override val trans: String): IDictEnum {

    MENU("menu", "菜单"),
    ACTION("action", "请求"),
    FUNCTION("function", "功能");

}