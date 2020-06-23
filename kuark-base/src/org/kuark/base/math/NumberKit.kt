package org.kuark.base.math

import org.apache.commons.lang3.math.NumberUtils
import java.math.BigDecimal
import java.math.BigInteger
import java.text.DecimalFormat

/**
 * 数值工具类
 *
 * @since 1.0.0
 * @author admin
 * @time 2013-4-9 下午7:28:33
 */
object NumberKit {
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.math.NumberUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 将一个`String`转换为`int`， 如果转换失败返回0
     *
     *
     *
     *
     * 如果字符串为`null`，返回0
     *
     *
     * <pre>
     * NumberUtils.toInt(null) = 0
     * NumberUtils.toInt("")   = 0
     * NumberUtils.toInt("1")  = 1
    </pre> *
     *
     * @param str 要转换的字符串，可以为null，为null将返回0
     * @return 字符串的int表示，字符串为null或转换失败时将返回0
     * @since 1.0.0
     * @author admin
     * @time 2013-5-11 下午10:46:21
     */
    fun toInt(str: String?): Int {
        return NumberUtils.toInt(str)
    }

    val NUMBER = DecimalFormat("###.####")

    /**
     * 格式化Number为字符串
     * 如:10000==> 10000; 1000.1==>1000.1
     * 最大支持四位小数
     * @param number
     * @return
     */
    fun toString(number: Number?): String {
        return NUMBER.format(number)
    }

    /**
     *
     *
     * 将一个`String`转换为`int`， 如果转换失败返回指定的默认值
     *
     *
     *
     *
     * 如果字符串为`null`，返回指定的默认值
     *
     *
     * <pre>
     * NumberUtils.toInt(null, 1) = 1
     * NumberUtils.toInt("", 1)   = 1
     * NumberUtils.toInt("1", 0)  = 1
    </pre> *
     *
     * @param str 要转换的字符串，可以为null，如果为null将返回指定的默认值
     * @param defaultValue 字符串为null时返回的默认值
     * @return 字符串的int表示，字符串为null或转换失败时将返回指定的默认值
     * @since 1.0.0
     * @author admin
     * @time 2013-5-11 下午10:48:30
     */
    fun toInt(str: String?, defaultValue: Int): Int {
        return NumberUtils.toInt(str, defaultValue)
    }

    /**
     *
     *
     * 将一个`String`转换为`long`， 如果转换失败返回0
     *
     *
     *
     *
     * 如果字符串为`null`，返回0
     *
     *
     * <pre>
     * NumberUtils.toLong(null) = 0L
     * NumberUtils.toLong("")   = 0L
     * NumberUtils.toLong("1")  = 1L
    </pre> *
     *
     * @param str 要转换的字符串，可以为null，为null将返回0
     * @return 字符串的long表示，字符串为null或转换失败时将返回0
     * @since 1.0.0
     * @author admin
     * @time 2013-5-11 下午10:49:52
     */
    fun toLong(str: String?): Long {
        return NumberUtils.toLong(str)
    }

    /**
     *
     *
     * 将一个`String`转换为`long`， 如果转换失败返回指定的默认值
     *
     *
     *
     *
     * 如果字符串为`null`，返回指定的默认值
     *
     *
     * <pre>
     * NumberUtils.toLong(null, 1L) = 1L
     * NumberUtils.toLong("", 1L)   = 1L
     * NumberUtils.toLong("1", 0L)  = 1L
    </pre> *
     *
     * @param str 要转换的字符串，可以为null，如果为null将返回指定的默认值
     * @param defaultValue 字符串为null时返回的默认值
     * @return 字符串的long表示，字符串为null或转换失败时将返回指定的默认值
     * @since 1.0.0
     * @author admin
     * @time 2013-5-11 下午10:51:12
     */
    fun toLong(str: String?, defaultValue: Long): Long {
        return NumberUtils.toLong(str, defaultValue)
    }

    /**
     *
     *
     * 将一个`String`转换为`float`， 如果转换失败返回0.0f
     *
     *
     *
     *
     * 如果字符串为`null`，返回0.0f
     *
     *
     * <pre>
     * NumberUtils.toFloat(null)   = 0.0f
     * NumberUtils.toFloat("")     = 0.0f
     * NumberUtils.toFloat("1.5")  = 1.5f
    </pre> *
     *
     * @param str 要转换的字符串，可以为null，为null将返回0.0f
     * @return 字符串的float表示，字符串为null或转换失败时将返回0.0f
     * @since 1.0.0
     * @author admin
     * @time 2013-5-11 下午10:52:27
     */
    fun toFloat(str: String?): Float {
        return NumberUtils.toFloat(str)
    }

    /**
     *
     *
     * 将一个`String`转换为`float`， 如果转换失败返回指定的默认值
     *
     *
     *
     *
     * 如果字符串为`null`，返回指定的默认值
     *
     *
     * <pre>
     * NumberUtils.toFloat(null, 1.1f)   = 1.0f
     * NumberUtils.toFloat("", 1.1f)     = 1.1f
     * NumberUtils.toFloat("1.5", 0.0f)  = 1.5f
    </pre> *
     *
     * @param str 要转换的字符串，可以为null，如果为null将返回指定的默认值
     * @param defaultValue 字符串为null时返回的默认值
     * @return 字符串的float表示，字符串为null或转换失败时将返回指定的默认值
     * @since 1.0.0
     * @author admin
     * @time 2013-5-11 下午10:53:58
     */
    fun toFloat(str: String?, defaultValue: Float): Float {
        return NumberUtils.toFloat(str, defaultValue)
    }

    /**
     *
     *
     * 将一个`String`转换为`double`， 如果转换失败返回0.0d
     *
     *
     *
     *
     * 如果字符串为`null`，返回0.0d
     *
     *
     * <pre>
     * NumberUtils.toDouble(null)   = 0.0d
     * NumberUtils.toDouble("")     = 0.0d
     * NumberUtils.toDouble("1.5")  = 1.5d
    </pre> *
     *
     * @param str 要转换的字符串，可以为null，为null将返回0.0d
     * @return 字符串的double表示，字符串为null或转换失败时将返回0.0d
     * @since 1.0.0
     * @author admin
     * @time 2013-5-11 下午10:55:09
     */
    fun toDouble(str: String?): Double {
        return NumberUtils.toDouble(str)
    }

    /**
     *
     *
     * 将一个`String`转换为`double`， 如果转换失败返回指定的默认值
     *
     *
     *
     *
     * 如果字符串为`null`，返回指定的默认值
     *
     *
     * <pre>
     * NumberUtils.toDouble(null, 1.1d)   = 1.1d
     * NumberUtils.toDouble("", 1.1d)     = 1.1d
     * NumberUtils.toDouble("1.5", 0.0d)  = 1.5d
    </pre> *
     *
     * @param str 要转换的字符串，可以为null，如果为null将返回指定的默认值
     * @param defaultValue 字符串为null时返回的默认值
     * @return 字符串的double表示，字符串为null或转换失败时将返回指定的默认值
     * @since 1.0.0
     * @author admin
     * @time 2013-5-11 下午10:56:34
     */
    fun toDouble(str: String?, defaultValue: Double): Double {
        return NumberUtils.toDouble(str, defaultValue)
    }
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 将一个`String`转换为`byte`， 如果转换失败返回0
     *
     *
     *
     *
     * 如果字符串为`null`，返回0
     *
     *
     * <pre>
     * NumberUtils.toByte(null) = 0
     * NumberUtils.toByte("")   = 0
     * NumberUtils.toByte("1")  = 1
    </pre> *
     *
     * @param str 要转换的字符串，可以为null，为null将返回0
     * @return 字符串的byte表示，字符串为null或转换失败时将返回0
     * @since 1.0.0
     * @author admin
     * @time 2013-5-11 下午10:59:00
     */
    fun toByte(str: String?): Byte {
        return NumberUtils.toByte(str)
    }

    /**
     *
     *
     * 将一个`String`转换为`byte`， 如果转换失败返回指定的默认值
     *
     *
     *
     *
     * 如果字符串为`null`，返回指定的默认值
     *
     *
     * <pre>
     * NumberUtils.toByte(null, 1) = 1
     * NumberUtils.toByte("", 1)   = 1
     * NumberUtils.toByte("1", 0)  = 1
    </pre> *
     *
     * @param str 要转换的字符串，可以为null，如果为null将返回指定的默认值
     * @param defaultValue 字符串为null时返回的默认值
     * @return 字符串的byte表示，字符串为null或转换失败时将返回指定的默认值
     * @since 1.0.0
     * @author admin
     * @time 2013-5-11 下午11:00:04
     */
    fun toByte(str: String?, defaultValue: Byte): Byte {
        return NumberUtils.toByte(str, defaultValue)
    }

    /**
     *
     *
     * 将一个`String`转换为`short`， 如果转换失败返回0
     *
     *
     *
     *
     * 如果字符串为`null`，返回0
     *
     *
     * <pre>
     * NumberUtils.toShort(null) = 0
     * NumberUtils.toShort("")   = 0
     * NumberUtils.toShort("1")  = 1
    </pre> *
     *
     * @param str 要转换的字符串，可以为null，为null将返回0
     * @return 字符串的short表示，字符串为null或转换失败时将返回0
     * @since 1.0.0
     * @author admin
     * @time 2013-5-11 下午11:02:07
     */
    fun toShort(str: String?): Short {
        return NumberUtils.toShort(str)
    }

    /**
     *
     *
     * 将一个`String`转换为`short`， 如果转换失败返回指定的默认值
     *
     *
     *
     *
     * 如果字符串为`null`，返回指定的默认值
     *
     *
     * <pre>
     * NumberUtils.toShort(null, 1) = 1
     * NumberUtils.toShort("", 1)   = 1
     * NumberUtils.toShort("1", 0)  = 1
    </pre> *
     *
     * @param str 要转换的字符串，可以为null，为null将返回0
     * @param defaultValue 字符串为null时返回的默认值
     * @return 字符串的short表示，字符串为null或转换失败时将返回指定的默认值
     * @since 1.0.0
     * @author admin
     * @time 2013-5-11 下午11:08:39
     */
    fun toShort(str: String?, defaultValue: Short): Short {
        return NumberUtils.toShort(str, defaultValue)
    }
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 将字符串转换为java.lang.Number
     *
     *
     *
     *
     * 首先，将检查给定值的结尾类型限定符`'f','F','d','D','l','L'`。
     * 如果找到，开始尝试从指定的类型逐个创建更大的类型，直到找到一个能表示该值的类型。
     *
     *
     *
     *
     * 如果一个类型说明符也没有找到，它会检查小数点，然后从小到大地尝试类型，
     * 从Integer到BigInteger，从Float的BigDecimal
     *
     *
     *
     *
     * 一个字符串以`0x` 或 `-0x`(大写或小写)开头，它将被解释为十六进制整数。
     * 以`0`开头的则被解释为八进制。
     *
     *
     *
     *
     * 如果参数为 `null` 将返回 `null`.
     *
     *
     *
     *
     * 该方法不会对输入的字符串作trim操作。
     * 如：字符串含有前导或后导空格将抛出NumberFormatException异常.
     *
     *
     * @param str 数值的字符串形式, 可以为null
     * @return 字符串所代表的数值，为 `null` 将返回 `null`
     * @throws NumberFormatException 如果字符串不能被转换
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:13:17
     */
    @Throws(NumberFormatException::class)
    fun createNumber(str: String?): Number {
        return NumberUtils.createNumber(str)
    }
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 将一个`String`转换为`Float`
     *
     *
     *
     *
     * 如果参数为 `null` 将返回 `null`.
     *
     *
     * @param str 待转换的字符串, 可以为null，为null将返回null
     * @return 转换后的 `Float` 值，参数为 `null` 将返回 `null`
     * @throws NumberFormatException 如果字符串不能被转换
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:15:22
     */
    fun createFloat(str: String?): Float {
        return NumberUtils.createFloat(str)
    }

    /**
     *
     *
     * 将一个`String`转换为`Double`
     *
     *
     *
     *
     * 如果参数为 `null` 将返回 `null`.
     *
     *
     * @param str 待转换的字符串, 可以为null，为null将返回null
     * @return 转换后的 `Double` 值，参数为 `null` 将返回 `null`
     * @throws NumberFormatException 如果字符串不能被转换
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:15:22
     */
    fun createDouble(str: String?): Double {
        return NumberUtils.createDouble(str)
    }

    /**
     *
     *
     * 将一个`String`转换为`Integer`
     *
     *
     *
     *
     * 如果参数为 `null` 将返回 `null`.
     *
     *
     * @param str 待转换的字符串, 可以为null，为null将返回null
     * @return 转换后的 `Integer` 值，参数为 `null` 将返回 `null`
     * @throws NumberFormatException 如果字符串不能被转换
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:15:22
     */
    fun createInteger(str: String?): Int {
        return NumberUtils.createInteger(str)
    }

    /**
     *
     *
     * 将一个`String`转换为`Long`
     *
     *
     *
     *
     * 如果参数为 `null` 将返回 `null`.
     *
     *
     * @param str 待转换的字符串, 可以为null，为null将返回null
     * @return 转换后的 `Long` 值，参数为 `null` 将返回 `null`
     * @throws NumberFormatException 如果字符串不能被转换
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:15:22
     */
    fun createLong(str: String?): Long {
        return NumberUtils.createLong(str)
    }

    /**
     *
     *
     * 将一个`String`转换为`BigInteger`
     *
     *
     *
     *
     * 如果参数为 `null` 将返回 `null`.
     *
     *
     * @param str 待转换的字符串, 可以为null，为null将返回null
     * @return 转换后的 `BigInteger` 值，参数为 `null` 将返回 `null`
     * @throws NumberFormatException 如果字符串不能被转换
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:15:22
     */
    fun createBigInteger(str: String?): BigInteger {
        return NumberUtils.createBigInteger(str)
    }

    /**
     *
     *
     * 将一个`String`转换为`BigDecimal`
     *
     *
     *
     *
     * 如果参数为 `null` 将返回 `null`.
     *
     *
     * @param str 待转换的字符串, 可以为null，为null将返回null
     * @return 转换后的 `BigDecimal` 值，参数为 `null` 将返回 `null`
     * @throws NumberFormatException 如果字符串不能被转换
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:15:22
     */
    fun createBigDecimal(str: String?): BigDecimal {
        return NumberUtils.createBigDecimal(str)
    }
    // Min in array
    // --------------------------------------------------------------------
    /**
     *
     *
     * 返回数组中的最小元素
     *
     *
     * @param array 待检测的数组, 不能为null或空
     * @return 数组中的最小元素
     * @throws IllegalArgumentException 如果数组为null或空
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:18:15
     */
    fun min(array: LongArray): Long {
        return NumberUtils.min(*array)
    }

    /**
     *
     *
     * 返回数组中的最小元素
     *
     *
     * @param array 待检测的数组, 不能为null或空
     * @return 数组中的最小元素
     * @throws IllegalArgumentException 如果数组为null或空
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:18:15
     */
    fun min(array: IntArray): Int {
        return NumberUtils.min(*array)
    }

    /**
     *
     *
     * 返回数组中的最小元素
     *
     *
     * @param array 待检测的数组, 不能为null或空
     * @return 数组中的最小元素
     * @throws IllegalArgumentException 如果数组为null或空
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:18:15
     */
    fun min(array: ShortArray): Short {
        return NumberUtils.min(*array)
    }

    /**
     *
     *
     * 返回数组中的最小元素
     *
     *
     * @param array 待检测的数组, 不能为null或空
     * @return 数组中的最小元素
     * @throws IllegalArgumentException 如果数组为null或空
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:18:15
     */
    fun min(array: ByteArray): Byte {
        return NumberUtils.min(*array)
    }

    /**
     *
     *
     * 返回数组中的最小元素
     *
     *
     * @param array 待检测的数组, 不能为null或空
     * @return 数组中的最小元素
     * @throws IllegalArgumentException 如果数组为null或空
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:18:15
     */
    fun min(array: DoubleArray): Double {
        return NumberUtils.min(*array)
    }

    /**
     *
     *
     * 返回数组中的最小元素
     *
     *
     * @param array 待检测的数组, 不能为null或空
     * @return 数组中的最小元素
     * @throws IllegalArgumentException 如果数组为null或空
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:18:15
     */
    fun min(array: FloatArray): Float {
        return NumberUtils.min(*array)
    }
    // Max in array
    // --------------------------------------------------------------------
    /**
     *
     *
     * 返回数组中的最大元素
     *
     *
     * @param array 待检测的数组, 不能为null或空
     * @return 数组中的最大小元素
     * @throws IllegalArgumentException 如果数组为null或空
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:18:15
     */
    fun max(array: LongArray): Long {
        return NumberUtils.max(*array)
    }

    /**
     *
     *
     * 返回数组中的最大元素
     *
     *
     * @param array 待检测的数组, 不能为null或空
     * @return 数组中的最大小元素
     * @throws IllegalArgumentException 如果数组为null或空
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:18:15
     */
    fun max(array: IntArray): Int {
        return NumberUtils.max(*array)
    }

    /**
     *
     *
     * 返回数组中的最大元素
     *
     *
     * @param array 待检测的数组, 不能为null或空
     * @return 数组中的最大小元素
     * @throws IllegalArgumentException 如果数组为null或空
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:18:15
     */
    fun max(array: ShortArray): Short {
        return NumberUtils.max(*array)
    }

    /**
     *
     *
     * 返回数组中的最大元素
     *
     *
     * @param array 待检测的数组, 不能为null或空
     * @return 数组中的最大小元素
     * @throws IllegalArgumentException 如果数组为null或空
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:18:15
     */
    fun max(array: ByteArray): Byte {
        return NumberUtils.max(*array)
    }

    /**
     *
     *
     * 返回数组中的最大元素
     *
     *
     * @param array 待检测的数组, 不能为null或空
     * @return 数组中的最大小元素
     * @throws IllegalArgumentException 如果数组为null或空
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:18:15
     */
    fun max(array: DoubleArray): Double {
        return NumberUtils.max(*array)
    }

    /**
     *
     *
     * 返回数组中的最大元素
     *
     *
     * @param array 待检测的数组, 不能为null或空
     * @return 数组中的最大小元素
     * @throws IllegalArgumentException 如果数组为null或空
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:18:15
     */
    fun max(array: FloatArray): Float {
        return NumberUtils.max(*array)
    }
    // 3 param min
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 返回三个值中的最小值
     *
     *
     * @param a 第一个值
     * @param b 第二个值
     * @param c 第三个值
     * @return 最小值
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:20:05
     */
    fun min(a: Long, b: Long, c: Long): Long {
        return NumberUtils.min(a, b, c)
    }

    /**
     *
     *
     * 返回三个值中的最小值
     *
     *
     * @param a 第一个值
     * @param b 第二个值
     * @param c 第三个值
     * @return 最小值
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:20:05
     */
    fun min(a: Int, b: Int, c: Int): Int {
        return NumberUtils.min(a, b, c)
    }

    /**
     *
     *
     * 返回三个值中的最小值
     *
     *
     * @param a 第一个值
     * @param b 第二个值
     * @param c 第三个值
     * @return 最小值
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:20:05
     */
    fun min(a: Short, b: Short, c: Short): Short {
        return NumberUtils.min(a, b, c)
    }

    /**
     *
     *
     * 返回三个值中的最小值
     *
     *
     * @param a 第一个值
     * @param b 第二个值
     * @param c 第三个值
     * @return 最小值
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:20:05
     */
    fun min(a: Byte, b: Byte, c: Byte): Byte {
        return NumberUtils.min(a, b, c)
    }

    /**
     *
     *
     * 返回三个值中的最小值
     *
     *
     *
     *
     * 如果任何参数为`NaN`，将返回`NaN`。支持无穷大/小
     *
     *
     * @param a 第一个值
     * @param b 第二个值
     * @param c 第三个值
     * @return 最小值
     * [Symbol][IEEE754rUtils.min]
     * @see IEEE754rUtils.min
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:20:05
     */
    fun min(a: Double, b: Double, c: Double): Double {
        return NumberUtils.min(a, b, c)
    }

    /**
     *
     *
     * 返回三个值中的最小值
     *
     *
     *
     *
     * 如果任何参数为`NaN`，将返回`NaN`。支持无穷大/小
     *
     *
     * @param a 第一个值
     * @param b 第二个值
     * @param c 第三个值
     * @return 最小值
     * @see NumberUtils.min
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:20:05
     */
    fun min(a: Float, b: Float, c: Float): Float {
        return NumberUtils.min(a, b, c)
    }
    // 3 param max
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 返回三个值中的最大值
     *
     *
     * @param a 第一个值
     * @param b 第二个值
     * @param c 第三个值
     * @return 最大值
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:20:05
     */
    fun max(a: Long, b: Long, c: Long): Long {
        return NumberUtils.min(a, b, c)
    }

    /**
     *
     *
     * 返回三个值中的最大值
     *
     *
     * @param a 第一个值
     * @param b 第二个值
     * @param c 第三个值
     * @return 最大值
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:20:05
     */
    fun max(a: Int, b: Int, c: Int): Int {
        return NumberUtils.min(a, b, c)
    }

    /**
     *
     *
     * 返回三个值中的最大值
     *
     *
     * @param a 第一个值
     * @param b 第二个值
     * @param c 第三个值
     * @return 最大值
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:20:05
     */
    fun max(a: Short, b: Short, c: Short): Short {
        return NumberUtils.min(a, b, c)
    }

    /**
     *
     *
     * 返回三个值中的最大值
     *
     *
     * @param a 第一个值
     * @param b 第二个值
     * @param c 第三个值
     * @return 最大值
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:20:05
     */
    fun max(a: Byte, b: Byte, c: Byte): Byte {
        return NumberUtils.min(a, b, c)
    }

    /**
     *
     *
     * 返回三个值中的最大值
     *
     *
     *
     *
     * 如果任何参数为`NaN`，将返回`NaN`。支持无穷大/小
     *
     *
     * @param a 第一个值
     * @param b 第二个值
     * @param c 第三个值
     * @return 最大值
     * @see IEEE754rUtils.max
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:20:05
     */
    fun max(a: Double, b: Double, c: Double): Double {
        return NumberUtils.max(a, b, c)
    }

    /**
     *
     *
     * 返回三个值中的最大值
     *
     *
     *
     *
     * 如果任何参数为`NaN`，将返回`NaN`。支持无穷大/小
     *
     *
     * @param a 第一个值
     * @param b 第二个值
     * @param c 第三个值
     * @return 最大值
     * @see IEEE754rUtils.max
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:20:05
     */
    fun max(a: Float, b: Float, c: Float): Float {
        return NumberUtils.max(a, b, c)
    }
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 检查指定的字符串是否只包含数字字符
     *
     *
     *
     *
     * `Null` 或 空串将返回 `false`.
     *
     *
     * @param str 待检查的字符串
     * @return `true` 指定的字符串只包含Unicode的数字字符
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:25:11
     */
    fun isDigits(str: String?): Boolean {
        return NumberUtils.isDigits(str)
    }

    /**
     *
     *
     * 检查指定的字符串是否只为java的数值
     *
     *
     *
     *
     * 有效的数值包括以限定符`0x`开头的十六进制数，科学记数法和
     * 以类型限定符结尾的数值（如：123L）
     *
     *
     *
     *
     * `Null` 或 空串将返回 `false`.
     *
     *
     * @param str 待检查的字符串
     * @return `true` 如果指定的字符串为一个正确格式的数值
     * @since 1.0.0
     * @author admin
     * @time 2013-5-12 下午11:28:14
     */
    fun isNumber(str: String?): Boolean {
        return NumberUtils.isNumber(str)
    } // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.math.NumberUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}