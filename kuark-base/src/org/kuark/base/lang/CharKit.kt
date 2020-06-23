package org.kuark.base.lang

import org.apache.commons.lang3.CharUtils

/**
 * 字符处理工具类
 *
 * @since 1.0.0
 * @author admin
 * @time 2013-4-9 下午8:32:48
 */
object CharKit {
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.CharUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    /**
     *
     *
     * 将字符串的第一个字符转为Character, 如果字符串为null或空串将返回null
     *
     *
     *
     * 对于7位ASCII码的字符, 该方法将使用缓存, 因此同一字符将返回同一个Character对象
     *
     *
     * <pre>
     * CharUtils.toCharacterObject(null) = null
     * CharUtils.toCharacterObject("")   = null
     * CharUtils.toCharacterObject("A")  = 'A'
     * CharUtils.toCharacterObject("BA") = 'B'
    </pre> *
     *
     * @param str 要处理的字符串
     * @return 第一个字符对应的Character对象
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:17:44
     */
    fun toCharacterObject(str: String?): Char {
        return CharUtils.toCharacterObject(str)
    }
    //-----------------------------------------------------------------------
    /**
     *
     *
     * 将Character转为char, 如果参数为`null`将抛出异常
     *
     *
     * <pre>
     * CharUtils.toChar(' ')  = ' '
     * CharUtils.toChar('A')  = 'A'
     * CharUtils.toChar(null) throws IllegalArgumentException
    </pre> *
     *
     * @param ch 要转化的Character
     * @return 转化后的char
     * @throws IllegalArgumentException 如果参数为`null`
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:20:16
     */
    fun toChar(ch: Char?): Char {
        return CharUtils.toChar(ch)
    }

    /**
     *
     *
     * 将Character转为char, 如果参数为`null`将返回指定的默认值
     *
     *
     * <pre>
     * CharUtils.toChar(null, 'X') = 'X'
     * CharUtils.toChar(' ', 'X')  = ' '
     * CharUtils.toChar('A', 'X')  = 'A'
    </pre> *
     *
     * @param ch 要转化的Character
     * @param defaultValue 默认值
     * @return Character参数的char值 或 返回默认值参数如果Character参数为null的话
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:22:40
     */
    fun toChar(ch: Char?, defaultValue: Char): Char {
        return CharUtils.toChar(ch, defaultValue)
    }
    //-----------------------------------------------------------------------
    /**
     *
     *
     * 将字符串的第一个字符转为Character, 如果字符串为null或空串将返抛出一个异常
     *
     *
     * <pre>
     * CharUtils.toChar("A")  = 'A'
     * CharUtils.toChar("BA") = 'B'
     * CharUtils.toChar(null) throws IllegalArgumentException
     * CharUtils.toChar("")   throws IllegalArgumentException
    </pre> *
     *
     * @param str 要转化的字符串
     * @return 第一个字符对应的Character对象
     * @throws IllegalArgumentException 如果字符串为null或空串将返抛出一个异常
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:28:06
     */
    fun toChar(str: String?): Char {
        return CharUtils.toChar(str)
    }

    /**
     *
     *
     * 将字符串的第一个字符转为Character, 如果字符串为null或空串将返回指定的默认值
     *
     *
     * <pre>
     * CharUtils.toChar(null, 'X') = 'X'
     * CharUtils.toChar("", 'X')   = 'X'
     * CharUtils.toChar("A", 'X')  = 'A'
     * CharUtils.toChar("BA", 'X') = 'B'
    </pre> *
     *
     * @param str 要转化的字符串
     * @param defaultValue 默认值
     * @return 第一个字符对应的char值 或 返回默认值参数如果Character参数为null或空串的话
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:28:16
     */
    fun toChar(str: String?, defaultValue: Char): Char {
        return CharUtils.toChar(str, defaultValue)
    }
    //-----------------------------------------------------------------------
    /**
     *
     *
     * 将char转化它代表的int值, 如果字符不是一个数值将抛出一个异常
     *
     *
     * <pre>
     * CharUtils.toIntValue('3')  = 3
     * CharUtils.toIntValue('A')  throws IllegalArgumentException
    </pre> *
     *
     * @param ch 要转化的char
     * @return the 字符的int值
     * @throws IllegalArgumentException 如果字符不是一个ASCII码数值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:31:34
     */
    fun toIntValue(ch: Char): Int {
        return CharUtils.toIntValue(ch)
    }

    /**
     *
     *
     * 将char转化它代表的int值, 如果字符不是一个数值将返回指定的默认值
     *
     *
     * <pre>
     * CharUtils.toIntValue('3', -1)  = 3
     * CharUtils.toIntValue('A', -1)  = -1
    </pre> *
     *
     * @param ch 要转化的char
     * @param defaultValue 默认值
     * @return 字符的int值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:33:23
     */
    fun toIntValue(ch: Char, defaultValue: Int): Int {
        return CharUtils.toIntValue(ch, defaultValue)
    }

    /**
     *
     *
     * 将Character转化它代表的int值, 如果字符不是一个数值将抛出一个异常
     *
     *
     * <pre>
     * CharUtils.toIntValue('3')  = 3
     * CharUtils.toIntValue(null) throws IllegalArgumentException
     * CharUtils.toIntValue('A')  throws IllegalArgumentException
    </pre> *
     *
     * @param ch 要转化的char
     * @return 字符的int值
     * @throws IllegalArgumentException 果字符不是一个ASCII码数值或者为null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:36:07
     */
    fun toIntValue(ch: Char?): Int {
        return CharUtils.toIntValue(ch)
    }

    /**
     *
     *
     * 将char转化它代表的int值, 如果字符不是一个数值将返回指定的默认值
     *
     *
     * <pre>
     * CharUtils.toIntValue(null, -1) = -1
     * CharUtils.toIntValue('3', -1)  = 3
     * CharUtils.toIntValue('A', -1)  = -1
    </pre> *
     *
     * @param ch 要转化的char
     * @param defaultValue 默认值
     * @return 字符的int值
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:37:06
     */
    fun toIntValue(ch: Char?, defaultValue: Int): Int {
        return CharUtils.toIntValue(ch, defaultValue)
    }
    //-----------------------------------------------------------------------
    /**
     *
     *
     * 将char转化为字符串
     *
     *
     *
     *
     * 对于7位ASCII码的字符, 该方法将使用缓存, 因此同一字符将返回同一个字符串对象
     *
     *
     * <pre>
     * CharUtils.toString(' ')  = " "
     * CharUtils.toString('A')  = "A"
    </pre> *
     *
     * @param ch 要转化的char
     * @return 包含一个给定字符的字符串
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:39:00
     */
    fun toString(ch: Char): String {
        return CharUtils.toString(ch)
    }

    /**
     *
     *
     * 将character转化为字符串
     *
     *
     *
     *
     * 对于7位ASCII码的字符, 该方法将使用缓存, 因此同一字符将返回同一个字符串对象
     *
     *
     *
     * 传入 `null` 将返回 `null`
     *
     * <pre>
     * CharUtils.toString(null) = null
     * CharUtils.toString(' ')  = " "
     * CharUtils.toString('A')  = "A"
    </pre> *
     *
     * @param ch 要转化的char
     * @return 包含一个给定字符的字符串
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:40:29
     */
    fun toString(ch: Char?): String {
        return CharUtils.toString(ch)
    }
    //--------------------------------------------------------------------------
    /**
     *
     *
     * 将char转化为其Unicode编码的字符串
     *
     *
     * <pre>
     * CharUtils.unicodeEscaped(' ') = "\u0020"
     * CharUtils.unicodeEscaped('A') = "\u0041"
    </pre> *
     *
     * @param ch 要转化的char
     * @return 字符对应Unicode编码的字符串
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:42:26
     */
    fun unicodeEscaped(ch: Char): String {
        return CharUtils.unicodeEscaped(ch)
    }

    /**
     *
     *
     * 将Character转化为其Unicode编码的字符串
     *
     *
     *
     * 传入 `null` 将返回 `null`
     *
     * <pre>
     * CharUtils.unicodeEscaped(null) = null
     * CharUtils.unicodeEscaped(' ')  = "\u0020"
     * CharUtils.unicodeEscaped('A')  = "\u0041"
    </pre> *
     *
     * @param ch 要转化的Character, 可以为null
     * @return 字符对应Unicode编码的字符串, 传入的参数为null将返回null
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:44:29
     */
    fun unicodeEscaped(ch: Char?): String {
        return CharUtils.unicodeEscaped(ch)
    }
    //--------------------------------------------------------------------------
    /**
     *
     *
     * 检测给定的char是否为7位的ASCII码
     *
     *
     * <pre>
     * CharUtils.isAscii('a')  = true
     * CharUtils.isAscii('A')  = true
     * CharUtils.isAscii('3')  = true
     * CharUtils.isAscii('-')  = true
     * CharUtils.isAscii('\n') = true
     * CharUtils.isAscii('') = false
    </pre> *
     *
     * @param ch 要检测的char
     * @return true: 如果ASCII码值小于128
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:52:09
     */
    fun isAscii(ch: Char): Boolean {
        return CharUtils.isAscii(ch)
    }

    /**
     *
     *
     * 检测给定的char是否为7位可打印的ASCII码
     *
     *
     * <pre>
     * CharUtils.isAsciiPrintable('a')  = true
     * CharUtils.isAsciiPrintable('A')  = true
     * CharUtils.isAsciiPrintable('3')  = true
     * CharUtils.isAsciiPrintable('-')  = true
     * CharUtils.isAsciiPrintable('\n') = false
     * CharUtils.isAsciiPrintable('') = false
    </pre> *
     *
     * @param ch 要检测的char
     * @return true: 如果ASCII码值介于32和126之间
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:53:41
     */
    fun isAsciiPrintable(ch: Char): Boolean {
        return CharUtils.isAsciiPrintable(ch)
    }

    /**
     *
     *
     * 检测给定的char是否为7位ASCII码的控制字符
     *
     *
     * <pre>
     * CharUtils.isAsciiControl('a')  = false
     * CharUtils.isAsciiControl('A')  = false
     * CharUtils.isAsciiControl('3')  = false
     * CharUtils.isAsciiControl('-')  = false
     * CharUtils.isAsciiControl('\n') = true
     * CharUtils.isAsciiControl('') = false
    </pre> *
     *
     * @param ch 要检测的char
     * @return true: 如果ASCII码值介于32和127之间
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:56:14
     */
    fun isAsciiControl(ch: Char): Boolean {
        return CharUtils.isAsciiControl(ch)
    }

    /**
     *
     *
     * 检测给定的char是否为7位ASCII码的字母
     *
     *
     * <pre>
     * CharUtils.isAsciiAlpha('a')  = true
     * CharUtils.isAsciiAlpha('A')  = true
     * CharUtils.isAsciiAlpha('3')  = false
     * CharUtils.isAsciiAlpha('-')  = false
     * CharUtils.isAsciiAlpha('\n') = false
     * CharUtils.isAsciiAlpha('') = false
    </pre> *
     *
     * @param ch 要检测的char
     * @return true: 如果ASCII码值介于65和90之间(大写字母)或97和122之间(小写字母)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午7:59:51
     */
    fun isAsciiAlpha(ch: Char): Boolean {
        return CharUtils.isAsciiAlpha(ch)
    }

    /**
     *
     *
     * 检测给定的char是否为7位ASCII码的大写字母
     *
     *
     * <pre>
     * CharUtils.isAsciiAlphaUpper('a')  = false
     * CharUtils.isAsciiAlphaUpper('A')  = true
     * CharUtils.isAsciiAlphaUpper('3')  = false
     * CharUtils.isAsciiAlphaUpper('-')  = false
     * CharUtils.isAsciiAlphaUpper('\n') = false
     * CharUtils.isAsciiAlphaUpper('') = false
    </pre> *
     *
     * @param ch 要检测的char
     * @return true: 如果ASCII码值介于65和90
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午8:00:45
     */
    fun isAsciiAlphaUpper(ch: Char): Boolean {
        return CharUtils.isAsciiAlphaUpper(ch)
    }

    /**
     *
     *
     * 检测给定的char是否为7位ASCII码的大写字母
     *
     *
     * <pre>
     * CharUtils.isAsciiAlphaLower('a')  = true
     * CharUtils.isAsciiAlphaLower('A')  = false
     * CharUtils.isAsciiAlphaLower('3')  = false
     * CharUtils.isAsciiAlphaLower('-')  = false
     * CharUtils.isAsciiAlphaLower('\n') = false
     * CharUtils.isAsciiAlphaLower('') = false
    </pre> *
     *
     * @param ch 要检测的char
     * @return true: 如果ASCII码值介于97和122之间
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午8:01:40
     */
    fun isAsciiAlphaLower(ch: Char): Boolean {
        return CharUtils.isAsciiAlphaLower(ch)
    }

    /**
     *
     *
     * 检测给定的char是否为7位ASCII码的数字
     *
     *
     * <pre>
     * CharUtils.isAsciiNumeric('a')  = false
     * CharUtils.isAsciiNumeric('A')  = false
     * CharUtils.isAsciiNumeric('3')  = true
     * CharUtils.isAsciiNumeric('-')  = false
     * CharUtils.isAsciiNumeric('\n') = false
     * CharUtils.isAsciiNumeric('') = false
    </pre> *
     *
     * @param ch 要检测的char
     * @return true: 如果ASCII码值介于48和57之间
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午8:03:01
     */
    fun isAsciiNumeric(ch: Char): Boolean {
        return CharUtils.isAsciiNumeric(ch)
    }

    /**
     *
     *
     * 检测给定的char是否为7位ASCII码的字母或数字
     *
     *
     * <pre>
     * CharUtils.isAsciiAlphanumeric('a')  = true
     * CharUtils.isAsciiAlphanumeric('A')  = true
     * CharUtils.isAsciiAlphanumeric('3')  = true
     * CharUtils.isAsciiAlphanumeric('-')  = false
     * CharUtils.isAsciiAlphanumeric('\n') = false
     * CharUtils.isAsciiAlphanumeric('') = false
    </pre> *
     *
     * @param ch 要检测的char
     * @return true: 如果ASCII码值介于48和57之间(数字)或65和90之间(大写字母)或97和122之间(小写字母)
     * @since 1.0.0
     * @author admin
     * @time 2013-4-30 下午8:07:04
     */
    fun isAsciiAlphanumeric(ch: Char): Boolean {
        return CharUtils.isAsciiAlphanumeric(ch)
    } // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.CharUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}