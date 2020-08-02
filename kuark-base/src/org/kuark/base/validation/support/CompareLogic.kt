package org.kuark.base.validation.support

/**
 * 比较逻辑枚举
 *
 * @author K
 * @since 1.0.0
 */
enum class CompareLogic(val code: String, val trans: String) {

    EQ("=", "等于"),
    IEQ("I=", "忽略大小写等于"), // 仅适用于CharSequence或Char
    NE("!=", "不等于"),
    GE(">=", "大于等于"),
    LE("<=", "小于等于"),
    GT(">", "大于"),
    LT("<", "小于");

}