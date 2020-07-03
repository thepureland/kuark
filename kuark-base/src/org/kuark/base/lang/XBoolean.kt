package org.kuark.base.lang

import org.apache.commons.lang3.BooleanUtils

// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// 封装org.apache.commons.lang3.BooleanUtils
// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

/**
 * 将boolean转化为int, 0当作false
 *
 * <pre>
 * true.toInt()  = 1
 * false.toInt() = 0
 * </pre>
 *
 * @return `true`返回1, `false`返回0
 * @since 1.0.0
 */
fun Boolean.toInt(): Int = BooleanUtils.toInteger(this)


/**
 * 将Boolean转化为String, 返回`'true'`, `'false'`
 *
 * <pre>
 * true.toStringTrueFalse()  = "true"
 * false.toStringTrueFalse() = "false"
 * </pre>
 *
 * @return `'true'`, `'false'`
 * @since 1.0.0
 */
fun Boolean.toStringTrueFalse(): String = BooleanUtils.toStringTrueFalse(this)

/**
 * 将Boolean转化为String, 返回`'on'`, `'off'`
 *
 * <pre>
 * true.toStringTrueFalse()  = "on"
 * false.toStringTrueFalse() = "off"
 * </pre>
 *
 * @return `'on'`, `'off'`
 * @since 1.0.0
 */
fun Boolean.toStringOnOff(): String = BooleanUtils.toStringOnOff(this)

/**
 * 将Boolean转化为String, 返回`'yes'`, `'no'`
 *
 * <pre>
 * true.toStringTrueFalse()  = "yes"
 * false.toStringTrueFalse() = "no"
 * </pre>
 *
 * @param bool 要转化的Boolean
 * @return `'yes'`, `'no'`
 * @since 1.0.0
 */
fun toStringYesNo(bool: Boolean?): String = BooleanUtils.toStringYesNo(bool)

/**
 * 将Boolean转化为String, 返回输入的某个匹配的字符串
 *
 * <pre>
 * true.toString("true", "false")   = "true"
 * false.toString("true", "false")  = "false"
 * </pre>
 *
 * @param trueString 代表 `true`的值(大小写敏感), 可以为 `null`
 * @param falseString 代表 `false`的值(大小写敏感), 可以为 `null`
 * @return 输入的某个匹配的字符串
 * @since 1.0.0
 */
fun Boolean.toString(trueString: String?, falseString: String?): String =
    BooleanUtils.toString(this, trueString, falseString)

/**
 * 对一组boolean进行逻辑与操作
 *
 * <pre>
 * [true, true].and()         = true
 * [false, false].and()       = false
 * [true, false].and()        = false
 * [true, true, false].and()  = false
 * [true, true, true].and()   = true
 * </pre>
 *
 * @return 逻辑与操作的结果
 * @throws IllegalArgumentException 如果 `array` 为空.
 * @since 1.0.0
 */
fun Array<Boolean>.and(): Boolean = BooleanUtils.and(*this)


/**
 * 对一组boolean进行逻辑或操作
 *
 * <pre>
 * [true, true].or()          = true
 * [false, false].or()        = false
 * [true, false].or()         = true
 * [true, true, false].or()   = true
 * [true, true, true].or()    = true
 * [false, false, false].or() = false
 * </pre>
 *
 * @return 逻辑或操作的结果
 * @throws IllegalArgumentException 如果 `array` 为空.
 * @since 1.0.0
 */
fun Array<Boolean>.or(): Boolean = BooleanUtils.or(*this)

/**
 * 对一组boolean进行逻辑异或操作
 *
 * <pre>
 * [true, true].xor()   = false
 * [false, false].xor() = false
 * [true, false].xor()  = true
 * [true, true].xor()   = false
 * [false, false].xor() = false
 * [true, false].xor()  = true
 * </pre>
 *
 * @return 逻辑异或操作的结果
 * @throws IllegalArgumentException 如果 `array` 为空.
 * @since 1.0.0
 */
fun Array<Boolean>.xor(): Boolean = BooleanUtils.xor(*this)

// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// 封装org.apache.commons.lang3.BooleanUtils
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^