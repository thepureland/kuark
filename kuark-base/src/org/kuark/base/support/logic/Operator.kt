package org.kuark.base.support.logic

import org.kuark.base.lang.collections.containsAll
import org.kuark.base.lang.EnumKit
import org.kuark.base.support.enums.IDictEnum

/**
 * 逻辑操作符枚举
 *
 * @author K
 * @since 1.0.0
 */
enum class Operator constructor(
    override val code: String,
    override val trans: String,
    // 值是否可接受null
    val isAcceptNull: Boolean = false,
    // 操作值只接收字符串类型
    val isStringOnly: Boolean = false
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

//    /** 等于属性 */
//    EQ_P("P=", "等于属性", false, false),
//
//    /** 不等于属性 */
//    NE_P("P!=", "不等于属性", false, false),
//
//    /** 小于大于(不等于)属性 */
//    LG_P("P<>", "小于大于(不等于)属性", false, false),
//
//    /** 大于等于属性 */
//    GE_P("P>=", "大于等于属性", false, false),
//
//    /** 小于等于属性 */
//    LE_P("P<=", "小于等于属性", false, false),
//
//    /** 大于属性 */
//    GT_P("P>", "大于属性", false, false),
//
//    /** 小于属性 */
//    LT_P("P<", "小于属性", false, false),



    /**
     * 根据当前操作符作断言
     *
     * @param v1 左值
     * @param v2 右值 (对于IS_NULL、IS_NOT_NULL、IS_EMPTY、IS_NOT_EMPTY来说无意义)
     * @return 是否满足逻辑关系
     */
    fun assert(v1: Any?, v2: Any? = null): Boolean {
        return when (this) {
            EQ -> {
                if (v1 == null && v2 == null) {
                    return true
                }
                if (v1 == null || v2 == null) {
                    false
                } else v1 == v2
            }
            IEQ -> {
                if (v1 == null && v2 == null) {
                    return true
                }
                if (v1 == null || v2 == null) {
                    return false
                }
                if (v1 is String && v2 is String) {
                    (v1.toString() + "").toLowerCase() == (v2.toString() + "").toLowerCase()
                } else v1 == v2
            }
            NE, LG -> {
                if (v1 == null && v2 == null) {
                    return false
                }
                if (v1 == null || v2 == null) {
                    return true
                }
                if (v1 is String && v2 is String) {
                    v1.toString() + "" != v2.toString() + ""
                } else v1 != v2
            }
            GE -> {
                if (v1 == null && v2 == null) {
                    return true
                }
                if (v1 is Comparable<*> && v2 is Comparable<*>) {
                    (v1 as Comparable<Any?>) >= v2
                } else false
            }
            LE -> {
                if (v1 == null && v2 == null) {
                    return true
                }
                if (v1 is Comparable<*> && v2 is Comparable<*>) {
                    (v1 as Comparable<Any?>) <= v2
                } else false
            }
            GT -> {
                if (v1 is Comparable<*> && v2 is Comparable<*>) {
                    (v1 as Comparable<Any?>) > v2
                } else false
            }
            LT -> {
                if (v1 is Comparable<*> && v2 is Comparable<*>) {
                    (v1 as Comparable<Any?>) < v2
                } else false
            }
            LIKE -> {
                if (v1 is String && v2 is String) {
                    v1.contains((v2 as CharSequence?)!!)
                } else false
            }
            LIKE_S -> {
                if (v1 is String && v2 is String) {
                    v1.trim { it <= ' ' }.startsWith((v2 as String?)!!)
                } else false
            }
            LIKE_E -> {
                if (v1 is String && v2 is String) {
                    v1.trim { it <= ' ' }.endsWith((v2 as String?)!!)
                } else false
            }
            ILIKE -> {
                if (v1 is String && v2 is String) {
                    v1.toLowerCase().contains(v2.toLowerCase())
                } else false
            }
            ILIKE_S -> {
                if (v1 is String && v2 is String) {
                    v1.trim { it <= ' ' }.toLowerCase()
                        .startsWith(v2.toLowerCase())
                } else false
            }
            ILIKE_E -> {
                if (v1 is String && v2 is String) {
                    v1.trim { it <= ' ' }.toLowerCase()
                        .endsWith(v2.toLowerCase())
                } else false
            }
            IN -> `in`(v1, v2)
            NOT_IN -> !`in`(v1, v2)
            IS_NULL -> v1 == null
            IS_NOT_NULL -> v1 != null
            IS_NOT_EMPTY -> {
                if (v1 == null) {
                    return true
                }
                if (v1 is String) {
                    return (v1 as String?)!!.isNotEmpty()
                }
                if (v1 is Array<*>) {
                    return (v1 as Array<Any?>).isNotEmpty()
                }
                if (v1 is Collection<*>) {
                    return !(v1 as Collection<*>?)!!.isEmpty()
                }
                if (v1 is Map<*, *>) {
                    (v1 as Map<*, *>?)!!.isNotEmpty()
                } else v1.toString().isEmpty()
            }
            IS_EMPTY -> {
                if (v1 == null) {
                    return false
                }
                if (v1 is String) {
                    return v1.isEmpty()
                }
                if (v1 is Array<*>) {
                    return v1.isEmpty()
                }
                if (v1 is Collection<*>) {
                    return v1.isEmpty()
                }
                if (v1 is Map<*, *>) {
                    v1.isEmpty()
                } else v1.toString().isEmpty()
            }
            else -> false
        }
    }

    private fun `in`(v1: Any?, v2: Any?): Boolean {
        var value1 = v1
        var value2 = v2
        if (value1 is String && value2 is String) {
            val elems = value2.split(",").toTypedArray()
            return elems.contains(value1)
        }
        if (value1 is Array<*>) {
            value1 = listOf(*value1)
        }
        if (value2 is Array<*>) {
            value2 = listOf(*value2)
        }
        if (value2 is Collection<*>) {
            return if (value1 is Collection<*>) {
                value2.containsAll((value1 as Collection<*>?)!!)
            } else {
                value2.contains(value1)
            }
        }
        return if (value1 is Map<*, *> && value2 is Map<*, *>) {
            (value2 as Map<Any?, Any?>).containsAll(value2)
        } else false
    }

    companion object {
        fun enumOf(code: String): Operator? {
            var codeStr = code
            if (codeStr.isNotBlank()) {
                codeStr = codeStr.toUpperCase()
            }
            return EnumKit.enumOf(Operator::class, codeStr)
        }
    }

}