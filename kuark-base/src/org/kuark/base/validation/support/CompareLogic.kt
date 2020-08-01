package org.kuark.base.validation.support

/**
 * Create by (admin) on 2015/1/23.
 */
enum class CompareLogic(val code: String, val trans: String) {

    EQ("=", "等于"),
    IEQ("I=", "忽略大小写等于"),
    NE("!=", "不等于"),
    GE(">=", "大于等于"),
    LE("<=", "小于等于"),
    GT(">", "大于"),
    LT("<", "小于");

}