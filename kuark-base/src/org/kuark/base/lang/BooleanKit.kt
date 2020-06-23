package org.kuark.base.lang

import org.apache.commons.lang3.BooleanUtils

/**
 * 布尔处理工具类
 *
 * @since 1.0.0
 * @author admin
 * @time 2013-4-9 下午8:08:34
 */
object BooleanKit {
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.BooleanUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    /**
     *
     *
     * 对指定的布尔值取反
     *
     *
     *
     *
     *
     * 如果传入`null`, 将返回`null`.
     *
     *
     *
     *
     * 注意: 返回null的结果如果自动拆箱为boolean, 将产生NullPointerException异常
     *
     *
     * <pre>
     * BooleanUtils.negate(Boolean.TRUE) = Boolean.FALSE;
     * BooleanUtils.negate(Boolean.FALSE) = Boolean.TRUE;
     * BooleanUtils.negate(null) = null;
    </pre> *
     *
     * @param bool 要取反的布尔值, 可以为null
     * @return 指定布尔值的非运算结果, 输入`null`将返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午3:32:49
     */
    fun negate(bool: Boolean?): Boolean {
        return BooleanUtils.negate(bool)
    }
    // boolean Boolean methods
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 检查指定的`Boolean` 值是否为`true`, `null`将返回`false`
     *
     *
     * <pre>
     * BooleanUtils.isTrue(Boolean.TRUE)  = true
     * BooleanUtils.isTrue(Boolean.FALSE) = false
     * BooleanUtils.isTrue(null)          = false
    </pre> *
     *
     * @param bool 要检查的布尔值, null 将返回 `false`
     * @return `true` 仅当输入的参数不为null并且为true时
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午3:34:57
     */
    fun isTrue(bool: Boolean?): Boolean {
        return BooleanUtils.isTrue(bool)
    }

    /**
     *
     *
     * 检查指定的`Boolean` 值是否不为`true`, `null`将返回`true`
     *
     *
     * <pre>
     * BooleanUtils.isNotTrue(Boolean.TRUE)  = false
     * BooleanUtils.isNotTrue(Boolean.FALSE) = true
     * BooleanUtils.isNotTrue(null)          = true
    </pre> *
     *
     * @param bool 要检查的布尔值, null 将返回 `true`
     * @return `true` 如果输入的参数为null或false时
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午3:36:26
     */
    fun isNotTrue(bool: Boolean?): Boolean {
        return BooleanUtils.isNotTrue(bool)
    }

    /**
     *
     *
     * 检查指定的`Boolean` 值是否为`false`, `null`将返回`false`
     *
     *
     * <pre>
     * BooleanUtils.isFalse(Boolean.TRUE)  = false
     * BooleanUtils.isFalse(Boolean.FALSE) = true
     * BooleanUtils.isFalse(null)          = false
    </pre> *
     *
     * @param bool 要检查的布尔值, null 将返回 `false`
     * @return `true` 仅当输入的参数不为null并且为false时
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午3:37:45
     */
    fun isFalse(bool: Boolean?): Boolean {
        return BooleanUtils.isFalse(bool)
    }

    /**
     *
     *
     * 检查指定的`Boolean` 值是否不为`false`, `null`将返回`true`
     *
     *
     * <pre>
     * BooleanUtils.isNotFalse(Boolean.TRUE)  = true
     * BooleanUtils.isNotFalse(Boolean.FALSE) = false
     * BooleanUtils.isNotFalse(null)          = true
    </pre> *
     *
     * @param bool 要检查的布尔值, null 将返回 `true`
     * @return `true` 如果输入的参数为null或true时
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午3:38:53
     */
    fun isNotFalse(bool: Boolean?): Boolean {
        return BooleanUtils.isNotFalse(bool)
    }
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 将Boolean转化为boolean, `null` 将返回 `false`.
     *
     *
     * <pre>
     * BooleanUtils.toBoolean(Boolean.TRUE)  = true
     * BooleanUtils.toBoolean(Boolean.FALSE) = false
     * BooleanUtils.toBoolean(null)          = false
    </pre> *
     *
     * @param bool 要转化的Boolean值
     * @return `true` 或 `false`, `null` 将返回 `false`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午3:42:59
     */
    fun toBoolean(bool: Boolean?): Boolean {
        return BooleanUtils.toBoolean(bool)
    }

    /**
     *
     *
     * 将Boolean转化为boolean, 输入`null`时将返回指定的默认值
     *
     *
     * <pre>
     * BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false) = true
     * BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true) = false
     * BooleanUtils.toBooleanDefaultIfNull(null, true)          = true
    </pre> *
     *
     * @param bool 要转化的Boolean值
     * @param valueIfNull 布尔值为`null`时返回的值
     * @return `true` 或 `false`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午3:44:39
     */
    fun toBooleanDefaultIfNull(bool: Boolean?, valueIfNull: Boolean): Boolean {
        return BooleanUtils.toBooleanDefaultIfNull(bool, valueIfNull)
    }
    // Integer to Boolean methods
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 将int转化为boolean, 0将当作false
     *
     *
     * <pre>
     * BooleanUtils.toBoolean(0) = false
     * BooleanUtils.toBoolean(1) = true
     * BooleanUtils.toBoolean(2) = true
    </pre> *
     *
     * @param value 要转化的int值
     * @return `true` 如果非0, `false` 如果是0
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午3:48:52
     */
    fun toBoolean(value: Int): Boolean {
        return BooleanUtils.toBoolean(value)
    }

    /**
     *
     *
     * 将int转化为Boolean, 0将当作false
     *
     *
     * <pre>
     * BooleanUtils.toBoolean(0) = Boolean.FALSE
     * BooleanUtils.toBoolean(1) = Boolean.TRUE
     * BooleanUtils.toBoolean(2) = Boolean.TRUE
    </pre> *
     *
     * @param value 要转化的int值
     * @return Boolean.TRUE 如果非0, Boolean.FALSE 如果是0
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午3:50:37
     */
    fun toBooleanObject(value: Int): Boolean {
        return BooleanUtils.toBooleanObject(value)
    }

    /**
     *
     *
     * 将Integer转化为Boolean, 0将当作false
     *
     *
     *
     *
     * `null` 将返回 `null`.
     *
     *
     *
     *
     * 注意: 返回null的结果如果自动拆箱为boolean, 将产生NullPointerException异常
     *
     *
     * <pre>
     * BooleanUtils.toBoolean(Integer.valueOf(0))    = Boolean.FALSE
     * BooleanUtils.toBoolean(Integer.valueOf(1))    = Boolean.TRUE
     * BooleanUtils.toBoolean(Integer.valueOf(null)) = null
    </pre> *
     *
     * @param value 要转化的Integer值
     * @return Boolean.TRUE 如果非0, Boolean.FALSE 如果是0, `null`将返回`null` input
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午3:58:48
     */
    fun toBooleanObject(value: Int?): Boolean {
        return BooleanUtils.toBooleanObject(value)
    }

    /**
     *
     *
     * 将int转化为boolean, 使用指定的转换规则值
     *
     *
     * <pre>
     * BooleanUtils.toBoolean(0, 1, 0) = false
     * BooleanUtils.toBoolean(1, 1, 0) = true
     * BooleanUtils.toBoolean(2, 1, 2) = false
     * BooleanUtils.toBoolean(2, 2, 0) = true
    </pre> *
     *
     * @param value 要转化的int值
     * @param trueValue 代表 `true`的值
     * @param falseValue 代表 `false`的值
     * @return `true` 或 `false`
     * @throws IllegalArgumentException 如果不匹配
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午4:02:28
     */
    fun toBoolean(value: Int, trueValue: Int, falseValue: Int): Boolean {
        return BooleanUtils.toBoolean(value, trueValue, falseValue)
    }

    /**
     *
     *
     * 将Integer转化为boolean, 使用指定的转换规则值
     *
     *
     * <pre>
     * BooleanUtils.toBoolean(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0)) = false
     * BooleanUtils.toBoolean(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0)) = true
     * BooleanUtils.toBoolean(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(2)) = false
     * BooleanUtils.toBoolean(Integer.valueOf(2), Integer.valueOf(2), Integer.valueOf(0)) = true
     * BooleanUtils.toBoolean(null, null, Integer.valueOf(0))                     = true
    </pre> *
     *
     * @param value 要转化的Integer值
     * @param trueValue 代表 `true`的值, 可以为 `null`
     * @param falseValue 代表 `false`的值, 可以为 `null`
     * @return `true` 或 `false`
     * @throws IllegalArgumentException 如果没有匹配
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午4:04:22
     */
    fun toBoolean(value: Int?, trueValue: Int?, falseValue: Int?): Boolean {
        return BooleanUtils.toBoolean(value, trueValue, falseValue)
    }

    /**
     *
     *
     * 将int转化为Boolean, 使用指定的转换规则值
     *
     *
     *
     *
     * 注意: 返回null的结果如果自动拆箱为boolean, 将产生NullPointerException异常
     *
     *
     * <pre>
     * BooleanUtils.toBooleanObject(0, 0, 2, 3) = Boolean.TRUE
     * BooleanUtils.toBooleanObject(2, 1, 2, 3) = Boolean.FALSE
     * BooleanUtils.toBooleanObject(3, 1, 2, 3) = null
    </pre> *
     *
     * @param value 要转化的int值
     * @param trueValue 代表 `true`的值
     * @param falseValue 代表 `false`的值
     * @param nullValue 代表 `null`的值
     * @return Boolean.TRUE, Boolean.FALSE, 或 `null`
     * @throws IllegalArgumentException 如果没有匹配
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午4:07:47
     */
    fun toBooleanObject(value: Int, trueValue: Int, falseValue: Int, nullValue: Int): Boolean {
        return BooleanUtils.toBooleanObject(value, trueValue, falseValue, nullValue)
    }

    /**
     *
     *
     * 将Integer转化为Boolean, 使用指定的转换规则值
     *
     *
     *
     *
     * 注意: 返回null的结果如果自动拆箱为boolean, 将产生NullPointerException异常
     *
     *
     * <pre>
     * BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(2), Integer.valueOf(3)) = Boolean.TRUE
     * BooleanUtils.toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)) = Boolean.FALSE
     * BooleanUtils.toBooleanObject(Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)) = null
    </pre> *
     *
     * @param value 要转化的Integer值
     * @param trueValue 代表 `true`的值, 可以为 `null`
     * @param falseValue 代表 `false`的值, 可以为 `null`
     * @param nullValue 代表 `null`的值, 可以为 `null`
     * @return Boolean.TRUE, Boolean.FALSE, 或 `null`
     * @throws IllegalArgumentException 如果没有匹配
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午4:09:57
     */
    fun toBooleanObject(value: Int?, trueValue: Int?, falseValue: Int?, nullValue: Int?): Boolean {
        return BooleanUtils.toBooleanObject(value, trueValue, falseValue, nullValue)
    }
    // Boolean to Integer methods
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 将boolean转化为int, 0当作false
     *
     *
     * <pre>
     * BooleanUtils.toInteger(true)  = 1
     * BooleanUtils.toInteger(false) = 0
    </pre> *
     *
     * @param bool 要转化的boolean值
     * @return `true`返回1, `false`返回0
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午4:24:20
     */
    fun toInteger(bool: Boolean): Int {
        return BooleanUtils.toInteger(bool)
    }

    /**
     *
     *
     * 将boolean转化为Integer, 0当作false
     *
     *
     * <pre>
     * BooleanUtils.toIntegerObject(true)  = Integer.valueOf(1)
     * BooleanUtils.toIntegerObject(false) = Integer.valueOf(0)
    </pre> *
     *
     * @param bool 要转化的boolean值
     * @return `true`返回1, `false`返回0
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午4:26:35
     */
    fun toIntegerObject(bool: Boolean): Int {
        return BooleanUtils.toIntegerObject(bool)
    }

    /**
     *
     *
     * 将Boolean转化为Integer, 0当作false
     *
     *
     *
     *
     * `null` 将返回 `null`.
     *
     *
     * <pre>
     * BooleanUtils.toIntegerObject(Boolean.TRUE)  = Integer.valueOf(1)
     * BooleanUtils.toIntegerObject(Boolean.FALSE) = Integer.valueOf(0)
    </pre> *
     *
     * @param bool 要转化的Boolean值
     * @return `true`返回1, `false`返回0, `null`返回`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午4:27:57
     */
    fun toIntegerObject(bool: Boolean?): Int {
        return BooleanUtils.toIntegerObject(bool)
    }

    /**
     *
     *
     * 将boolean转化为int, 使用指定的转换规则值
     *
     *
     * <pre>
     * BooleanUtils.toInteger(true, 1, 0)  = 1
     * BooleanUtils.toInteger(false, 1, 0) = 0
    </pre> *
     *
     * @param bool 要转化的boolean值
     * @param trueValue 代表 `true`的值
     * @param falseValue 代表 `false`的值
     * @return int值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午4:33:33
     */
    fun toInteger(bool: Boolean, trueValue: Int, falseValue: Int): Int {
        return BooleanUtils.toInteger(bool, trueValue, falseValue)
    }

    /**
     *
     *
     * 将Boolean转化为int, 使用指定的转换规则值
     *
     *
     * <pre>
     * BooleanUtils.toInteger(Boolean.TRUE, 1, 0, 2)  = 1
     * BooleanUtils.toInteger(Boolean.FALSE, 1, 0, 2) = 0
     * BooleanUtils.toInteger(null, 1, 0, 2)          = 2
    </pre> *
     *
     * @param bool 要转化的Boolean值
     * @param trueValue 代表 `true`的值
     * @param falseValue 代表 `false`的值
     * @param nullValue 代表 `null`的值
     * @return int值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午4:35:16
     */
    fun toInteger(bool: Boolean?, trueValue: Int, falseValue: Int, nullValue: Int): Int {
        return BooleanUtils.toIntegerObject(bool, trueValue, falseValue, nullValue)
    }

    /**
     *
     *
     * 将boolean转化为Integer, 使用指定的转换规则值
     *
     *
     * <pre>
     * BooleanUtils.toIntegerObject(true, Integer.valueOf(1), Integer.valueOf(0))  = Integer.valueOf(1)
     * BooleanUtils.toIntegerObject(false, Integer.valueOf(1), Integer.valueOf(0)) = Integer.valueOf(0)
    </pre> *
     *
     * @param bool 要转化的Boolean值
     * @param trueValue 代表 `true`的值, 可以为 `null`
     * @param falseValue 代表 `false`的值, 可以为 `null`
     * @return int值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午4:37:08
     */
    fun toIntegerObject(bool: Boolean, trueValue: Int?, falseValue: Int?): Int {
        return BooleanUtils.toIntegerObject(bool, trueValue, falseValue)
    }

    /**
     *
     *
     * 将Boolean转化为Integer, 使用指定的转换规则值
     *
     *
     * <pre>
     * BooleanUtils.toIntegerObject(Boolean.TRUE, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(2))  = Integer.valueOf(1)
     * BooleanUtils.toIntegerObject(Boolean.FALSE, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(2)) = Integer.valueOf(0)
     * BooleanUtils.toIntegerObject(null, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(2))          = Integer.valueOf(2)
    </pre> *
     *
     * @param bool 要转化的Boolean值
     * @param trueValue 代表 `true`的值, 可以为 `null`
     * @param falseValue 代表 `false`的值, 可以为 `null`
     * @param nullValue 代表 `null`的值, 可以为 `null`
     * @return Integer值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午4:45:52
     */
    fun toIntegerObject(bool: Boolean?, trueValue: Int?, falseValue: Int?, nullValue: Int?): Int {
        return BooleanUtils.toIntegerObject(bool, trueValue, falseValue, nullValue)
    }
    // String to Boolean methods
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 将String转化为Boolean
     *
     *
     *
     *
     * `'true'`, `'on'` 或 `'yes'` (大小写不敏感) 将返回 `true`. `'false'`, `'off'` 或
     * `'no'` (大小写不敏感) 将返回 `false`. 否则, 返回`null`.
     *
     *
     *
     *
     * 注意: 返回null的结果如果自动拆箱为boolean, 将产生NullPointerException异常
     *
     *
     * <pre>
     * BooleanUtils.toBooleanObject(null)    = null
     * BooleanUtils.toBooleanObject("true")  = Boolean.TRUE
     * BooleanUtils.toBooleanObject("false") = Boolean.FALSE
     * BooleanUtils.toBooleanObject("on")    = Boolean.TRUE
     * BooleanUtils.toBooleanObject("ON")    = Boolean.TRUE
     * BooleanUtils.toBooleanObject("off")   = Boolean.FALSE
     * BooleanUtils.toBooleanObject("oFf")   = Boolean.FALSE
     * BooleanUtils.toBooleanObject("1")   = Boolean.FALSE
     * BooleanUtils.toBooleanObject("0")   = Boolean.FALSE
     * BooleanUtils.toBooleanObject("blue")  = null
    </pre> *
     *
     * @param str 要转化的字符串
     * @return 字符串的布尔值, 不匹配或输入null将返回null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午4:51:44
     */
    fun toBooleanObject(str: String): Boolean {
        if ("1" == str) {
            return java.lang.Boolean.TRUE
        }
        return if ("0" == str) {
            java.lang.Boolean.FALSE
        } else BooleanUtils.toBooleanObject(str)
    }

    /**
     *
     *
     * 将String转化为Boolean, 如果没有匹配将抛出异常
     *
     *
     *
     *
     * 注意: 返回null的结果如果自动拆箱为boolean, 将产生NullPointerException异常
     *
     *
     * <pre>
     * BooleanUtils.toBooleanObject("true", "true", "false", "null")  = Boolean.TRUE
     * BooleanUtils.toBooleanObject("false", "true", "false", "null") = Boolean.FALSE
     * BooleanUtils.toBooleanObject("null", "true", "false", "null")  = null
    </pre> *
     *
     * @param str 要转化的字符串
     * @param trueString 代表 `true`的值(大小写敏感), 可以为 `null`
     * @param falseString 代表 `false`的值(大小写敏感), 可以为 `null`
     * @param nullString 代表 `null`的值(大小写敏感), 可以为 `null`
     * @return 字符串的布尔值, 如果字符串匹配`nullString`或为`null`且 `nullString`也为`null`将返回`null`,
     * @throws IllegalArgumentException 如果没有匹配
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:05:35
     */
    fun toBooleanObject(
        str: String?,
        trueString: String?,
        falseString: String?,
        nullString: String?
    ): Boolean {
        return BooleanUtils.toBooleanObject(str, trueString, falseString, nullString)
    }
    // String to boolean methods
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 将String转化为boolean(性能优化)
     *
     *
     *
     *
     * `'true'`, `'on'` 或 `'yes'` (大小写不敏感) 将返回 `true`. 否则, 返回`false`.
     *
     *
     *
     *
     * 这个方法的性能比较jdk1.4的`Boolean.valueOf(String)`快4倍. 而且, 该方法接受'1'、'on' 和 'yes' 当作true.
     *
     *
     * <pre>
     * BooleanUtils.toBoolean(null)    = false
     * BooleanUtils.toBoolean("true")  = true
     * BooleanUtils.toBoolean("TRUE")  = true
     * BooleanUtils.toBoolean("tRUe")  = true
     * BooleanUtils.toBoolean("on")    = true
     * BooleanUtils.toBoolean("yes")   = true
     * BooleanUtils.toBoolean("1")    = true
     * BooleanUtils.toBoolean("0")   = false
     * BooleanUtils.toBoolean("false") = false
     * BooleanUtils.toBoolean("x gti") = false
    </pre> *
     *
     * @param str 要转化的字符串
     * @return 字符串的布尔值, 如果没有匹配或字符串为null将返回`false`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:14:15
     */
    fun toBoolean(str: String): Boolean {
        if ("1" == str) {
            return true
        }
        return if ("0" == str) {
            false
        } else BooleanUtils.toBoolean(str)
    }

    /**
     *
     *
     * 将String转化为boolean, 如果没有匹配将抛出异常
     *
     *
     * <pre>
     * BooleanUtils.toBoolean("true", "true", "false")  = true
     * BooleanUtils.toBoolean("false", "true", "false") = false
    </pre> *
     *
     * @param str 要转化的字符串
     * @param trueString 代表 `true`的值(大小写敏感), 可以为 `null`
     * @param falseString 代表 `false`的值(大小写敏感), 可以为 `null`
     * @return 字符串的布尔值
     * @throws IllegalArgumentException 如果没有匹配
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:16:17
     */
    fun toBoolean(str: String?, trueString: String?, falseString: String?): Boolean {
        return BooleanUtils.toBoolean(str, trueString, falseString)
    }
    // Boolean to String methods
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 将Boolean转化为String, 返回`'true'`, `'false'`,或 `null`.
     *
     *
     * <pre>
     * BooleanUtils.toStringTrueFalse(Boolean.TRUE)  = "true"
     * BooleanUtils.toStringTrueFalse(Boolean.FALSE) = "false"
     * BooleanUtils.toStringTrueFalse(null)          = null;
    </pre> *
     *
     * @param bool 要转化的Boolean
     * @return `'true'`, `'false'`, 或 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:17:59
     */
    fun toStringTrueFalse(bool: Boolean?): String {
        return BooleanUtils.toStringTrueFalse(bool)
    }

    /**
     *
     *
     * 将Boolean转化为String, 返回`'on'`, `'off'`,或 `null`.
     *
     *
     * <pre>
     * BooleanUtils.toStringOnOff(Boolean.TRUE)  = "on"
     * BooleanUtils.toStringOnOff(Boolean.FALSE) = "off"
     * BooleanUtils.toStringOnOff(null)          = null;
    </pre> *
     *
     * @param bool 要转化的Boolean
     * @return `'on'`, `'off'`, 或 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:18:46
     */
    fun toStringOnOff(bool: Boolean?): String {
        return BooleanUtils.toStringOnOff(bool)
    }

    /**
     *
     *
     * 将Boolean转化为String, 返回`'yes'`, `'no'`,或 `null`.
     *
     *
     * <pre>
     * BooleanUtils.toStringYesNo(Boolean.TRUE)  = "yes"
     * BooleanUtils.toStringYesNo(Boolean.FALSE) = "no"
     * BooleanUtils.toStringYesNo(null)          = null;
    </pre> *
     *
     * @param bool 要转化的Boolean
     * @return `'yes'`, `'no'`, 或 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:19:21
     */
    fun toStringYesNo(bool: Boolean?): String {
        return BooleanUtils.toStringYesNo(bool)
    }

    /**
     *
     *
     * 将Boolean转化为String, 返回输入的某个匹配的字符串
     *
     *
     * <pre>
     * BooleanUtils.toString(Boolean.TRUE, "true", "false", null)   = "true"
     * BooleanUtils.toString(Boolean.FALSE, "true", "false", null)  = "false"
     * BooleanUtils.toString(null, "true", "false", null)           = null;
    </pre> *
     *
     * @param bool 要转化的Boolean
     * @param trueString 代表 `true`的值(大小写敏感), 可以为 `null`
     * @param falseString 代表 `false`的值(大小写敏感), 可以为 `null`
     * @param nullString 代表 `null`的值(大小写敏感), 可以为 `null`
     * @return 输入的某个匹配的字符串
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:23:04
     */
    fun toString(
        bool: Boolean?,
        trueString: String?,
        falseString: String?,
        nullString: String?
    ): String {
        return BooleanUtils.toString(bool, trueString, falseString, nullString)
    }
    // boolean to String methods
    // -----------------------------------------------------------------------
    /**
     *
     *
     * 将boolean转化为String, 返回 `'true'` or `'false'`.
     *
     *
     * <pre>
     * BooleanUtils.toStringTrueFalse(true)   = "true"
     * BooleanUtils.toStringTrueFalse(false)  = "false"
    </pre> *
     *
     * @param bool 要转化的Boolean
     * @return `'true'`, `'false'`, 或 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:24:04
     */
    fun toStringTrueFalse(bool: Boolean): String {
        return BooleanUtils.toStringTrueFalse(bool)
    }

    /**
     *
     *
     * 将boolean转化为String, 返回 `'on'` or `'off'`.
     *
     *
     * <pre>
     * BooleanUtils.toStringOnOff(true)   = "on"
     * BooleanUtils.toStringOnOff(false)  = "off"
    </pre> *
     *
     * @param bool 要转化的Boolean
     * @return `'on'`, `'off'`, 或 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:24:49
     */
    fun toStringOnOff(bool: Boolean): String {
        return BooleanUtils.toStringOnOff(bool)
    }

    /**
     *
     *
     * 将boolean转化为String, 返回 `'yes'` or `'no'`.
     *
     *
     * <pre>
     * BooleanUtils.toStringYesNo(true)   = "yes"
     * BooleanUtils.toStringYesNo(false)  = "no"
    </pre> *
     *
     * @param bool 要转化的Boolean
     * @return `'yes'`, `'no'`, 或 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:25:32
     */
    fun toStringYesNo(bool: Boolean): String {
        return BooleanUtils.toStringYesNo(bool)
    }

    /**
     *
     *
     * 将boolean转化为String, 返回输入的某个匹配的字符串
     *
     *
     * <pre>
     * BooleanUtils.toString(true, "true", "false")   = "true"
     * BooleanUtils.toString(false, "true", "false")  = "false"
    </pre> *
     *
     * @param bool 要转化的Boolean
     * @param trueString 代表 `true`的值(大小写敏感), 可以为 `null`
     * @param falseString 代表 `false`的值(大小写敏感), 可以为 `null`
     * @return 输入的某个匹配的字符串
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:26:32
     */
    fun toString(bool: Boolean, trueString: String?, falseString: String?): String {
        return BooleanUtils.toString(bool, trueString, falseString)
    }
    // logical operations
    // ----------------------------------------------------------------------
    /**
     *
     *
     * 对一组boolean进行逻辑与操作
     *
     *
     * <pre>
     * BooleanUtils.and(true, true)         = true
     * BooleanUtils.and(false, false)       = false
     * BooleanUtils.and(true, false)        = false
     * BooleanUtils.and(true, true, false)  = false
     * BooleanUtils.and(true, true, true)   = true
    </pre> *
     *
     * @param array `boolean`数组
     * @return 逻辑与操作的结果
     * @throws IllegalArgumentException 如果 `array` 为 `null`
     * @throws IllegalArgumentException 如果 `array` 为空.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:29:37
     */
    fun and(vararg array: Boolean): Boolean {
        return BooleanUtils.and(*array)
    }

    /**
     *
     *
     * 对一组Boolean进行逻辑与操作
     *
     *
     * <pre>
     * BooleanUtils.and(Boolean.TRUE, Boolean.TRUE)                 = Boolean.TRUE
     * BooleanUtils.and(Boolean.FALSE, Boolean.FALSE)               = Boolean.FALSE
     * BooleanUtils.and(Boolean.TRUE, Boolean.FALSE)                = Boolean.FALSE
     * BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)   = Boolean.TRUE
     * BooleanUtils.and(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE) = Boolean.FALSE
     * BooleanUtils.and(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)  = Boolean.FALSE
    </pre> *
     *
     * @param array `Boolean`数组
     * @return 逻辑与操作的结果
     * @throws IllegalArgumentException 如果 `array` 为 `null`
     * @throws IllegalArgumentException 如果 `array` 为空.
     * @throws IllegalArgumentException 如果 `array` 包含 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:30:59
     */
    fun and(vararg array: Boolean?): Boolean {
        return BooleanUtils.and(*array)
    }

    /**
     *
     *
     * 对一组boolean进行逻辑或操作
     *
     *
     * <pre>
     * BooleanUtils.or(true, true)          = true
     * BooleanUtils.or(false, false)        = false
     * BooleanUtils.or(true, false)         = true
     * BooleanUtils.or(true, true, false)   = true
     * BooleanUtils.or(true, true, true)    = true
     * BooleanUtils.or(false, false, false) = false
    </pre> *
     *
     * @param array `boolean`数组
     * @return 逻辑或操作的结果
     * @throws IllegalArgumentException 如果 `array` 为 `null`
     * @throws IllegalArgumentException 如果 `array` 为空.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:32:17
     */
    fun or(vararg array: Boolean): Boolean {
        return BooleanUtils.or(*array)
    }

    /**
     *
     *
     * 对一组Boolean进行逻辑或操作
     *
     *
     * <pre>
     * BooleanUtils.or(Boolean.TRUE, Boolean.TRUE)                  = Boolean.TRUE
     * BooleanUtils.or(Boolean.FALSE, Boolean.FALSE)                = Boolean.FALSE
     * BooleanUtils.or(Boolean.TRUE, Boolean.FALSE)                 = Boolean.TRUE
     * BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)    = Boolean.TRUE
     * BooleanUtils.or(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE)  = Boolean.TRUE
     * BooleanUtils.or(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)   = Boolean.TRUE
     * BooleanUtils.or(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE) = Boolean.FALSE
    </pre> *
     *
     * @param array `Boolean`数组
     * @return 逻辑或操作的结果
     * @throws IllegalArgumentException 如果 `array` 为 `null`
     * @throws IllegalArgumentException 如果 `array` 为空.
     * @throws IllegalArgumentException 如果 `array` 包含 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:33:05
     */
    fun or(vararg array: Boolean?): Boolean {
        return BooleanUtils.or(*array)
    }

    /**
     *
     *
     * 对一组boolean进行逻辑异或操作
     *
     *
     * <pre>
     * BooleanUtils.xor(true, true)   = false
     * BooleanUtils.xor(false, false) = false
     * BooleanUtils.xor(true, false)  = true
     * BooleanUtils.xor(true, true)   = false
     * BooleanUtils.xor(false, false) = false
     * BooleanUtils.xor(true, false)  = true
    </pre> *
     *
     * @param array `boolean`数组
     * @return 逻辑异或操作的结果
     * @throws IllegalArgumentException 如果 `array` 为 `null`
     * @throws IllegalArgumentException 如果 `array` 为空.
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:34:09
     */
    fun xor(vararg array: Boolean): Boolean {
        return BooleanUtils.xor(*array)
    }

    /**
     *
     *
     * 对一组boolean进行逻辑异或操作
     *
     *
     * <pre>
     * BooleanUtils.xor(new Boolean[] { Boolean.TRUE, Boolean.TRUE })   = Boolean.FALSE
     * BooleanUtils.xor(new Boolean[] { Boolean.FALSE, Boolean.FALSE }) = Boolean.FALSE
     * BooleanUtils.xor(new Boolean[] { Boolean.TRUE, Boolean.FALSE })  = Boolean.TRUE
    </pre> *
     *
     * @param array `boolean`数组
     * @return 逻辑异或操作的结果
     * @throws IllegalArgumentException 如果 `array` 为 `null`
     * @throws IllegalArgumentException 如果 `array` 为空.
     * @throws IllegalArgumentException 如果 `array` 包含 `null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午5:34:41
     */
    fun xor(vararg array: Boolean?): Boolean {
        return BooleanUtils.xor(*array)
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.BooleanUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    @JvmStatic
    fun main(args: Array<String>) {
        println(toBoolean("1"))
    }
}