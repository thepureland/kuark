package io.kuark.base.support.logic

import io.kuark.base.lang.EnumKit
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.enums.IDictEnum
import java.util.*

/**
 * 逻辑操作符枚举
 *
 * @author K
 * @since 1.0.0
 */
enum class LogicOperator constructor(
    override val code: String,
    override val trans: String,
    // 值是否可接受null
    val acceptNull: Boolean = false,
    // 操作值只接收字符串类型
    val stringOnly: Boolean = false
) : IDictEnum {

    /** 等于 */
    EQ("=", "等于"),

    /** 忽略大小写等于 */
    IEQ("I=", "忽略大小写等于", false, true),

    /** 不等于 */
    NE("!=", "不等于", false, false),

    /** 小于大于(不等于) */
    LG("<>", "小于大于(不等于)"),

    /** 大于等于 */
    GE(">=", "大于等于"),

    /** 小于等于 */
    LE("<=", "小于等于"),

    /** 大于 */
    GT(">", "大于"),

    /** 小于 */
    LT("<", "小于"),

    /** 匹配字符串任意位置 */
    LIKE("LIKE", "任意位置匹配", false, true),

    /** 匹配字符串前面 */
    LIKE_S("LIKE_S", "匹配前面", false, true),

    /** 匹配字符串后面 */
    LIKE_E("LIKE_E", "匹配后面", false, true),

    /** 忽略大小写匹配字符串任意位置 */
    ILIKE("ILIKE", "忽略大小写任意位置匹配", false, true),

    /** 忽略大小写匹配字符串前面 */
    ILIKE_S("ILIKE_S", "忽略大小写匹配前面", false, true),

    /** 忽略大小写匹配字符串后面 */
    ILIKE_E("ILIKE_E", "忽略大小写匹配后面", false, true),

    /** in查询 */
    IN("IN", "in查询"),

    /** not in查询 */
    NOT_IN("NOT IN", "not in查询"),

    /** 是否为null */
    IS_NULL("IS NULL", "判空", true, false),

    /** 是否不为null */
    IS_NOT_NULL("IS NOT NULL", "非空", true, false),

    /** 是否为空串 */
    IS_EMPTY("=''", "等于空串", true, true),

    /** 是否不为空串 */
    IS_NOT_EMPTY("!=''", "不等于空串", true, true);


    /**
     * 根据当前操作符作断言
     *
     * @param v1 左值
     * @param v2 右值 (对于IS_NULL、IS_NOT_NULL、IS_EMPTY、IS_NOT_EMPTY来说无意义)
     * @return 是否满足逻辑关系
     * @author K
     * @since 1.0.0
     */
    fun assert(v1: Any?, v2: Any? = null): Boolean {
        return when (this) {
            EQ -> Operator.EQ.assert(v1, v2)
            IEQ -> Operator.IEQ.assert(v1, v2)
            NE, LG -> Operator.NE.assert(v1, v2)
            GE -> Operator.GE.assert(v1, v2)
            LE -> Operator.LE.assert(v1, v2)
            GT -> Operator.GT.assert(v1, v2)
            LT ->Operator.LT.assert(v1, v2)
            LIKE -> Operator.LIKE.assert(v1, v2)
            LIKE_S -> Operator.LIKE_S.assert(v1, v2)
            LIKE_E -> Operator.LIKE_E.assert(v1, v2)
            ILIKE -> Operator.ILIKE.assert(v1, v2)
            ILIKE_S -> Operator.ILIKE_S.assert(v1, v2)
            ILIKE_E -> Operator.ILIKE_E.assert(v1, v2)
            IN -> Operator.IN.assert(v1, v2)
            NOT_IN -> Operator.NOT_IN.assert(v1, v2)
            IS_NULL -> Operator.IS_NULL.assert(v1, v2)
            IS_NOT_NULL -> Operator.IS_NOT_NULL.assert(v1, v2)
            IS_NOT_EMPTY -> Operator.IS_NOT_EMPTY.assert(v1, v2)
            IS_EMPTY -> Operator.IS_EMPTY.assert(v1, v2)
        }
    }

    companion object {
        fun enumOf(code: String): LogicOperator? {
            var codeStr = code
            if (codeStr.isNotBlank()) {
                codeStr = codeStr.uppercase(Locale.getDefault())
            }
            return EnumKit.enumOf(LogicOperator::class, codeStr)
        }
    }

}