package io.kuark.base.lang.math

import org.apache.commons.lang3.math.NumberUtils

/**
 * 数值工具类
 *
 * @author K
 * @since 1.0.0
 */
object NumberKit {
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.math.NumberUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // -----------------------------------------------------------------------

    /**
     * 将字符串转换为Number
     * 首先，将检查给定值的结尾类型限定符`'f','F','d','D','l','L'`。
     * 如果找到，开始尝试从指定的类型逐个创建更大的类型，直到找到一个能表示该值的类型。
     * 如果一个类型说明符也没有找到，它会检查小数点，然后从小到大地尝试类型，
     * 从Integer到BigInteger，从Float的BigDecimal
     * 一个字符串以`0x` 或 `-0x`(大写或小写)开头，它将被解释为十六进制整数。
     * 以`0`开头的则被解释为八进制。
     * 如果参数为 `null` 将返回 `null`.
     * 该方法不会对输入的字符串作trim操作。
     * 如：字符串含有前导或后导空格将抛出NumberFormatException异常.
     *
     * @param str 数值的字符串形式, 可以为null
     * @return 字符串所代表的数值，为 `null` 将返回 `null`
     * @throws NumberFormatException 如果字符串不能被转换
     * @since 1.0.0
     */
    fun createNumber(str: String?): Number = NumberUtils.createNumber(str)

    /**
     * 检查指定的字符串是否只包含数字字符
     * `Null` 或 空串将返回 `false`.
     *
     * @param str 待检查的字符串
     * @return `true` 指定的字符串只包含Unicode的数字字符
     * @since 1.0.0
     */
    fun isDigits(str: String?): Boolean = NumberUtils.isDigits(str)

    /**
     * 检查指定的字符串是否只为java的数值
     * 有效的数值包括以限定符`0x`开头的十六进制数，科学记数法和
     * 以类型限定符结尾的数值（如：123L）
     * `Null` 或 空串将返回 `false`.
     *
     * @param str 待检查的字符串
     * @return `true` 如果指定的字符串为一个正确格式的数值
     * @since 1.0.0
     */
    fun isNumber(str: String?): Boolean = NumberUtils.isNumber(str)

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.math.NumberUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}