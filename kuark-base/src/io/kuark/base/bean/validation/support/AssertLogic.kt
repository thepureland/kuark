package io.kuark.base.bean.validation.support

import io.kuark.base.query.enums.Operator

/**
 * 断言逻辑枚举
 *
 * @author K
 * @since 1.0.0
 */
enum class AssertLogic {

    /** null断言 */
    IS_NULL,
    /** 非null断言 */
    IS_NOT_NULL,
    /**
     * 空断言。字符串判断是否为空串，数组、集合、Map判断是否为空，其他对象toString()后判断是否为空串。null返回false
     */
    IS_EMPTY,
    /**
     * 非空断言。字符串判断是否不为空串，数组、集合、Map判断是否不为空，其他对象toString()后判断是否不为空串。null返回false
     */
    IS_NOT_EMPTY,

    /**
     * null或空或空白。字符串判断是否为空串或空白，数组、集合、Map判断是否为空，其他对象toString()后判断是否为空串或空白。null返回true
     */
    IS_BLANK,
    /**
     * 非null和空和空白。字符串判断是否不为空串和空白，数组、集合、Map判断是否不为空，其他对象toString()后判断是否不为空串和空白。null返回false
     */
    IS_NOT_BLANK;

    /**
     * 按当前逻辑对指定的值进行断言
     *
     * @param value 待检测的值
     * @return 断言结果，true:通过, false:不通过
     * @author K
     * @since 1.0.0
     */
    fun assert(value: Any?): Boolean {
        return when(this) {
            IS_NULL -> Operator.IS_NULL.assert(value, null)
            IS_NOT_NULL -> Operator.IS_NOT_NULL.assert(value, null)
            IS_EMPTY -> Operator.IS_EMPTY.assert(value, null)
            IS_NOT_EMPTY -> Operator.IS_NOT_EMPTY.assert(value, null)
            IS_BLANK -> {
                when (value) {
                    null -> true
                    is String -> value.isBlank()
                    else -> Operator.IS_NULL.assert(value, null) || Operator.IS_EMPTY.assert(value, null)
                }
            }
            IS_NOT_BLANK -> {
                when (value) {
                    null -> false
                    is String -> value.isNotBlank()
                    else -> Operator.IS_NOT_NULL.assert(value, null) && Operator.IS_NOT_EMPTY.assert(value, null)
                }
            }
        }
    }

}