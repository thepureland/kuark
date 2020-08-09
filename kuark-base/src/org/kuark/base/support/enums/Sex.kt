package org.kuark.base.support.enums

/**
 * 性别枚举
 *
 * @author K
 * @since 1.0.0
 */
enum class Sex(override val code: String, override var trans: String?) : IDictEnum {

    FEMALE("0", "女性"),
    MALE("1", "男性"),
    SECRET("9", "保密");

}