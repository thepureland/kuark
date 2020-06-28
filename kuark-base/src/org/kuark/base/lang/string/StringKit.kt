package org.kuark.base.lang.string

import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils
import org.kuark.base.security.CryptoKit
import org.kuark.base.security.DigestKit
import java.util.*
import java.util.regex.Matcher
import kotlin.math.ceil

/**
 * 字符串操作工具类
 *
 * @since 1.0.0
 */
object StringKit {

    /**
     * 查找子串，并用指定字符串替换之（替换所有出现的地方），支持多对替换规则
     *
     * @param text 被查找和替换的源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @param map  Map<要查找的字符串, 用来替换的字符串>
     * @return 替换后的字符串
     * @see .replaceEach
     * @since 1.0.0
     */
    fun replaceEach(text: String?, map: Map<String?, String>): String? {
        if (isEmpty(text)) return text
        return if (map.isNotEmpty()) {
            replaceEach(text!!, map.keys.toTypedArray(), map.values.toTypedArray())
        } else text
    }

    /**
     * 将字符串转换为十六进制表示的值
     *
     * @param str 需要转换的字符串
     * @return 转换后的十六进制表示的字符串
     * @since 1.0.0
     */
    fun toHexStr(str: String): String = String(CryptoKit.encodeHex(str.toByteArray()))

    /**
     * 解码十六进制表示的字符串
     *
     * @param hexStr 十六进制表示的字符串
     * @return 解码后的字符串
     * @since 1.0.0
     */
    fun decodeHexStr(hexStr: String): String = String(CryptoKit.decodeHex(hexStr.toByteArray()))

    /**
     * 对字符串进行MD5加密后，再进行十六进制编码
     *
     * @param str     待加密的字符串
     * @param saltStr 盐
     * @return 加密的字符串
     * @since 1.0.0
     */
    fun toMd5HexStr(str: String, saltStr: String): String = DigestKit.getMD5(str, saltStr)

    /**
     * 将字符串按给定的长度均分(最后一组可能不是等分的)
     *
     * <pre>
     * divideAverage(null, *) = []
     * divideAverage("", *) = []
     * divideAverage(*, 0) = []
     * divideAverage(*, -3) = []
     * divideAverage("123456", 3) = ["12", "34", "56"]
     * divideAverage("1234567", 3) = ["123", "456", "7"]
     * </pre>
     * @param srcStr   源字符串
     * @param groupLen 每份长度
     * @return 等分后每个分组组成的数组
     * @since 1.0.0
     */
    fun divideAverage(srcStr: String?, groupLen: Int): Array<String?> {
        if (isEmpty(srcStr) || groupLen <= 0) {
            return arrayOf()
        }
        val strLen = srcStr!!.length
        val eachCount = ceil(strLen.toDouble() / groupLen).toInt()
        val groups = mutableListOf<String>()
        for (i in 0 until groupLen) {
            val beginIndex = i * eachCount
            var endIndex = if (i == groupLen - 1) { // 最后一组
                strLen
            } else {
                beginIndex + eachCount
            }
            groups.add(srcStr.substring(beginIndex, endIndex))
        }
        return groups.toTypedArray()
    }

    /**
     * 将“驼峰”式写法的字符串转为用“_”分割的字符串
     *
     * <pre>
     * humpToUnderscore(null) = ""
     * humpToUnderscore("") = ""
     * humpToUnderscore(" ") = ""
     * humpToUnderscore("humpToUnderscore") = "HUMP_TO_Underscore"
     * </pre>
     * @param str “驼峰”式写法的字符串
     * @return “_”分割的字符串, 并且是大写的
     * @since 1.0.0
     */
    fun humpToUnderscore(str: String?): String {
        var s = str
        if (isNotBlank(s)) {
            s = s!!.trim { it <= ' ' }
            val chars = s.toCharArray()
            val sb = StringBuilder()
            sb.append(chars[0])
            for (i in 1 until chars.size) {
                if (Character.isUpperCase(chars[i])) { //  && Character.isLowerCase(chars[i - 1])
                    sb.append("_")
                }
                sb.append(chars[i])
            }
            return sb.toString().toUpperCase()
        }
        return ""
    }

    /**
     * 将“_”分割的字符串转为“驼峰”式写法的字符串, 如：HUMP_TO_Underscore -> humpToUnderscore
     *
     * <pre>
     * underscoreToHump(null) = ""
     * underscoreToHump("") = ""
     * underscoreToHump(" ") = ""
     * underscoreToHump("HUMP_TO_Underscore") = "humpToUnderscore"
     * </pre>
     * @param str “_”分割的字符串
     * @return “驼峰”式写法的字符串
     * @since 1.0.0
     */
    fun underscoreToHump(str: String?): String {
        var s = str
        if (isNotBlank(s)) {
            s = s!!.trim { it <= ' ' }
            val words = split(s, "_")
            val sb = StringBuilder()
            for (i in words.indices) {
                val word = words[i]
                if (i == 0) {
                    sb.append(word.toLowerCase())
                } else {
                    sb.append(capitalize(word.toLowerCase()))
                }
            }
            return sb.toString()
        }
        return ""
    }

    /**
     * 替换模板中的参数(以"${"和"}"括起来)
     *
     * @param template 模板
     * @param paramMap 参数map
     * @return 替换后的字符串
     * @since 1.0.0
     */
    fun fillTemplate(template: String?, paramMap: Map<String, String>?): String? {
        if (isEmpty(template)) return template
        var templateStr = template
        paramMap?.let {
            for ((paramName, paramValue) in paramMap)
                templateStr = templateStr!!.replace(
                    Regex("\\$\\{$paramName\\}"), Matcher.quoteReplacement(paramValue)
                )
        }
        return templateStr
    }

    /**
     * 替换模板中的参数(以"${"和"}"括起来)
     *
     * @param template 模板
     * @param paramMap 参数map
     * @return 替换后的字符串
     * @since 1.0.0
     */
    fun fillTemplateByObjectMap(template: String, paramMap: Map<String, Any>): String {
        var templateStr = template
        if (paramMap.isNotEmpty()) {
            for ((paramName, value) in paramMap) {
                templateStr = templateStr.replace(
                    "\\$\\{" + paramName + "\\}".toRegex(), Matcher.quoteReplacement(value.toString())
                )
            }
        }
        return templateStr
    }

    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.StringUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    //region 判空
    /**
     * 判断给定的字符序列是否为空串或为null
     *
     * <pre>
     * StringKit.isEmpty(null)      = true
     * StringKit.isEmpty("")        = true
     * StringKit.isEmpty(" ")       = false
     * StringKit.isEmpty("bob")     = false
     * StringKit.isEmpty("  bob  ") = false
     * </pre>
     * @param cs 待判断的字符序列
     * @return `true` 如果字符序列为空串或为null
     * @since 1.0.0
     */
    fun isEmpty(cs: CharSequence?): Boolean = StringUtils.isEmpty(cs)

    /**
     * 判断给定的字符序列是否不为空串或为null
     *
     * <pre>
     * StringKit.isNotEmpty(null)      = false
     * StringKit.isNotEmpty("")        = false
     * StringKit.isNotEmpty(" ")       = true
     * StringKit.isNotEmpty("bob")     = true
     * StringKit.isNotEmpty("  bob  ") = true
     * </pre>
     * @param cs 待判断的字符序列
     * @return `true` 如果字符序列不为空串或为null
     * @since 1.0.0
     */
    fun isNotEmpty(cs: CharSequence?): Boolean = StringUtils.isNotEmpty(cs)

    /**
     * 判断给定的字符序列是否为空白、空串或为null
     *
     * <pre>
     * StringKit.isBlank(null)      = true
     * StringKit.isBlank("")        = true
     * StringKit.isBlank(" ")       = true
     * StringKit.isBlank("bob")     = false
     * StringKit.isBlank("  bob  ") = false
     * </pre>
     * @param cs 待判断的字符序列
     * @return `true` 如果字符序列为空白、空串或为null
     * @since 1.0.0
     */
    fun isBlank(cs: CharSequence?): Boolean = StringUtils.isBlank(cs)

    /**
     * 判断给定的字符序列是否不为空白、空串或为null
     *
     * <pre>
     * StringKit.isNotBlank(null)      = false
     * StringKit.isNotBlank("")        = false
     * StringKit.isNotBlank(" ")       = false
     * StringKit.isNotBlank("bob")     = true
     * StringKit.isNotBlank("  bob  ") = true
     * </pre>
     * @param cs 待判断的字符序列，可以为null
     * @return `true` 如果字符序列不为空白、空串或为null
     * @since 1.0.0
     */
    fun isNotBlank(cs: CharSequence?): Boolean = StringUtils.isNotBlank(cs)
    //endregion 判空

    //region Trim
    /**
     * 去除给定字符串的前后控制符和空白符(如果有的话)
     *
     * <pre>
     * StringKit.trim(null)          = null
     * StringKit.trim("")            = ""
     * StringKit.trim("     ")       = ""
     * StringKit.trim("abc")         = "abc"
     * StringKit.trim("    abc    ") = "abc"
     * </pre>
     * @param str 待处理的字符串，可以为null
     * @return 去除前后控制符和空白符后的字符串
     * @since 1.0.0
     */
    fun trim(str: String?): String? = StringUtils.trim(str)

    /**
     * 去除给定字符串的前后控制符和空白符(如果有的话)，结果如果是空串或null的话，将返回null
     *
     * <pre>
     * StringKit.trimToNull(null)          = null
     * StringKit.trimToNull("")            = null
     * StringKit.trimToNull("     ")       = null
     * StringKit.trimToNull("abc")         = "abc"
     * StringKit.trimToNull("    abc    ") = "abc"
     * </pre>
     * @param str 待处理的字符串，可以为null
     * @return 去除前后控制符和空白符后的字符串，如果是空串或null的话，将返回null
     * @since 1.0.0
     */
    fun trimToNull(str: String?): String? = StringUtils.trimToNull(str)

    /**
     * 去除给定字符串的前后控制符和空白符(如果有的话)，结果如果是空串或null的话，将返回空串
     *
     * <pre>
     * StringKit.trimToEmpty(null)          = ""
     * StringKit.trimToEmpty("")            = ""
     * StringKit.trimToEmpty("     ")       = ""
     * StringKit.trimToEmpty("abc")         = "abc"
     * StringKit.trimToEmpty("    abc    ") = "abc"
     * </pre>
     * @param str 待处理的字符串，可以为null
     * @return 去除前后控制符和空白符后的字符串，如果是空串或null的话，将返回空串
     * @since 1.0.0
     */
    fun trimToEmpty(str: String?): String = StringUtils.trimToEmpty(str)
    //endregion Trim

    //region Stripping
    /**
     * 去除给定字符串的前后空白符(如果有的话)
     *
     * <pre>
     * StringKit.strip(null)     = null
     * StringKit.strip("")       = ""
     * StringKit.strip("   ")    = ""
     * StringKit.strip("abc")    = "abc"
     * StringKit.strip("  abc")  = "abc"
     * StringKit.strip("abc  ")  = "abc"
     * StringKit.strip(" abc ")  = "abc"
     * StringKit.strip(" ab c ") = "ab c"
     * </pre>
     * @param str 待处理的字符串，可以为null
     * @return 去除前后空白符后的字符串
     * @since 1.0.0
     */
    fun strip(str: String?): String? = StringUtils.strip(str)

    /**
     * 去除给定字符串的前后空白符(如果有的话)，结果如果是空串或null的话，将返回null
     *
     * <pre>
     * StringKit.stripToNull(null)     = null
     * StringKit.stripToNull("")       = null
     * StringKit.stripToNull("   ")    = null
     * StringKit.stripToNull("abc")    = "abc"
     * StringKit.stripToNull("  abc")  = "abc"
     * StringKit.stripToNull("abc  ")  = "abc"
     * StringKit.stripToNull(" abc ")  = "abc"
     * StringKit.stripToNull(" ab c ") = "ab c"
     * </pre>
     * @param str 待处理的字符串，可以为null
     * @return 去除前后空白符后的字符串，如果是空串或null的话，将返回null
     * @since 1.0.0
     */
    fun stripToNull(str: String?): String? = StringUtils.stripToNull(str)

    /**
     * 去除给定字符串的前后空白符(如果有的话)，结果如果是空串或null的话，将返回空串
     *
     * <pre>
     * StringKit.stripToEmpty(null)     = ""
     * StringKit.stripToEmpty("")       = ""
     * StringKit.stripToEmpty("   ")    = ""
     * StringKit.stripToEmpty("abc")    = "abc"
     * StringKit.stripToEmpty("  abc")  = "abc"
     * StringKit.stripToEmpty("abc  ")  = "abc"
     * StringKit.stripToEmpty(" abc ")  = "abc"
     * StringKit.stripToEmpty(" ab c ") = "ab c"
     * </pre>
     * @param str 待处理的字符串，可以为null
     * @return 去除前后空白符后的字符串，如果是空串或null的话，将返回空串
     * @since 1.0.0
     */
    fun stripToEmpty(str: String?): String = StringUtils.stripToEmpty(str)

    /**
     * 删除给定字符串前后匹配任何指定字符集的子集的内容
     *
     * <pre>
     * StringKit.strip(null, *)          = null
     * StringKit.strip("", *)            = ""
     * StringKit.strip("abc", null)      = "abc"
     * StringKit.strip("  abc", null)    = "abc"
     * StringKit.strip("abc  ", null)    = "abc"
     * StringKit.strip(" abc ", null)    = "abc"
     * StringKit.strip("  abcyx", "xyz") = "  abc"
     * </pre>
     * @param str        待处理的字符串，可以为null
     * @param stripChars 要删除的字符集，null将被当作空白符
     * @return 处理后的字符串
     * @since 1.0.0
     */
    fun strip(str: String?, stripChars: String?): String? = StringUtils.strip(str, stripChars)

    /**
     * 删除给定字符串前端匹配任何指定字符集的子集的内容
     *
     * <pre>
     * StringKit.stripStart(null, *)          = null
     * StringKit.stripStart("", *)            = ""
     * StringKit.stripStart("abc", "")        = "abc"
     * StringKit.stripStart("abc", null)      = "abc"
     * StringKit.stripStart("  abc", null)    = "abc"
     * StringKit.stripStart("abc  ", null)    = "abc  "
     * StringKit.stripStart(" abc ", null)    = "abc "
     * StringKit.stripStart("yxabc  ", "xyz") = "abc  "
     * </pre>
     * @param str        待处理的字符串，可以为null
     * @param stripChars 要删除的字符串，null将被当作空白符
     * @return 处理后的字符串
     * @since 1.0.0
     */
    fun stripStart(str: String?, stripChars: String?): String? = StringUtils.stripStart(str, stripChars)

    /**
     * 删除给定字符串末端匹配任何指定字符集的子集的内容
     *
     * <pre>
     * StringKit.stripEnd(null, *)          = null
     * StringKit.stripEnd("", *)            = ""
     * StringKit.stripEnd("abc", "")        = "abc"
     * StringKit.stripEnd("abc", null)      = "abc"
     * StringKit.stripEnd("  abc", null)    = "  abc"
     * StringKit.stripEnd("abc  ", null)    = "abc"
     * StringKit.stripEnd(" abc ", null)    = " abc"
     * StringKit.stripEnd("  abcyx", "xyz") = "  abc"
     * StringKit.stripEnd("120.00", ".0")   = "12"
     * </pre>
     * @param str        待处理的字符串，可以为null
     * @param stripChars 要删除的字符串，null将被当作空白符
     * @return 处理后的字符串
     * @since 1.0.0
     */
    fun stripEnd(str: String?, stripChars: String?): String? = StringUtils.stripEnd(str, stripChars)

    /**
     * 对字符串数组中的每个字符串进行 strip(String str) ，然后返回
     *
     * <pre>
     * StringKit.stripAll(null)             = null
     * StringKit.stripAll([])               = []
     * StringKit.stripAll(["abc", "  abc"]) = ["abc", "abc"]
     * StringKit.stripAll(["abc  ", null])  = ["abc", null]
     * </pre>
     * @param strs 待处理的字符串数组，可以为null
     * @return 处理后的字符串数组，`null` 如果strs参数值为null
     * @since 1.0.0
     */
    fun stripAll(vararg strs: String?): Array<String> = StringUtils.stripAll(*strs)

    /**
     * 对字符串数组中的每个字符串进行 strip(String str, String stripChars) ，然后返回。
     *
     * <pre>
     * StringKit.stripAll(null, *)                = null
     * StringKit.stripAll([], *)                  = []
     * StringKit.stripAll(["abc", "  abc"], null) = ["abc", "abc"]
     * StringKit.stripAll(["abc  ", null], null)  = ["abc", null]
     * StringKit.stripAll(["abc  ", null], "yz")  = ["abc  ", null]
     * StringKit.stripAll(["yabcz", null], "yz")  = ["abc", null]
     * </pre>
     * @param strs       待处理的字符串数组，可以为null
     * @param stripChars 要删除的字符串，null将被当作空白符
     * @return 处理后的字符串数组，`null` 如果strs参数值为null
     * @since 1.0.0
     */
    fun stripAll(strs: Array<String?>?, stripChars: String): Array<String>? = StringUtils.stripAll(strs, stripChars)

    /**
     * 移删重音符号，大小写不改变
     *
     * <pre>
     * StringKit.stripAccents(null)                = null
     * StringKit.stripAccents("")                  = ""
     * StringKit.stripAccents("control")           = "control"
     * StringKit.stripAccents("clair")     = "eclair"
     * </pre>
     * @param input 源字符串，可以为null，为null返回null
     * @return 移除重音符号后的字符串
     * @since 1.0.0
     */
    fun stripAccents(input: String?): String? = StringUtils.stripAccents(input)
    //endregion Stripping 

    //region Equals
    // -----------------------------------------------------------------------
    /**
     * 比较两个字符串是否相等
     *
     * <pre>
     * StringKit.equals(null, null)   = true
     * StringKit.equals(null, "abc")  = false
     * StringKit.equals("abc", null)  = false
     * StringKit.equals("abc", "abc") = true
     * StringKit.equals("abc", "ABC") = false
     * </pre>
     * @param cs1 字符串1，可以为null
     * @param cs2 字符串2，可以为null
     * @return `true` 如果两字符串相等，大小写敏感，或两者都为null
     * @since 1.0.0
     */
    fun equals(cs1: CharSequence?, cs2: CharSequence?): Boolean = StringUtils.equals(cs1, cs2)

    /**
     * 比较两个字符串是否相等，不区分大小写
     *
     * <pre>
     * StringKit.equalsIgnoreCase(null, null)   = true
     * StringKit.equalsIgnoreCase(null, "abc")  = false
     * StringKit.equalsIgnoreCase("abc", null)  = false
     * StringKit.equalsIgnoreCase("abc", "abc") = true
     * StringKit.equalsIgnoreCase("abc", "ABC") = true
     * </pre>
     * @param str1 字符串1，可以为null
     * @param str2 字符串2，可以为null
     * @return `true` 如果两字符串相等，大小写不敏感，或两者都为null
     * @since 1.0.0
     */
    fun equalsIgnoreCase(str1: CharSequence?, str2: CharSequence?): Boolean = StringUtils.equalsIgnoreCase(str1, str2)
    //endregion Equals

    //region IndexOf
    /**
     * 返回字符 searchChar 在字符串 str 中第一次出现的位置
     *
     * <pre>
     * StringKit.indexOf(null, *)         = -1
     * StringKit.indexOf("", *)           = -1
     * StringKit.indexOf("aabaabaa", 'a') = 0
     * StringKit.indexOf("aabaabaa", 'b') = 2
     * </pre>
     * @param seq        待查找的字符序列，可以为null
     * @param searchChar 要查找的字符
     * @return 第一次出现的位置，如果没有找到或输入seq为null，将返回-1
     * @since 1.0.0
     */
    fun indexOf(seq: CharSequence?, searchChar: Int): Int = StringUtils.indexOf(seq, searchChar)

    /**
     * 返回字符 searchChar 从 startPos 开始在字符串 str 中第一次出现的位置
     *
     * <pre>
     * StringKit.indexOf(null, *, *)          = -1
     * StringKit.indexOf("", *, *)            = -1
     * StringKit.indexOf("aabaabaa", 'b', 0)  = 2
     * StringKit.indexOf("aabaabaa", 'b', 3)  = 5
     * StringKit.indexOf("aabaabaa", 'b', 9)  = -1
     * StringKit.indexOf("aabaabaa", 'b', -1) = 2
     * </pre>
     * @param seq        待查找的字符序列，可以为null
     * @param searchChar 要查找的字符
     * @param startPos   开始位置，负数将当作0处理
     * @return 第一次出现的位置，如果没有找到或输入seq为null，将返回-1
     * @since 1.0.0
     */
    fun indexOf(seq: CharSequence?, searchChar: Int, startPos: Int): Int =
        StringUtils.indexOf(seq, searchChar, startPos)

    /**
     * 返回字符串 searchStr 在字符串 str 中第一次出现的位置
     *
     * <pre>
     * StringKit.indexOf(null, *)          = -1
     * StringKit.indexOf(*, null)          = -1
     * StringKit.indexOf("", "")           = 0
     * StringKit.indexOf("", *)            = -1 (except when * = "")
     * StringKit.indexOf("aabaabaa", "a")  = 0
     * StringKit.indexOf("aabaabaa", "b")  = 2
     * StringKit.indexOf("aabaabaa", "ab") = 1
     * StringKit.indexOf("aabaabaa", "")   = 0
     * </pre>
     * @param seq       待查找的字符序列，可以为null
     * @param searchSeq 要查找的字符序列，可以为null
     * @return 第一次出现的位置，如果没有找到或输入seq为null，将返回-1
     * @since 1.0.0
     */
    fun indexOf(seq: CharSequence?, searchSeq: CharSequence?): Int = StringUtils.indexOf(seq, searchSeq)

    /**
     * 返回字符串 searchStr 在字符串 str 中第一次出现的位置，从startPos位置开始找
     *
     * <pre>
     * StringKit.indexOf(null, *, *)          = -1
     * StringKit.indexOf(*, null, *)          = -1
     * StringKit.indexOf("", "", 0)           = 0
     * StringKit.indexOf("", *, 0)            = -1 (except when * = "")
     * StringKit.indexOf("aabaabaa", "a", 0)  = 0
     * StringKit.indexOf("aabaabaa", "b", 0)  = 2
     * StringKit.indexOf("aabaabaa", "ab", 0) = 1
     * StringKit.indexOf("aabaabaa", "b", 3)  = 5
     * StringKit.indexOf("aabaabaa", "b", 9)  = -1
     * StringKit.indexOf("aabaabaa", "b", -1) = 2
     * StringKit.indexOf("aabaabaa", "", 2)   = 2
     * StringKit.indexOf("abc", "", 9)        = 3
     * </pre>
     * @param seq       待查找的字符序列，可以为null
     * @param searchSeq 要查找的字符序列，可以为null
     * @param startPos  开始位置，负数将当作0处理
     * @return 第一次出现的位置，如果没有找到或输入seq为null，将返回-1
     * @since 1.0.0
     */
    fun indexOf(seq: CharSequence?, searchSeq: CharSequence?, startPos: Int): Int =
        StringUtils.indexOf(seq, searchSeq, startPos)

    /**
     * 返回字符串 searchStr 在字符串 str 中第 ordinal 次出现的位置
     *
     * <pre>
     * StringKit.ordinalIndexOf(null, *, *)          = -1
     * StringKit.ordinalIndexOf(*, null, *)          = -1
     * StringKit.ordinalIndexOf("", "", *)           = 0
     * StringKit.ordinalIndexOf("aabaabaa", "a", 1)  = 0
     * StringKit.ordinalIndexOf("aabaabaa", "a", 2)  = 1
     * StringKit.ordinalIndexOf("aabaabaa", "b", 1)  = 2
     * StringKit.ordinalIndexOf("aabaabaa", "b", 2)  = 5
     * StringKit.ordinalIndexOf("aabaabaa", "ab", 1) = 1
     * StringKit.ordinalIndexOf("aabaabaa", "ab", 2) = 4
     * StringKit.ordinalIndexOf("aabaabaa", "", 1)   = 0
     * StringKit.ordinalIndexOf("aabaabaa", "", 2)   = 0
     * </pre>
     * @param str       待查找的字符序列，可以为null
     * @param searchStr 要查找的字符序列，可以为null
     * @param ordinal   匹配的序数
     * @return 第 ordinal 次出现的位置，如果没有找到或输入str为null，将返回-1
     * @since 1.0.0
     */
    fun ordinalIndexOf(str: CharSequence?, searchStr: CharSequence?, ordinal: Int): Int =
        StringUtils.ordinalIndexOf(str, searchStr, ordinal)

    /**
     * 返回字符 searchChar 在字符串 str 中第一次出现的位置，大小不敏感
     *
     * <pre>
     * StringKit.indexOfIgnoreCase(null, *)          = -1
     * StringKit.indexOfIgnoreCase(*, null)          = -1
     * StringKit.indexOfIgnoreCase("", "")           = 0
     * StringKit.indexOfIgnoreCase("aabaabaa", "a")  = 0
     * StringKit.indexOfIgnoreCase("aabaabaa", "b")  = 2
     * StringKit.indexOfIgnoreCase("aabaabaa", "ab") = 1
     * </pre>
     * @param str       待查找的字符序列，可以为null
     * @param searchStr 要查找的字符序列，可以为null
     * @return 第一次出现的位置，如果没有找到或输入seq为null，将返回-1
     * @since 1.0.0
     */
    fun indexOfIgnoreCase(str: CharSequence?, searchStr: CharSequence?): Int =
        StringUtils.indexOfIgnoreCase(str, searchStr)

    /**
     * 返回字符 searchChar 从 startPos 开始在字符串 str 中第一次出现的位置，大小写敏感
     *
     * <pre>
     * StringKit.indexOfIgnoreCase(null, *, *)          = -1
     * StringKit.indexOfIgnoreCase(*, null, *)          = -1
     * StringKit.indexOfIgnoreCase("", "", 0)           = 0
     * StringKit.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     * StringKit.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
     * StringKit.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
     * StringKit.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
     * StringKit.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
     * StringKit.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
     * StringKit.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
     * StringKit.indexOfIgnoreCase("abc", "", 9)        = 3
     * </pre>
     * @param str       待查找的字符序列，可以为null
     * @param searchStr 要查找的字符序列
     * @param startPos  开始位置，负数将当作0处理
     * @return 第一次出现的位置，如果没有找到或输入seq为null，将返回-1
     * @since 1.0.0
     */
    fun indexOfIgnoreCase(str: CharSequence?, searchStr: CharSequence?, startPos: Int): Int =
        StringUtils.indexOfIgnoreCase(str, searchStr, startPos)

    /**
     * 在字符串 seq 中查找最后一次出现字符 searchChar 的位置(从后往前第一次出现的位置)
     *
     * <pre>
     * StringKit.lastIndexOf(null, *)         = -1
     * StringKit.lastIndexOf("", *)           = -1
     * StringKit.lastIndexOf("aabaabaa", 'a') = 7
     * StringKit.lastIndexOf("aabaabaa", 'b') = 5
     * </pre>
     * @param seq        待查找的字符序列，可以为null
     * @param searchChar 要查找的字符
     * @return 最后一次出现的位置，如果没有找到或输入seq为null，将返回-1
     * @since 1.0.0
     */
    fun lastIndexOf(seq: CharSequence?, searchChar: Int): Int = StringUtils.lastIndexOf(seq, searchChar)

    /**
     * 返回字符 searchChar 从 startPos 开始在字符串 seq 中最后一次出现的位置(从后往前第一次出现的位置)
     *
     * <pre>
     * StringKit.lastIndexOf(null, *, *)          = -1
     * StringKit.lastIndexOf("", *,  *)           = -1
     * StringKit.lastIndexOf("aabaabaa", 'b', 8)  = 5
     * StringKit.lastIndexOf("aabaabaa", 'b', 4)  = 2
     * StringKit.lastIndexOf("aabaabaa", 'b', 0)  = -1
     * StringKit.lastIndexOf("aabaabaa", 'b', 9)  = 5
     * StringKit.lastIndexOf("aabaabaa", 'b', -1) = -1
     * StringKit.lastIndexOf("aabaabaa", 'a', 0)  = 0
     * </pre>
     * @param seq        待查找的字符序列，可以为null
     * @param searchChar 要查找的字符
     * @param startPos   开始位置，负数将当作0处理; 大于待查找的字符串的长度时将查找整个字符串
     * @return 最后一次出现的位置，如果没有找到或输入seq为null，将返回-1
     * @since 1.0.0
     */
    fun lastIndexOf(seq: CharSequence?, searchChar: Int, startPos: Int): Int =
        StringUtils.lastIndexOf(seq, searchChar, startPos)

    /**
     * 在字符串 seq 中查找最后一次出现字符串 searchChar 的位置(从后往前第一次出现的位置)
     *
     * <pre>
     * StringKit.lastIndexOf(null, *)          = -1
     * StringKit.lastIndexOf(*, null)          = -1
     * StringKit.lastIndexOf("", "")           = 0
     * StringKit.lastIndexOf("aabaabaa", "a")  = 7
     * StringKit.lastIndexOf("aabaabaa", "b")  = 5
     * StringKit.lastIndexOf("aabaabaa", "ab") = 4
     * StringKit.lastIndexOf("aabaabaa", "")   = 8
     * </pre>
     * @param seq       待查找的字符序列，可以为null
     * @param searchSeq 要查找的字符串
     * @return 最后一次出现的位置，如果没有找到或输入seq为null，将返回-1
     * @since 1.0.0
     */
    fun lastIndexOf(seq: CharSequence?, searchSeq: CharSequence?): Int = StringUtils.lastIndexOf(seq, searchSeq)

    /**
     * 从后往前查找，返回字符串 searchStr 在字符串 str 中第 ordinal 次出现的位置
     *
     * <pre>
     * StringKit.lastOrdinalIndexOf(null, *, *)          = -1
     * StringKit.lastOrdinalIndexOf(*, null, *)          = -1
     * StringKit.lastOrdinalIndexOf("", "", *)           = 0
     * StringKit.lastOrdinalIndexOf("aabaabaa", "a", 1)  = 7
     * StringKit.lastOrdinalIndexOf("aabaabaa", "a", 2)  = 6
     * StringKit.lastOrdinalIndexOf("aabaabaa", "b", 1)  = 5
     * StringKit.lastOrdinalIndexOf("aabaabaa", "b", 2)  = 2
     * StringKit.lastOrdinalIndexOf("aabaabaa", "ab", 1) = 4
     * StringKit.lastOrdinalIndexOf("aabaabaa", "ab", 2) = 1
     * StringKit.lastOrdinalIndexOf("aabaabaa", "", 1)   = 8
     * StringKit.lastOrdinalIndexOf("aabaabaa", "", 2)   = 8
     * </pre>
     * @param str       待查找的字符序列，可以为null
     * @param searchStr 要查找的字符串
     * @param ordinal   开始位置，负数将当作0处理; 大于待查找的字符串的长度时将查找整个字符串
     * @return 最后ordina次出现的位置，如果没有找到或输入seq为null，将返回-1
     * @since 1.0.0
     */
    fun lastOrdinalIndexOf(str: CharSequence?, searchStr: CharSequence?, ordinal: Int): Int =
        StringUtils.lastOrdinalIndexOf(str, searchStr, ordinal)

    /**
     * 返回字符串 searchChar 从 startPos 开始在字符串 seq 中最后一次出现的位置(从后往前第一次出现的位置)
     *
     * <pre>
     * StringKit.lastIndexOf(null, *, *)          = -1
     * StringKit.lastIndexOf(*, null, *)          = -1
     * StringKit.lastIndexOf("aabaabaa", "a", 8)  = 7
     * StringKit.lastIndexOf("aabaabaa", "b", 8)  = 5
     * StringKit.lastIndexOf("aabaabaa", "ab", 8) = 4
     * StringKit.lastIndexOf("aabaabaa", "b", 9)  = 5
     * StringKit.lastIndexOf("aabaabaa", "b", -1) = -1
     * StringKit.lastIndexOf("aabaabaa", "a", 0)  = 0
     * StringKit.lastIndexOf("aabaabaa", "b", 0)  = -1
     * </pre>
     * @param seq       待查找的字符序列，可以为null
     * @param searchSeq 要查找的字符串
     * @param startPos  开始位置，负数将当作0处理; 大于待查找的字符串的长度时将查找整个字符串
     * @return 最后一次出现的位置，如果没有找到或输入seq为null，将返回-1
     * @since 1.0.0
     */
    fun lastIndexOf(seq: CharSequence?, searchSeq: CharSequence?, startPos: Int): Int =
        StringUtils.lastIndexOf(seq, searchSeq, startPos)

    /**
     * 返回字符 searchChar 在字符串 str 中最后一次出现的位置，大小不敏感
     *
     * <pre>
     * StringKit.lastIndexOfIgnoreCase(null, *)          = -1
     * StringKit.lastIndexOfIgnoreCase(*, null)          = -1
     * StringKit.lastIndexOfIgnoreCase("aabaabaa", "A")  = 7
     * StringKit.lastIndexOfIgnoreCase("aabaabaa", "B")  = 5
     * StringKit.lastIndexOfIgnoreCase("aabaabaa", "AB") = 4
     * </pre>
     * @param str       待查找的字符序列，可以为null
     * @param searchStr 要查找的字符序列，可以为null
     * @return 最后一次出现的位置，如果没有找到或输入seq为null，将返回-1
     * @since 1.0.0
     */
    fun lastIndexOfIgnoreCase(str: CharSequence?, searchStr: CharSequence?): Int =
        StringUtils.lastIndexOfIgnoreCase(str, searchStr)

    /**
     * 返回字符 searchChar 在字符串 str 中从 startPos 开始最后一次出现的位置(从后往前第一次出现的位置)，大小不敏感
     *
     * <pre>
     * StringKit.lastIndexOfIgnoreCase(null, *, *)          = -1
     * StringKit.lastIndexOfIgnoreCase(*, null, *)          = -1
     * StringKit.lastIndexOfIgnoreCase("aabaabaa", "A", 8)  = 7
     * StringKit.lastIndexOfIgnoreCase("aabaabaa", "B", 8)  = 5
     * StringKit.lastIndexOfIgnoreCase("aabaabaa", "AB", 8) = 4
     * StringKit.lastIndexOfIgnoreCase("aabaabaa", "B", 9)  = 5
     * StringKit.lastIndexOfIgnoreCase("aabaabaa", "B", -1) = -1
     * StringKit.lastIndexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     * StringKit.lastIndexOfIgnoreCase("aabaabaa", "B", 0)  = -1
     * </pre>
     * @param str       待查找的字符序列，可以为null
     * @param searchStr 要查找的字符序列，可以为null
     * @param startPos  开始位置，负数将当作0处理; 大于待查找的字符串的长度时将查找整个字符串
     * @return 最后一次出现的位置，如果没有找到或输入seq为null，将返回-1
     * @since 1.0.0
     */
    fun lastIndexOfIgnoreCase(str: CharSequence?, searchStr: CharSequence?, startPos: Int): Int =
        StringUtils.lastIndexOfIgnoreCase(str, searchStr, startPos)
    //endregion IndexOf

    //region Contains
    /**
     * 检查字符串 seq 是否包含字符 searchChar
     *
     * <pre>
     * StringKit.contains(null, *)    = false
     * StringKit.contains("", *)      = false
     * StringKit.contains("abc", 'a') = true
     * StringKit.contains("abc", 'z') = false
     * </pre>
     * @param seq        待查找的字符序列，可以为null
     * @param searchChar 要查找的字符
     * @return true：如果字符串 seq 包含字符 searchChar, false： 如果不包含或字符串 seq 为null或空串
     * @since 1.0.0
     */
    fun contains(seq: CharSequence?, searchChar: Int): Boolean = StringUtils.contains(seq, searchChar)

    /**
     * 检查字符串 seq 是否包含字符串 searchSeq
     *
     * <pre>
     * StringKit.contains(null, *)     = false
     * StringKit.contains(*, null)     = false
     * StringKit.contains("", "")      = true
     * StringKit.contains("abc", "")   = true
     * StringKit.contains("abc", "a")  = true
     * StringKit.contains("abc", "z")  = false
     * </pre>
     * @param seq       待查找的字符序列，可以为null
     * @param searchSeq 要查找的字符串, 可以为null
     * @return true：如果字符串 seq 包含字符串 searchSeq, false： 如果不包含或字符串 seq 为null
     * @since 1.0.0
     */
    fun contains(seq: CharSequence?, searchSeq: CharSequence?): Boolean = StringUtils.contains(seq, searchSeq)

    /**
     * 检查字符串 str 是否包含字符串 searchStr，忽略大小写
     *
     * <pre>
     * StringKit.contains(null, *) = false
     * StringKit.contains(*, null) = false
     * StringKit.contains("", "") = true
     * StringKit.contains("abc", "") = true
     * StringKit.contains("abc", "a") = true
     * StringKit.contains("abc", "z") = false
     * StringKit.contains("abc", "A") = true
     * StringKit.contains("abc", "Z") = false
     * </pre>
     * @param str       待查找的字符序列，可以为null
     * @param searchStr 要查找的字符串, 可以为null
     * @return true：如果字符串 str 包含(忽略大小写)字符串 searchStr, false： 如果不包含或字符串 str 为null
     * @since 1.0.0
     */
    fun containsIgnoreCase(str: CharSequence?, searchStr: CharSequence?): Boolean =
        StringUtils.containsIgnoreCase(str, searchStr)

    /**
     * 检查给定是字符串是否包含任何空白字符
     *
     * @param seq 待查找的字符序列，可以为null
     * @return true：如果字符串非空至少包含1个空白字符, false： 如果不包含或字符串为null或空串
     * @see java.lang.Character.isWhitespace
     * @since 1.0.0
     */
    fun containsWhitespace(seq: CharSequence?): Boolean = StringUtils.containsWhitespace(seq)
    //endregion Contains

    //region IndexOfAny
    /**
     * 在字符序列 cs 中查找给定的一组字符 searchChars，返回第一次出现任何字符的位置
     *
     * <pre>
     * StringKit.indexOfAny(null, *)                = -1
     * StringKit.indexOfAny("", *)                  = -1
     * StringKit.indexOfAny(*, null)                = -1
     * StringKit.indexOfAny(*, [])                  = -1
     * StringKit.indexOfAny("zzabyycdxx",['z','a']) = 0
     * StringKit.indexOfAny("zzabyycdxx",['b','y']) = 3
     * StringKit.indexOfAny("aba", ['z'])           = -1
     * </pre>
     * @param cs          待查找的字符序列，可以为null
     * @param searchChars 要查找的字符组, 可以为null
     * @return 任何第一次匹配的字符的下标。如果没有找到或cs、searchChars两者之一为null，将返回-1
     * @since 1.0.0
     */
    fun indexOfAny(cs: CharSequence?, vararg searchChars: Char): Int = StringUtils.indexOfAny(cs, *searchChars)

    /**
     * 在字符序列 cs 中查找给定的一组字符 searchChars，返回第一次出现给定的任何字符的位置
     *
     * <pre>
     * StringKit.indexOfAny(null, *)            = -1
     * StringKit.indexOfAny("", *)              = -1
     * StringKit.indexOfAny(*, null)            = -1
     * StringKit.indexOfAny(*, "")              = -1
     * StringKit.indexOfAny("zzabyycdxx", "za") = 0
     * StringKit.indexOfAny("zzabyycdxx", "by") = 3
     * StringKit.indexOfAny("aba","z")          = -1
     * </pre>
     * @param cs          待查找的字符序列，可以为null
     * @param searchChars 要查找的字符组, 可以为null
     * @return 任何第一次匹配的字符的下标。如果没有找到或cs、searchChars两者之一为null，将返回-1
     * @since 1.0.0
     */
    fun indexOfAny(cs: CharSequence?, searchChars: String?): Int = StringUtils.indexOfAny(cs, searchChars)
    //endregion IndexOfAny

    //region ContainsAny
    /**
     * 检查是否字符序列 cs 包含给定的字符组 searchChars 的任何一个字符
     *
     * <pre>
     * StringKit.containsAny(null, *)                = false
     * StringKit.containsAny("", *)                  = false
     * StringKit.containsAny(*, null)                = false
     * StringKit.containsAny(*, [])                  = false
     * StringKit.containsAny("zzabyycdxx",['z','a']) = true
     * StringKit.containsAny("zzabyycdxx",['b','y']) = true
     * StringKit.containsAny("aba", ['z'])           = false
     * </pre>
     * @param cs          待查找的字符序列，可以为null
     * @param searchChars 要查找的字符组, 可以为null
     * @return true：任何给定的字符被找到，false：未找到或cs、searchChars两者之一为null
     * @since 1.0.0
     */
    fun containsAny(cs: CharSequence?, vararg searchChars: Char): Boolean = StringUtils.containsAny(cs, *searchChars)

    /**
     * 检查是否字符序列 cs 包含给定的字符组 searchChars 的任何一个字符
     *
     * <pre>
     * StringKit.containsAny(null, *)            = false
     * StringKit.containsAny("", *)              = false
     * StringKit.containsAny(*, null)            = false
     * StringKit.containsAny(*, "")              = false
     * StringKit.containsAny("zzabyycdxx", "za") = true
     * StringKit.containsAny("zzabyycdxx", "by") = true
     * StringKit.containsAny("aba","z")          = false
     * </pre>
     * @param cs          待查找的字符序列，可以为null
     * @param searchChars 要查找的字符组, 可以为null
     * @return true：任何给定的字符被找到，false：未找到或cs、searchChars两者之一为null
     * @since 1.0.0
     */
    fun containsAny(cs: CharSequence?, searchChars: CharSequence?): Boolean = StringUtils.containsAny(cs, searchChars)
    //endregion ContainsAny

    //region IndexOfAnyBut chars
    /**
     * 在字符序列 cs 中查找给定的一组字符 searchChars，返回第一次不出现给定的任何字符的位置
     *
     * <pre>
     * StringKit.indexOfAnyBut(null, *)                              = -1
     * StringKit.indexOfAnyBut("", *)                                = -1
     * StringKit.indexOfAnyBut(*, null)                              = -1
     * StringKit.indexOfAnyBut(*, [])                                = -1
     * StringKit.indexOfAnyBut("zzabyycdxx", new char[] {'z', 'a'} ) = 3
     * StringKit.indexOfAnyBut("aba", new char[] {'z'} )             = 0
     * StringKit.indexOfAnyBut("aba", new char[] {'a', 'b'} )        = -1
     *
     * </pre>
     * @param cs          待查找的字符序列，可以为null
     * @param searchChars 要查找的字符组, 可以为null
     * @return 任何第一次不匹配的字符的下标。如果没有找到或cs、searchChars两者之一为null，将返回-1
     * @since 1.0.0
     */
    fun indexOfAnyBut(cs: CharSequence?, vararg searchChars: Char): Int = StringUtils.indexOfAnyBut(cs, *searchChars)

    /**
     * 在字符序列 seq 中查找给定的一组字符 searchChars，返回第一次不出现给定的任何字符的位置
     *
     * <pre>
     * StringKit.indexOfAnyBut(null, *)            = -1
     * StringKit.indexOfAnyBut("", *)              = -1
     * StringKit.indexOfAnyBut(*, null)            = -1
     * StringKit.indexOfAnyBut(*, "")              = -1
     * StringKit.indexOfAnyBut("zzabyycdxx", "za") = 3
     * StringKit.indexOfAnyBut("zzabyycdxx", "")   = -1
     * StringKit.indexOfAnyBut("aba","ab")         = -1
     * </pre>
     * @param seq         待查找的字符序列，可以为null
     * @param searchChars 要查找的字符组, 可以为null
     * @return 任何第一次不匹配的字符的下标。如果没有找到或cs、searchChars两者之一为null，将返回-1
     * @since 1.0.0
     */
    fun indexOfAnyBut(seq: CharSequence?, searchChars: CharSequence?): Int = StringUtils.indexOfAnyBut(seq, searchChars)
    //endregion IndexOfAnyBut chars

    //region ContainsOnly
    /**
     * 检查字符序列 cs 是否只由 valid 中的字符组成
     *
     * <pre>
     * StringKit.containsOnly(null, *)       = false
     * StringKit.containsOnly(*, null)       = false
     * StringKit.containsOnly("", *)         = true
     * StringKit.containsOnly("ab", '')      = false
     * StringKit.containsOnly("abab", 'abc') = true
     * StringKit.containsOnly("ab1", 'abc')  = false
     * StringKit.containsOnly("abz", 'abc')  = false
     * </pre>
     * @param cs    待查找的字符序列，可以为null
     * @param valid 有效的字符组, 可以为null
     * @return true: 如果cs只由valid中的字符组成或cs为空串， false: cs包含其他字符或cs为null或valid为null
     * @since 1.0.0
     */
    fun containsOnly(cs: CharSequence?, vararg valid: Char): Boolean = StringUtils.containsOnly(cs, *valid)

    /**
     * 检查字符序列 cs 是否只由 validChars 中的字符组成
     *
     * <pre>
     * StringKit.containsOnly(null, *)       = false
     * StringKit.containsOnly(*, null)       = false
     * StringKit.containsOnly("", *)         = true
     * StringKit.containsOnly("ab", "")      = false
     * StringKit.containsOnly("abab", "abc") = true
     * StringKit.containsOnly("ab1", "abc")  = false
     * StringKit.containsOnly("abz", "abc")  = false
     * </pre>
     * @param cs         待查找的字符序列，可以为null
     * @param validChars 有效的字符组, 可以为null
     * @return true: 如果cs只由validChars中的字符组成或cs为空串， false:
     * cs包含其他字符或cs为null或validChars为null
     * @since 1.0.0
     */
    fun containsOnly(cs: CharSequence?, validChars: String?): Boolean = StringUtils.containsOnly(cs, validChars)
    //endregion ContainsOnly

    //region ContainsNone
    /**
     * 检查字符序列 cs 是否都不由 searchChars 中的字符组成
     *
     * <pre>
     * StringKit.containsNone(null, *)       = true
     * StringKit.containsNone(*, null)       = true
     * StringKit.containsNone("", *)         = true
     * StringKit.containsNone("ab", '')      = true
     * StringKit.containsNone("abab", 'xyz') = true
     * StringKit.containsNone("ab1", 'xyz')  = true
     * StringKit.containsNone("abz", 'xyz')  = false
     * </pre>
     * @param cs          待查找的字符序列，可以为null
     * @param searchChars 无效的字符组, 可以为null
     * @return true: 如果cs只都不由searchChars中的字符组成或cs为null或cs为空串或searchChars为null，
     * false: cs包含searchChars中的任何字符
     * @since 1.0.0
     */
    fun containsNone(cs: CharSequence?, vararg searchChars: Char): Boolean = StringUtils.containsNone(cs, *searchChars)

    /**
     * 检查字符序列 cs 是否都不由字符串 invalidChars 中的字符组成
     *
     * <pre>
     * StringKit.containsNone(null, *)       = true
     * StringKit.containsNone(*, null)       = true
     * StringKit.containsNone("", *)         = true
     * StringKit.containsNone("ab", "")      = true
     * StringKit.containsNone("abab", "xyz") = true
     * StringKit.containsNone("ab1", "xyz")  = true
     * StringKit.containsNone("abz", "xyz")  = false
     * </pre>
     * @param cs           待查找的字符序列，可以为null
     * @param invalidChars 无效的字符组, 可以为null
     * @return true: 如果cs只都不由searchChars中的字符组成或cs为null或cs为空串或searchChars为null，
     * false: cs包含searchChars中的任何字符
     * @since 1.0.0
     */
    fun containsNone(cs: CharSequence?, invalidChars: String?): Boolean = StringUtils.containsNone(cs, invalidChars)
    //endregion ContainsNone

    //region IndexOfAny strings
    /**
     * 在字符序列 str 中查找给定的一组字符串 searchStrs，返回第一次出现任何字符串的位置
     *
     * <pre>
     * StringKit.indexOfAny(null, *)                     = -1
     * StringKit.indexOfAny(*, null)                     = -1
     * StringKit.indexOfAny(*, [])                       = -1
     * StringKit.indexOfAny("zzabyycdxx", ["ab","cd"])   = 2
     * StringKit.indexOfAny("zzabyycdxx", ["cd","ab"])   = 2
     * StringKit.indexOfAny("zzabyycdxx", ["mn","op"])   = -1
     * StringKit.indexOfAny("zzabyycdxx", ["zab","aby"]) = 1
     * StringKit.indexOfAny("zzabyycdxx", [""])          = 0
     * StringKit.indexOfAny("", [""])                    = 0
     * StringKit.indexOfAny("", ["a"])                   = -1
     * </pre>
     * @param str        待查找的字符序列，可以为null
     * @param searchStrs 要查找的字符串组, 可以为null
     * @return 任何第一次匹配的字符串的下标。如果没有找到或cs为null、searchChars为null或空数组，将返回-1。
     * searchStrs如果有空串的元素，将返回0
     * @since 1.0.0
     */
    fun indexOfAny(str: CharSequence?, vararg searchStrs: CharSequence?): Int = StringUtils.indexOfAny(str, *searchStrs)

    /**
     * 在字符序列 str 中从后往前查找给定的一组字符串 searchStrs，返回第一次出现任何字符串的位置
     *
     * <pre>
     * StringKit.lastIndexOfAny(null, *)                   = -1
     * StringKit.lastIndexOfAny(*, null)                   = -1
     * StringKit.lastIndexOfAny(*, [])                     = -1
     * StringKit.lastIndexOfAny(*, [null])                 = -1
     * StringKit.lastIndexOfAny("zzabyycdxx", ["ab","cd"]) = 6
     * StringKit.lastIndexOfAny("zzabyycdxx", ["cd","ab"]) = 6
     * StringKit.lastIndexOfAny("zzabyycdxx", ["mn","op"]) = -1
     * StringKit.lastIndexOfAny("zzabyycdxx", ["mn","op"]) = -1
     * StringKit.lastIndexOfAny("zzabyycdxx", ["mn",""])   = 10
     * </pre>
     * @param str        待查找的字符序列，可以为null
     * @param searchStrs 要查找的字符串组, 可以为null
     * @return 从后往前找任何第一次匹配的字符串的下标。如果没有找到或cs为null、searchChars为null或空数组，将返回-1。
     * searchStrs如果有空串的元素，将返回0
     * @since 1.0.0
     */
    fun lastIndexOfAny(str: CharSequence?, vararg searchStrs: CharSequence?): Int =
        StringUtils.lastIndexOfAny(str, *searchStrs)
    //endregion IndexOfAny strings

    //region Substring
    /**
     * 从字符串 str 的指定位置 start 开始获取子串
     *
     * <pre>
     * StringKit.substring(null, *)   = null
     * StringKit.substring("", *)     = ""
     * StringKit.substring("abc", 0)  = "abc"
     * StringKit.substring("abc", 2)  = "c"
     * StringKit.substring("abc", 4)  = ""
     * StringKit.substring("abc", -2) = "bc"
     * StringKit.substring("abc", -4) = "abc"
     * </pre>
     * @param str   主串, 可以为null
     * @param start 开始位置，负数表示从后往前数
     * @return 从开始位置开始的子串。str为null将返回null, str为空串将返回空串
     * @since 1.0.0
     */
    fun substring(str: String?, start: Int): String? = StringUtils.substring(str, start)

    /**
     * 从字符串 str 获取子串，开始于位置 start 并结束于位置 end
     *
     * <pre>
     * StringKit.substring(null, *, *)    = null
     * StringKit.substring("", * ,  *)    = "";
     * StringKit.substring("abc", 0, 2)   = "ab"
     * StringKit.substring("abc", 2, 0)   = ""
     * StringKit.substring("abc", 2, 4)   = "c"
     * StringKit.substring("abc", 4, 6)   = ""
     * StringKit.substring("abc", 2, 2)   = ""
     * StringKit.substring("abc", -2, -1) = "b"
     * StringKit.substring("abc", -4, 2)  = "ab"
     * </pre>
     * @param str   主串, 可以为null
     * @param start 开始位置，负数表示从后往前数
     * @param end   结束位置，负数表示从后往前数
     * @return 从开始位置开始到结束位置的子串。str为null将返回null, str为空串或start<=end将返回空串
     * @since 1.0.0
     */
    fun substring(str: String?, start: Int, end: Int): String? = StringUtils.substring(str, start, end)
    //endregion Substring

    //region Left/Right/Mid
    /**
     * 返回字符串 str 最左边的 len 个字符
     *
     * <pre>
     * StringKit.left(null, *)    = null
     * StringKit.left(*, -ve)     = ""
     * StringKit.left("", *)      = ""
     * StringKit.left("abc", 0)   = ""
     * StringKit.left("abc", 2)   = "ab"
     * StringKit.left("abc", 4)   = "abc"
     * </pre>
     * @param str 主串, 可以为null
     * @param len 子串的长度
     * @return 最左边的字符串, str为null将返回null, str为空串或len为负数将返回空串
     * @since 1.0.0
     */
    fun left(str: String?, len: Int): String? = StringUtils.leftPad(str, len)

    /**
     * 返回字符串 str 最右边的 len 个字符
     *
     * <pre>
     * StringKit.right(null, *)    = null
     * StringKit.right(*, -ve)     = ""
     * StringKit.right("", *)      = ""
     * StringKit.right("abc", 0)   = ""
     * StringKit.right("abc", 2)   = "bc"
     * StringKit.right("abc", 4)   = "abc"
     * </pre>
     * @param str 主串, 可以为null
     * @param len 子串的长度
     * @return 最左边的字符串, str为null将返回null, str为空串或len为负数将返回空串
     * @since 1.0.0
     */
    fun right(str: String?, len: Int): String? = StringUtils.right(str, len)

    /**
     * 返回字符串 str 从 pos 位置开始的 len 个字符
     *
     * <pre>
     * StringKit.mid(null, *, *)    = null
     * StringKit.mid(*, *, -ve)     = ""
     * StringKit.mid("", 0, *)      = ""
     * StringKit.mid("abc", 0, 2)   = "ab"
     * StringKit.mid("abc", 0, 4)   = "abc"
     * StringKit.mid("abc", 2, 4)   = "c"
     * StringKit.mid("abc", 4, 2)   = ""
     * StringKit.mid("abc", -2, 2)  = "ab"
     * </pre>
     * @param str 主串, 可以为null
     * @param pos 开始位置, 负数将被当作0
     * @param len 子串的长度
     * @return 从 pos 位置开始的 len 个字符, str为null将返回null, str为空串或len为负数将返回空串
     * @since 1.0.0
     */
    fun mid(str: String?, pos: Int, len: Int): String? = StringUtils.mid(str, pos, len)
    //endregion Left/Right/Mid

    //region SubStringAfter/SubStringBefore
    /**
     * 从字符串 str 中获取第一次出现字符串 separator 前的子串
     *
     * <pre>
     * StringKit.substringBefore(null, *)      = null
     * StringKit.substringBefore("", *)        = ""
     * StringKit.substringBefore("abc", "a")   = ""
     * StringKit.substringBefore("abcba", "b") = "a"
     * StringKit.substringBefore("abc", "c")   = "ab"
     * StringKit.substringBefore("abc", "d")   = "abc"
     * StringKit.substringBefore("abc", "")    = ""
     * StringKit.substringBefore("abc", null)  = "abc"
     * </pre>
     * @param str       主串, 可以为null
     * @param separator 分隔串, 可以为null
     * @return 第一次出现字符串 separator 前的子串。str为null将返回null，
     * str或separator为空串将返回空串，separator为null或未找到将返回str
     * @since 1.0.0
     */
    fun substringBefore(str: String?, separator: String?): String? = StringUtils.substringBefore(str, separator)

    /**
     * 从字符串 str 中获取第一次出现字符串 separator 后的子串
     *
     * <pre>
     * StringKit.substringAfter(null, *)      = null
     * StringKit.substringAfter("", *)        = ""
     * StringKit.substringAfter(*, null)      = ""
     * StringKit.substringAfter("abc", "a")   = "bc"
     * StringKit.substringAfter("abcba", "b") = "cba"
     * StringKit.substringAfter("abc", "c")   = ""
     * StringKit.substringAfter("abc", "d")   = ""
     * StringKit.substringAfter("abc", "")    = "abc"
     * </pre>
     * @param str       主串, 可以为null
     * @param separator 分隔串, 可以为null
     * @return 第一次出现字符串 separator 后的子串。str为null将返回null，
     * str或separator为空串或未找到将返回空串，separator为null将返回str
     * @since 1.0.0
     */
    fun substringAfter(str: String?, separator: String?): String? = StringUtils.substringAfter(str, separator)

    /**
     * 从字符串 str 中获取最后一次(从后往前找第一次)出现字符串 separator 前的子串
     *
     * <pre>
     * StringKit.substringBeforeLast(null, *)      = null
     * StringKit.substringBeforeLast("", *)        = ""
     * StringKit.substringBeforeLast("abcba", "b") = "abc"
     * StringKit.substringBeforeLast("abc", "c")   = "ab"
     * StringKit.substringBeforeLast("a", "a")     = ""
     * StringKit.substringBeforeLast("a", "z")     = "a"
     * StringKit.substringBeforeLast("a", null)    = "a"
     * StringKit.substringBeforeLast("a", "")      = "a"
     * </pre>
     * @param str       主串, 可以为null
     * @param separator 分隔串, 可以为null
     * @return 最后一次出现字符串 separator 前的子串。str为null将返回null，
     * str或separator为空串将返回空串，separator为null或未找到将返回str
     * @since 1.0.0
     */
    fun substringBeforeLast(str: String?, separator: String?): String? = StringUtils.substringBeforeLast(str, separator)

    /**
     * 从字符串 str 中获取最后一次(从后往前找第一次)出现字符串 separator 后的子串
     *
     * <pre>
     * StringKit.substringAfterLast(null, *)      = null
     * StringKit.substringAfterLast("", *)        = ""
     * StringKit.substringAfterLast(*, "")        = ""
     * StringKit.substringAfterLast(*, null)      = ""
     * StringKit.substringAfterLast("abc", "a")   = "bc"
     * StringKit.substringAfterLast("abcba", "b") = "a"
     * StringKit.substringAfterLast("abc", "c")   = ""
     * StringKit.substringAfterLast("a", "a")     = ""
     * StringKit.substringAfterLast("a", "z")     = ""
     * </pre>
     * @param str       主串, 可以为null
     * @param separator 分隔串, 可以为null
     * @return 最后一次出现字符串 separator 后的子串。str为null将返回null，
     * str或separator为空串或未找到将返回空串，separator为null将返回str
     * @since 1.0.0
     */
    fun substringAfterLast(str: String?, separator: String?): String? = StringUtils.substringAfterLast(str, separator)
    //endregion SubStringAfter/SubStringBefore

    //region Substring between
    /**
     * 从字符串 str 中获取嵌在两个相同字符串 tag 中间的子串
     *
     * <pre>
     * StringKit.substringBetween(null, *)            = null
     * StringKit.substringBetween("", "")             = ""
     * StringKit.substringBetween("", "tag")          = null
     * StringKit.substringBetween("tagabctag", null)  = null
     * StringKit.substringBetween("tagabctag", "")    = ""
     * StringKit.substringBetween("tagabctag", "tag") = "abc"
     * </pre>
     * @param str 主串, 可以为null
     * @param tag 子串前后的字符串, 可以为null
     * @return 子串, 未找到或str为null或tag为null都将返回null，tag为空串将返回空串
     * @since 1.0.0
     */
    fun substringBetween(str: String?, tag: String?): String? = StringUtils.substringBetween(str, tag)

    /**
     * 从字符串 str 中获取嵌在字符串 open 和 close 中间的子串，返回第一次匹配的结果
     *
     * <pre>
     * StringKit.substringBetween("wx[b]yz", "[", "]") = "b"
     * StringKit.substringBetween(null, *, *)          = null
     * StringKit.substringBetween(*, null, *)          = null
     * StringKit.substringBetween(*, *, null)          = null
     * StringKit.substringBetween("", "", "")          = ""
     * StringKit.substringBetween("", "", "]")         = null
     * StringKit.substringBetween("", "[", "]")        = null
     * StringKit.substringBetween("yabcz", "", "")     = ""
     * StringKit.substringBetween("yabcz", "y", "z")   = "abc"
     * StringKit.substringBetween("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     * @param str   主串, 可以为null
     * @param open  子串前的字符串, 可以为null
     * @param close 子串后的字符串, 可以为null
     * @return 子串, 未找到或str为null或open/close为null都将返回null，open/close为空串将返回空串
     * @since 1.0.0
     */
    fun substringBetween(str: String?, open: String?, close: String?): String? =
        StringUtils.substringBetween(str, open, close)

    /**
     * 从字符串 str 中获取嵌在字符串 open 和 close 中间的子串，返回全部匹配结果
     *
     * <pre>
     * StringKit.substringsBetween("[a][b][c]", "[", "]") = ["a","b","c"]
     * StringKit.substringsBetween(null, *, *)            = null
     * StringKit.substringsBetween(*, null, *)            = null
     * StringKit.substringsBetween(*, *, null)            = null
     * StringKit.substringsBetween("", "[", "]")          = []
     * </pre>
     * @param str   主串, null返回null, 空串返回空串
     * @param open  标识子串开始的字符串, 空串返回null
     * @param close 标识子串结束的字符串, 空串返回null
     * @return 子串数组, 未找到返回null
     * @since 1.0.0
     */
    fun substringsBetween(str: String?, open: String?, close: String?): Array<String>? =
        StringUtils.substringsBetween(str, open, close)
    //endregion Substring between

    //region Splitting
    /**
     * 用空白符分隔给定的字符串，相邻的分隔符将被当作一个分隔符
     *
     * <pre>
     * StringKit.split(null)       = null
     * StringKit.split("")         = []
     * StringKit.split("abc def")  = ["abc", "def"]
     * StringKit.split("abc  def") = ["abc", "def"]
     * StringKit.split(" abc ")    = ["abc"]
     * </pre>
     * @param str 主串, 可以为null，null返回null，空串返回空数组
     * @return 子串数组
     * @since 1.0.0
     */
    fun split(str: String?): Array<String>? = StringUtils.split(str)

    /**
     * 用指定的字符分隔给定的字符串，相邻的分隔符当作一个分隔符
     *
     * <pre>
     * StringKit.split(null, *)         = null
     * StringKit.split("", *)           = []
     * StringKit.split("a.b.c", '.')    = ["a", "b", "c"]
     * StringKit.split("a..b.c", '.')   = ["a", "b", "c"]
     * StringKit.split("a:b:c", '.')    = ["a:b:c"]
     * StringKit.split("a b c", ' ')    = ["a", "b", "c"]
     * </pre>
     * @param str           主串, 可以为null，null返回null，空串返回空数组
     * @param separatorChar 分隔符
     * @return 子串数组
     * @since 1.0.0
     */
    fun split(str: String?, separatorChar: Char): Array<String>? = StringUtils.split(str, separatorChar)

    /**
     * 用指定的字符串中的字符(任意排列组合)分隔给定的字符串，相邻的分隔符当作一个分隔符
     *
     * <pre>
     * StringKit.split(null, *)         = null
     * StringKit.split("", *)           = []
     * StringKit.split("abc def", null) = ["abc", "def"]
     * StringKit.split("abc def", " ")  = ["abc", "def"]
     * StringKit.split("abc  def", " ") = ["abc", "def"]
     * StringKit.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
     * StringKit.split("ab-!-cd-!ef", "!-") = ["ab", "cd", "ef"]
     * </pre>
     * @param str            主串, 可以为null，null返回null，空串返回空数组
     * @param separatorChars 分隔符串，为null将当作空白符
     * @return 子串数组
     * @since 1.0.0
     */
    fun split(str: String?, separatorChars: String?): Array<String> = StringUtils.split(str, separatorChars)

    /**
     * 用指定的字符串中的字符(任意排列组合)分隔给定的字符串，限制返回的结果数，相邻的分隔符当作一个分隔符
     *
     * <pre>
     * StringKit.split(null, *, *)            = null
     * StringKit.split("", *, *)              = []
     * StringKit.split("ab de fg", null, 0)   = ["ab", "cd", "ef"]
     * StringKit.split("ab   de fg", null, 0) = ["ab", "cd", "ef"]
     * StringKit.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
     * StringKit.split("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
     * </pre>
     * @param str            主串, 可以为null，null返回null，空串返回空数组
     * @param separatorChars 分隔符串，为null将当作空白符
     * @param max            最大结果数。0或负数表示不受限，实际结果如果大于该参数，结果将返回最后的max个元素
     * @return 子串数组
     * @since 1.0.0
     */
    fun split(str: String?, separatorChars: String?, max: Int): Array<String>? =
        StringUtils.split(str, separatorChars, max)

    /**
     * 用指定的字符串(当作一个不可分的整体)分隔给定的字符串，相邻的分隔串当作一个分隔串
     *
     * <pre>
     * StringKit.splitByWholeSeparator(null, *)               = null
     * StringKit.splitByWholeSeparator("", *)                 = []
     * StringKit.splitByWholeSeparator("ab de fg", null)      = ["ab", "de", "fg"]
     * StringKit.splitByWholeSeparator("ab   de fg", null)    = ["ab", "de", "fg"]
     * StringKit.splitByWholeSeparator("ab:cd:ef", ":")       = ["ab", "cd", "ef"]
     * StringKit.splitByWholeSeparator("ab-!-cd-!-ef", "-!-") = ["ab", "cd", "ef"]
     * </pre>
     * @param str       主串, 可以为null，null返回null，空串返回空数组
     * @param separator 分隔串，为null将当作空白符
     * @return 子串数组
     * @since 1.0.0
     */
    fun splitByWholeSeparator(str: String?, separator: String?): Array<String>? =
        StringUtils.splitByWholeSeparator(str, separator)

    /**
     * 用指定的字符串(当作一个不可分的整体)分隔给定的字符串，限制返回的结果数，相邻的分隔串当作一个分隔串
     *
     * <pre>
     * StringKit.splitByWholeSeparator(null, *, *)               = null
     * StringKit.splitByWholeSeparator("", *, *)                 = []
     * StringKit.splitByWholeSeparator("ab de fg", null, 0)      = ["ab", "de", "fg"]
     * StringKit.splitByWholeSeparator("ab   de fg", null, 0)    = ["ab", "de", "fg"]
     * StringKit.splitByWholeSeparator("ab:cd:ef", ":", 2)       = ["ab", "cd:ef"]
     * StringKit.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
     * StringKit.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
     * </pre>
     * @param str       主串, 可以为null，null返回null，空串返回空数组
     * @param separator 分隔串，为null将当作空白符
     * @param max       最大结果数。0或负数表示不受限，实际结果如果大于该参数，结果将返回最后的max个元素
     * @return 子串数组
     * @since 1.0.0
     */
    fun splitByWholeSeparator(str: String?, separator: String?, max: Int): Array<String>? =
        StringUtils.splitByWholeSeparator(str, separator, max)

    /**
     * 用指定的字符串(当作一个不可分的整体)分隔给定的字符串，相邻的分隔串被当作空串的分隔符
     *
     * <pre>
     * StringKit.splitByWholeSeparatorPreserveAllTokens(null, *)               = null
     * StringKit.splitByWholeSeparatorPreserveAllTokens("", *)                 = []
     * StringKit.splitByWholeSeparatorPreserveAllTokens("ab de fg", null)      = ["ab", "de", "fg"]
     * StringKit.splitByWholeSeparatorPreserveAllTokens("ab   de fg", null)    = ["ab", "", "", "de", "fg"]
     * StringKit.splitByWholeSeparatorPreserveAllTokens("ab:cd:ef", ":")       = ["ab", "cd", "ef"]
     * StringKit.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-") = ["ab", "cd", "ef"]
     * </pre>
     * @param str       主串, 可以为null
     * @param separator 分隔串，为null将当作空白符
     * @return 子串数组
     * @since 1.0.0
     */
    fun splitByWholeSeparatorPreserveAllTokens(str: String?, separator: String?): Array<String>? =
        StringUtils.splitByWholeSeparator(str, separator)

    /**
     * 用指定的字符串(当作一个不可分的整体)分隔给定的字符串，限制结果数，相邻的分隔串被当作空串的分隔符
     *
     * <pre>
     * StringKit.splitByWholeSeparatorPreserveAllTokens(null, *, *)               = null
     * StringKit.splitByWholeSeparatorPreserveAllTokens("", *, *)                 = []
     * StringKit.splitByWholeSeparatorPreserveAllTokens("ab de fg", null, 0)      = ["ab", "de", "fg"]
     * StringKit.splitByWholeSeparatorPreserveAllTokens("ab   de fg", null, 0)    = ["ab", "", "", "de", "fg"]
     * StringKit.splitByWholeSeparatorPreserveAllTokens("ab:cd:ef", ":", 2)       = ["ab", "cd:ef"]
     * StringKit.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
     * StringKit.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
     * </pre>
     * @param str       主串, 可以为null，为null将返回null
     * @param separator 分隔串，为null将当作空白符
     * @param max       最大结果数。0或负数表示不受限，实际结果如果大于该参数，结果将返回最后的max个元素
     * @return 子串数组
     * @since 1.0.0
     */
    fun splitByWholeSeparatorPreserveAllTokens(str: String?, separator: String?, max: Int): Array<String>? =
        StringUtils.splitByWholeSeparator(str, separator, max)

    /**
     * 用空白符分隔字符串，保留所有符号，包括由相邻分隔符创建的空串，相邻的分隔串被当作空串的分隔符
     *
     * <pre>
     * StringKit.splitPreserveAllTokens(null)       = null
     * StringKit.splitPreserveAllTokens("")         = []
     * StringKit.splitPreserveAllTokens("abc def")  = ["abc", "def"]
     * StringKit.splitPreserveAllTokens("abc  def") = ["abc", "", "def"]
     * StringKit.splitPreserveAllTokens(" abc ")    = ["", "abc", ""]
     * </pre>
     * @param str 主串, 可以为null，为null将返回null
     * @return 子串数组
     * @since 1.0.0
     */
    fun splitPreserveAllTokens(str: String?): Array<String>? = StringUtils.splitPreserveAllTokens(str)

    /**
     * 用指定分隔符分隔字符串，保留所有符号，包括由相邻分隔符创建的空串，相邻的分隔串被当作空串的分隔符
     *
     * <pre>
     * StringKit.splitPreserveAllTokens(null, *)         = null
     * StringKit.splitPreserveAllTokens("", *)           = []
     * StringKit.splitPreserveAllTokens("a.b.c", '.')    = ["a", "b", "c"]
     * StringKit.splitPreserveAllTokens("a..b.c", '.')   = ["a", "", "b", "c"]
     * StringKit.splitPreserveAllTokens("a:b:c", '.')    = ["a:b:c"]
     * StringKit.splitPreserveAllTokens("a\tb\nc", null) = ["a", "b", "c"]
     * StringKit.splitPreserveAllTokens("a b c", ' ')    = ["a", "b", "c"]
     * StringKit.splitPreserveAllTokens("a b c ", ' ')   = ["a", "b", "c", ""]
     * StringKit.splitPreserveAllTokens("a b c  ", ' ')   = ["a", "b", "c", "", ""]
     * StringKit.splitPreserveAllTokens(" a b c", ' ')   = ["", a", "b", "c"]
     * StringKit.splitPreserveAllTokens("  a b c", ' ')  = ["", "", a", "b", "c"]
     * StringKit.splitPreserveAllTokens(" a b c ", ' ')  = ["", a", "b", "c", ""]
     * </pre>
     * @param str           主串, 可以为null，为null将返回null
     * @param separatorChar 分隔符
     * @return 子串数组
     * @since 1.0.0
     */
    fun splitPreserveAllTokens(str: String?, separatorChar: Char): Array<String>? =
        StringUtils.splitPreserveAllTokens(str, separatorChar)

    /**
     * 用指定分隔串的任意字符分隔字符串，保留所有符号，包括由相邻分隔符创建的空串，相邻的分隔符被当作空串的分隔符
     *
     * <pre>
     * StringKit.splitPreserveAllTokens(null, *)           = null
     * StringKit.splitPreserveAllTokens("", *)             = []
     * StringKit.splitPreserveAllTokens("abc def", null)   = ["abc", "def"]
     * StringKit.splitPreserveAllTokens("abc def", " ")    = ["abc", "def"]
     * StringKit.splitPreserveAllTokens("abc  def", " ")   = ["abc", "", def"]
     * StringKit.splitPreserveAllTokens("ab:cd:ef", ":")   = ["ab", "cd", "ef"]
     * StringKit.splitPreserveAllTokens("ab:cd:ef:", ":")  = ["ab", "cd", "ef", ""]
     * StringKit.splitPreserveAllTokens("ab:cd:ef::", ":") = ["ab", "cd", "ef", "", ""]
     * StringKit.splitPreserveAllTokens("ab::cd:ef", ":")  = ["ab", "", cd", "ef"]
     * StringKit.splitPreserveAllTokens(":cd:ef", ":")     = ["", cd", "ef"]
     * StringKit.splitPreserveAllTokens("::cd:ef", ":")    = ["", "", cd", "ef"]
     * StringKit.splitPreserveAllTokens(":cd:ef:", ":")    = ["", cd", "ef", ""]
     * </pre>
     * @param str            主串, 可以为null，为null将返回null
     * @param separatorChars 分隔符串，为空或空串将被当作空白符
     * @return 子串数组
     * @since 1.0.0
     */
    fun splitPreserveAllTokens(str: String?, separatorChars: String?): Array<String>? =
        StringUtils.splitPreserveAllTokens(str, separatorChars)

    /**
     * 用指定分隔串的任意字符分隔字符串，保留所有符号，包括由相邻分隔符创建的空串，相邻的分隔符被当作空串的分隔符，限制结果数
     *
     * <pre>
     * StringKit.splitPreserveAllTokens(null, *, *)            = null
     * StringKit.splitPreserveAllTokens("", *, *)              = []
     * StringKit.splitPreserveAllTokens("ab de fg", null, 0)   = ["ab", "cd", "ef"]
     * StringKit.splitPreserveAllTokens("ab   de fg", null, 0) = ["ab", "cd", "ef"]
     * StringKit.splitPreserveAllTokens("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
     * StringKit.splitPreserveAllTokens("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
     * StringKit.splitPreserveAllTokens("ab   de fg", null, 2) = ["ab", "  de fg"]
     * StringKit.splitPreserveAllTokens("ab   de fg", null, 3) = ["ab", "", " de fg"]
     * StringKit.splitPreserveAllTokens("ab   de fg", null, 4) = ["ab", "", "", "de fg"]
     * </pre>
     * @param str            主串, 可以为null，为null将返回null
     * @param separatorChars 分隔符串，为空或空串将被当作空白符
     * @param max            最大结果数。0或负数表示不受限，实际结果如果大于该参数，结果将返回最后的max个元素
     * @return 子串数组
     * @since 1.0.0
     */
    fun splitPreserveAllTokens(str: String?, separatorChars: String?, max: Int): Array<String>? =
        StringUtils.splitPreserveAllTokens(str, separatorChars, max)

    /**
     * 根据字符的类型(由`java.lang.Character.getType(char)`
     * 返回)分隔字符串。多个连续的相同类型的字符将被当作同一组返回。
     *
     * <pre>
     * StringKit.splitByCharacterType(null)         = null
     * StringKit.splitByCharacterType("")           = []
     * StringKit.splitByCharacterType("ab de fg")   = ["ab", " ", "de", " ", "fg"]
     * StringKit.splitByCharacterType("ab   de fg") = ["ab", "   ", "de", " ", "fg"]
     * StringKit.splitByCharacterType("ab:cd:ef")   = ["ab", ":", "cd", ":", "ef"]
     * StringKit.splitByCharacterType("number5")    = ["number", "5"]
     * StringKit.splitByCharacterType("fooBar")     = ["foo", "B", "ar"]
     * StringKit.splitByCharacterType("foo200Bar")  = ["foo", "200", "B", "ar"]
     * StringKit.splitByCharacterType("ASFRules")   = ["ASFR", "ules"]
     * </pre>
     * @param str 主串, 可以为null，为null将返回null
     * @return 子串数组
     * @since 1.0.0
     */
    fun splitByCharacterType(str: String?): Array<String>? = StringUtils.splitByCharacterTypeCamelCase(str)

    /**
     * 根据字符的类型(由`java.lang.Character.getType(char)`
     * 返回)分隔字符串。多个连续的相同类型的字符将被当作同一组返回。 以下情况除外： 大写字母紧跟着小写字母(驼峰)，这样，该大写字母将属于小写字母的组
     *
     * <pre>
     * StringKit.splitByCharacterTypeCamelCase(null)         = null
     * StringKit.splitByCharacterTypeCamelCase("")           = []
     * StringKit.splitByCharacterTypeCamelCase("ab de fg")   = ["ab", " ", "de", " ", "fg"]
     * StringKit.splitByCharacterTypeCamelCase("ab   de fg") = ["ab", "   ", "de", " ", "fg"]
     * StringKit.splitByCharacterTypeCamelCase("ab:cd:ef")   = ["ab", ":", "cd", ":", "ef"]
     * StringKit.splitByCharacterTypeCamelCase("number5")    = ["number", "5"]
     * StringKit.splitByCharacterTypeCamelCase("fooBar")     = ["foo", "Bar"]
     * StringKit.splitByCharacterTypeCamelCase("foo200Bar")  = ["foo", "200", "Bar"]
     * StringKit.splitByCharacterTypeCamelCase("ASFRules")   = ["ASF", "Rules"]
     * </pre>
     * @param str 主串, 可以为null，为null将返回null
     * @return 子串数组
     * @since 1.0.0
     */
    fun splitByCharacterTypeCamelCase(str: String?): Array<String>? = StringUtils.splitByCharacterType(str)
    //endregion Splitting

    //region Joining
    /**
     * 将多个字符串按先后顺序连接在一起
     *
     * <pre>
     * StringKit.join(null)            = null
     * StringKit.join([])              = ""
     * StringKit.join([null])          = ""
     * StringKit.join(["a", "b", "c"]) = "abc"
     * StringKit.join([null, "", "a"]) = "a"
     * </pre>
     * @param <T>      元素的类型
     * @param elements 要连接的对象组, 可以为null，为null将返回null, 数组元素为null当空串处理
     * @return 连接后的字符串
     * @since 1.0.0
    </T> */
    fun <T> join(vararg elements: T): String? = StringUtils.join(*elements)

    /**
     * 将多个字符串按先后顺序连接在一起，两两被连接的字符串中间插入分隔符
     *
     * <pre>
     * StringKit.join(*, null)               = null
     * StringKit.join(*, [])                 = ""
     * StringKit.join(*, [null])             = ""
     * StringKit.join(';', ["a", "b", "c"])  = "a;b;c"
     * StringKit.join(null, ["a", "b", "c"]) = "abc"
     * StringKit.join(';', [null, "", "a"])  = ";;a"
     * </pre>
     * @param separator 分隔符
     * @param array     要连接的对象组, 可以为null，为null将返回null, 数组元素为null当空串处理
     * @return 连接后的字符串
     * @since 1.0.0
     */
    fun join(separator: Char, vararg array: Any?): String? = StringUtils.join(array, separator)

    /**
     * 将多个字符串按先后顺序连接在一起，两两被连接的字符串中间插入分隔符
     *
     * <pre>
     * StringKit.join(',', *, *, null)               = null
     * StringKit.join(',', 1, 1, [])                 = ""
     * StringKit.join(',', 1, 1, [null])             = ""
     * StringKit.join(';', 1, 3, ["a", "b", "c"])  = "b;c"
     * </pre>
     * @param separator  分隔符
     * @param startIndex 开始连接的第一个元素的数组下标，下标越界将报错。
     * @param endIndex   结束连接的最后一个(不包括)元素的数组下标，下标越界将报错。
     * @param array      要连接的对象组, 可以为null，为null将返回null, 数组元素为null当空串处理
     * @return 连接后的字符串
     * @since 1.0.0
     */
    fun join(separator: Char, startIndex: Int, endIndex: Int, vararg array: Any?): String? =
        StringUtils.join(array, separator, startIndex, endIndex)

    /**
     * 将多个字符串按先后顺序连接在一起，两两被连接的字符串中间插入分隔串
     *
     * <pre>
     * StringKit.join(*, null)                = null
     * StringKit.join(*, [])                  = ""
     * StringKit.join(*, [null])              = ""
     * StringKit.join("--", ["a", "b", "c"])  = "a--b--c"
     * StringKit.join(null, ["a", "b", "c"])  = "abc"
     * StringKit.join("", ["a", "b", "c"])    = "abc"
     * StringKit.join(',', [null, "", "a"])   = ",,a"
     * </pre>
     * @param separator 分隔串, null当空串处理
     * @param array     要连接的对象组, 可以为null，为null将返回null, 数组元素为null当空串处理
     * @return 连接后的字符串
     * @since 1.0.0
     */
    fun join(separator: String?, vararg array: Any?): String? = StringUtils.join(array, separator)

    /**
     * 将多个字符串按先后顺序连接在一起，两两被连接的字符串中间插入分隔串
     *
     * <pre>
     * StringKit.join(*, null)                = null
     * StringKit.join(*, [])                  = ""
     * StringKit.join(*, [null])              = ""
     * StringKit.join("--", ["a", "b", "c"])  = "a--b--c"
     * StringKit.join(null, ["a", "b", "c"])  = "abc"
     * StringKit.join("", ["a", "b", "c"])    = "abc"
     * StringKit.join(',', [null, "", "a"])   = ",,a"
     * </pre>
     * @param separator  分隔串, null当空串处理
     * @param startIndex 开始连接的第一个元素的数组下标，下标越界将报错。
     * @param endIndex   结束连接的最后一个(不包括)元素的数组下标，下标越界将报错。
     * @param array      要连接的对象组, 可以为null，为null将返回null, 数组元素为null当空串处理
     * @return 连接后的字符串
     * @since 1.0.0
     */
    fun join(separator: String?, startIndex: Int, endIndex: Int, vararg array: Any?): String? =
        StringUtils.join(array, separator, startIndex, endIndex)

    /**
     * 将多个字符串(由`Iterator`提供)按先后顺序连接在一起，两两被连接的字符串中间插入分隔符
     *
     * <pre>
     * StringKit.join(null, *)               = null
     * StringKit.join([], *)                 = ""
     * StringKit.join([null], *)             = ""
     * StringKit.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringKit.join(["a", "b", "c"], null) = "abc"
     * StringKit.join([null, "", "a"], ';')  = ";;a"
     * </pre>
     * @param iterator  要连接的对象迭代器, 可以为null，为null将返回null, 元素为null当空串处理
     * @param separator 分隔符
     * @return 连接后的字符串
     * @since 1.0.0
     */
    fun join(iterator: Iterator<*>?, separator: Char): String? =
        StringUtils.join(iterator, separator)

    /**
     * 将多个字符串(由`Iterator`提供)按先后顺序连接在一起，两两被连接的字符串中间插入分隔串
     *
     * <pre>
     * StringKit.join(null, *)                = null
     * StringKit.join([], *)                  = ""
     * StringKit.join([null], *)              = ""
     * StringKit.join(["a", "b", "c"], "--")  = "a--b--c"
     * StringKit.join(["a", "b", "c"], null)  = "abc"
     * StringKit.join(["a", "b", "c"], "")    = "abc"
     * StringKit.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     * @param iterator  要连接的对象迭代器, 可以为null，为null将返回null, 元素为null当空串处理
     * @param separator 分隔串, null当空串处理
     * @return 连接后的字符串
     * @since 1.0.0
     */
    fun join(iterator: Iterator<*>?, separator: String?): String? = StringUtils.join(iterator, separator)

    /**
     * 将多个字符串(由`Iterable`提供)按先后顺序连接在一起，两两被连接的字符串中间插入分隔符
     *
     * <pre>
     * StringKit.join(null, *)               = null
     * StringKit.join([], *)                 = ""
     * StringKit.join([null], *)             = ""
     * StringKit.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringKit.join(["a", "b", "c"], null) = "abc"
     * StringKit.join([null, "", "a"], ';')  = ";;a"
     * </pre>
     * @param iterable  要连接的可迭代的对象, 可以为null，为null将返回null, 元素为null当空串处理
     * @param separator 分隔符
     * @return 连接后的字符串
     * @since 1.0.0
     */
    fun join(iterable: Iterable<*>?, separator: Char): String? = StringUtils.join(iterable, separator)

    /**
     * 将多个字符串(由`Iterable`提供)按先后顺序连接在一起，两两被连接的字符串中间插入分隔串
     *
     * <pre>
     * StringKit.join(null, *)                = null
     * StringKit.join([], *)                  = ""
     * StringKit.join([null], *)              = ""
     * StringKit.join(["a", "b", "c"], "--")  = "a--b--c"
     * StringKit.join(["a", "b", "c"], null)  = "abc"
     * StringKit.join(["a", "b", "c"], "")    = "abc"
     * StringKit.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     * @param iterable  要连接的可迭代的对象, 可以为null，为null将返回null, 元素为null当空串处理
     * @param separator 分隔串, null当空串处理
     * @return 连接后的字符串
     * @since 1.0.0
     */
    fun join(iterable: Iterable<*>?, separator: String?): String? = StringUtils.join(iterable, separator)
    //endregion Joining

    /**
     * 删除所有空白字符
     *
     * <pre>
     * StringKit.deleteWhitespace(null)         = null
     * StringKit.deleteWhitespace("")           = ""
     * StringKit.deleteWhitespace("abc")        = "abc"
     * StringKit.deleteWhitespace("   ab  c  ") = "abc"
     * </pre>
     * @param str 要删除空白字符的字符串, 可以为null，为null时返回null
     * @return 没有空白字符的字符串
     * @since 1.0.0
     */
    fun deleteWhitespace(str: String?): String? = StringUtils.deleteWhitespace(str)

    //region Remove
    /**
     * 如果子串在主串的开头部分，则删除该子串，否则返回源主串
     *
     * <pre>
     * StringKit.removeStart(null, *)      = null
     * StringKit.removeStart("", *)        = ""
     * StringKit.removeStart(*, null)      = *
     * StringKit.removeStart("www.domain.com", "www.")   = "domain.com"
     * StringKit.removeStart("domain.com", "www.")       = "domain.com"
     * StringKit.removeStart("www.domain.com", "domain") = "www.domain.com"
     * StringKit.removeStart("abc", "")    = "abc"
     * </pre>
     * @param str    源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @param remove 要删除的子串，可以为null，为null或为空串时返回源字符串
     * @return 去掉开头部分的子串后的字符串
     * @since 1.0.0
     */
    fun removeStart(str: String?, remove: String?): String? = StringUtils.removeStartIgnoreCase(str, remove)

    /**
     * 如果子串在主串的开头部分（大小写不敏感），则删除该子串，否则返回源主串
     *
     * <pre>
     * StringKit.removeStartIgnoreCase(null, *)      = null
     * StringKit.removeStartIgnoreCase("", *)        = ""
     * StringKit.removeStartIgnoreCase(*, null)      = *
     * StringKit.removeStartIgnoreCase("www.domain.com", "www.")   = "domain.com"
     * StringKit.removeStartIgnoreCase("www.domain.com", "WWW.")   = "domain.com"
     * StringKit.removeStartIgnoreCase("domain.com", "www.")       = "domain.com"
     * StringKit.removeStartIgnoreCase("www.domain.com", "domain") = "www.domain.com"
     * StringKit.removeStartIgnoreCase("abc", "")    = "abc"
     * </pre>
     * @param str    源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @param remove 要删除的子串，可以为null，为null或为空串时返回源字符串
     * @return 去掉开头部分的子串后的字符串
     * @since 1.0.0
     */
    fun removeStartIgnoreCase(str: String?, remove: String?): String? = StringUtils.removeStart(str, remove)

    /**
     * 如果子串在主串的末尾，则删除该子串，否则返回源主串
     *
     * <pre>
     * StringKit.removeEnd(null, *)      = null
     * StringKit.removeEnd("", *)        = ""
     * StringKit.removeEnd(*, null)      = *
     * StringKit.removeEnd("www.domain.com", ".com.")  = "www.domain.com"
     * StringKit.removeEnd("www.domain.com", ".com")   = "www.domain"
     * StringKit.removeEnd("www.domain.com", "domain") = "www.domain.com"
     * StringKit.removeEnd("abc", "")    = "abc"
     * </pre>
     * @param str    源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @param remove 要删除的子串，可以为null，为null或为空串时返回源字符串
     * @return 去掉末尾的子串后的字符串
     * @since 1.0.0
     */
    fun removeEnd(str: String?, remove: String?): String? = StringUtils.removeEnd(str, remove)

    /**
     * 如果子串在主串的末尾（大小写不敏感），则删除该子串，否则返回源主串
     *
     * <pre>
     * StringKit.removeEndIgnoreCase(null, *)      = null
     * StringKit.removeEndIgnoreCase("", *)        = ""
     * StringKit.removeEndIgnoreCase(*, null)      = *
     * StringKit.removeEndIgnoreCase("www.domain.com", ".com.")  = "www.domain.com"
     * StringKit.removeEndIgnoreCase("www.domain.com", ".com")   = "www.domain"
     * StringKit.removeEndIgnoreCase("www.domain.com", "domain") = "www.domain.com"
     * StringKit.removeEndIgnoreCase("abc", "")    = "abc"
     * StringKit.removeEndIgnoreCase("www.domain.com", ".COM") = "www.domain")
     * StringKit.removeEndIgnoreCase("www.domain.COM", ".com") = "www.domain")
     * </pre>
     * @param str    源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @param remove 要删除的子串，可以为null，为null或为空串时返回源字符串
     * @return 去掉末尾的子串后的字符串
     * @since 1.0.0
     */
    fun removeEndIgnoreCase(str: String?, remove: String?): String? = StringUtils.removeEndIgnoreCase(str, remove)

    /**
     * 删除源字符串中所有出现的子串，找不到时返回源字符串
     *
     * <pre>
     * StringKit.remove(null, *)        = null
     * StringKit.remove("", *)          = ""
     * StringKit.remove(*, null)        = *
     * StringKit.remove(*, "")          = *
     * StringKit.remove("queued", "ue") = "qd"
     * StringKit.remove("queued", "zz") = "queued"
     * </pre>
     * @param str    源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @param remove 要删除的子串，可以为null，为null或为空串时返回源字符串
     * @return 去掉所有出现的子串后的字符串
     * @since 1.0.0
     */
    fun remove(str: String?, remove: String?): String? = StringUtils.remove(str, remove)

    /**
     * 删除源字符串中所有出现的指定字符，找不到时返回源字符串
     *
     * <pre>
     * StringKit.remove(null, *)       = null
     * StringKit.remove("", *)         = ""
     * StringKit.remove("queued", 'u') = "qeed"
     * StringKit.remove("queued", 'z') = "queued"
     * </pre>
     * @param str    源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @param remove 要删除的字符
     * @return 去掉所有出现的指定字符后的字符串
     * @since 1.0.0
     */
    fun remove(str: String?, remove: Char): String? = StringUtils.remove(str, remove)
    //endregion Remove

    //region Replacing
    /**
     * 查找子串，并用指定字符串替换之, 只替换一次
     *
     * <pre>
     * StringKit.replaceOnce(null, *, *)        = null
     * StringKit.replaceOnce("", *, *)          = ""
     * StringKit.replaceOnce("any", null, *)    = "any"
     * StringKit.replaceOnce("any", *, null)    = "any"
     * StringKit.replaceOnce("any", "", *)      = "any"
     * StringKit.replaceOnce("aba", "a", null)  = "aba"
     * StringKit.replaceOnce("aba", "a", "")    = "ba"
     * StringKit.replaceOnce("aba", "a", "z")   = "zba"
     * </pre>
     * @param text         被查找和替换的源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @param searchString 要查找的字符串, 可以为null，为null或空串时返回源字符串
     * @param replacement  用来替换的字符串, 可以为null，为null时返回源字符串
     * @return 替换后的字符串
     * @see .replace
     * @since 1.0.0
     */
    fun replaceOnce(text: String?, searchString: String?, replacement: String?): String? =
        StringUtils.replaceOnce(text, searchString, replacement)

    /**
     * 查找子串，并用指定字符串替换所有匹配的地方
     *
     * <pre>
     * StringKit.replace(null, *, *)        = null
     * StringKit.replace("", *, *)          = ""
     * StringKit.replace("any", null, *)    = "any"
     * StringKit.replace("any", *, null)    = "any"
     * StringKit.replace("any", "", *)      = "any"
     * StringKit.replace("aba", "a", null)  = "aba"
     * StringKit.replace("aba", "a", "")    = "b"
     * StringKit.replace("aba", "a", "z")   = "zbz"
     * </pre>
     * @param text         被查找和替换的源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @param searchString 要查找的字符串, 可以为null，为null或空串时返回源字符串
     * @param replacement  用来替换的字符串, 可以为null，为null时返回源字符串
     * @return 替换后的字符串
     * @see .replace
     * @since 1.0.0
     */
    fun replace(text: String?, searchString: String?, replacement: String?): String? =
        StringUtils.replace(text, searchString, replacement)

    /**
     * 查找子串，并用指定字符串替换之, 限定替换次数
     *
     * <pre>
     * StringKit.replace(null, *, *, *)         = null
     * StringKit.replace("", *, *, *)           = ""
     * StringKit.replace("any", null, *, *)     = "any"
     * StringKit.replace("any", *, null, *)     = "any"
     * StringKit.replace("any", "", *, *)       = "any"
     * StringKit.replace("any", *, *, 0)        = "any"
     * StringKit.replace("abaa", "a", null, -1) = "abaa"
     * StringKit.replace("abaa", "a", "", -1)   = "b"
     * StringKit.replace("abaa", "a", "z", 0)   = "abaa"
     * StringKit.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringKit.replace("abaa", "a", "z", 2)   = "zbza"
     * StringKit.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     * @param text         被查找和替换的源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @param searchString 要查找的字符串, 可以为null，为null或空串时返回源字符串
     * @param replacement  用来替换的字符串, 可以为null，为null时返回源字符串
     * @param max          最大替换次数
     * @return 替换后的字符串
     * @since 1.0.0
     */
    fun replace(text: String?, searchString: String?, replacement: String?, max: Int): String? =
        StringUtils.replace(text, searchString, replacement, max)

    /**
     * 查找子串，并用指定字符串替换之（替换所有出现的地方），支持多对替换规则
     *
     * <pre>
     * StringKit.replaceEach(null, *, *)        = null
     * StringKit.replaceEach("", *, *)          = ""
     * StringKit.replaceEach("aba", null, null) = "aba"
     * StringKit.replaceEach("aba", new String[0], null) = "aba"
     * StringKit.replaceEach("aba", null, new String[0]) = "aba"
     * StringKit.replaceEach("aba", new String[]{"a"}, null)  = "aba"
     * StringKit.replaceEach("aba", new String[]{"a"}, new String[]{""})  = "b"
     * StringKit.replaceEach("aba", new String[]{null}, new String[]{"a"})  = "aba"
     * StringKit.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"})  = "wcte"
     * StringKit.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"})  = "dcte"
     * </pre>
     * @param text            被查找和替换的源字符串
     * @param searchList      要查找的字符串数组
     * @param replacementList 用来替换的字符串数组，与查找的数组元素一一对应。可以为null，为null时返回源字符串
     * @return 替换后的字符串
     * @throws IllegalArgumentException 如果两个数组的长度不一致时(null或空数组是允许的)
     * @since 1.0.0
     */
    fun replaceEach(text: String, searchList: Array<String?>, replacementList: Array<String>): String =
        StringUtils.replaceEach(text, searchList, replacementList)

    /**
     * 查找子串，并用指定字符串循环替换之（替换所有出现的地方），支持多对替换规则
     *
     * <pre>
     * StringKit.replaceEachRepeatedly(null, *, *) = null
     * StringKit.replaceEachRepeatedly("", *, *) = ""
     * StringKit.replaceEachRepeatedly("aba", null, null) = "aba"
     * StringKit.replaceEachRepeatedly("aba", new String[0], null) = "aba"
     * StringKit.replaceEachRepeatedly("aba", null, new String[0]) = "aba"
     * StringKit.replaceEachRepeatedly("aba", new String[]{"a"}, null, *) = "aba"
     * StringKit.replaceEachRepeatedly("aba", new String[]{"a"}, new String[]{""}) = "b"
     * StringKit.replaceEachRepeatedly("aba", new String[]{null}, new String[]{"a"}) = "aba"
     * StringKit.replaceEachRepeatedly("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}) = "wcte"
     * StringKit.replaceEachRepeatedly("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}) = "tcte"
     * StringKit.replaceEachRepeatedly("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}) = IllegalStateException
     * </pre>
     * @param text            被查找和替换的源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @param searchList      要查找的字符串数组，可以为null，为null时返回源字符串
     * @param replacementList 用来替换的字符串数组，与查找的数组元素一一对应。可以为null，为null时返回源字符串
     * @return 替换后的字符串
     * @throws IllegalStateException    死循环时
     * @throws IllegalArgumentException 如果两个数组的长度不一致时(null或空数组是允许的)
     * @since 1.0.0
     */
    fun replaceEachRepeatedly(text: String?, searchList: Array<String?>?, replacementList: Array<String?>?): String? =
        StringUtils.replaceEachRepeatedly(text, searchList, replacementList)

    /**
     * 查找所有出现指定字符的地方，并用另一个字符替换所有这些地方
     *
     * <pre>
     * StringKit.replaceChars(null, *, *)        = null
     * StringKit.replaceChars("", *, *)          = ""
     * StringKit.replaceChars("abcba", 'b', 'y') = "aycya"
     * StringKit.replaceChars("abcba", 'z', 'y') = "abcba"
     * </pre>
     * @param str         被查找和替换的源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @param searchChar  要查找的字符
     * @param replaceChar 用来替换的字符
     * @return 替换后的字符串
     * @since 1.0.0
     */
    fun replaceChars(str: String?, searchChar: Char, replaceChar: Char): String? =
        StringUtils.replaceChars(str, searchChar, replaceChar)

    /**
     * 查找所有出现指定字符的地方，并用另一个字符替换所有这些地方，支持多对替换规则
     * 正常情况下，要查找的字符数应该与用来替换的字符数相同，这样它们按顺序一一对应。 当其中一者多于另一者时，多的相当于被截短
     *
     *  <pre>
     * StringKit.replaceChars(null, *, *)           = null
     * StringKit.replaceChars("", *, *)             = ""
     * StringKit.replaceChars("abc", null, *)       = "abc"
     * StringKit.replaceChars("abc", "", *)         = "abc"
     * StringKit.replaceChars("abc", "b", null)     = "ac"
     * StringKit.replaceChars("abc", "b", "")       = "ac"
     * StringKit.replaceChars("abcba", "bc", "yz")  = "ayzya"
     * StringKit.replaceChars("abcba", "bc", "y")   = "ayya"
     * StringKit.replaceChars("abcba", "bc", "yzx") = "ayzya"
     * </pre>
     * @param str          被查找和替换的源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @param searchChars  一组要查找的字符, 可以为null，可以为null，为null时返回源字符串
     * @param replaceChars 一组用来替换的字符，可以为null，为null时当作空串
     * @return 替换后的字符串
     * @since 1.0.0
     */
    fun replaceChars(str: String?, searchChars: String?, replaceChars: String?): String? =
        StringUtils.replaceChars(str, searchChars, replaceChars)
    //endregion Replacing

    /**
     * 用一个字符串替换源字符串的一部分（通过下标指定）
     *
     * <pre>
     * StringKit.overlay(null, *, *, *)            = null
     * StringKit.overlay("", "abc", 0, 0)          = "abc"
     * StringKit.overlay("abcdef", null, 2, 4)     = "abef"
     * StringKit.overlay("abcdef", "", 2, 4)       = "abef"
     * StringKit.overlay("abcdef", "", 4, 2)       = "abef"
     * StringKit.overlay("abcdef", "zzzz", 2, 4)   = "abzzzzef"
     * StringKit.overlay("abcdef", "zzzz", 4, 2)   = "abzzzzef"
     * StringKit.overlay("abcdef", "zzzz", -1, 4)  = "zzzzef"
     * StringKit.overlay("abcdef", "zzzz", 2, 8)   = "abzzzz"
     * StringKit.overlay("abcdef", "zzzz", -2, -3) = "zzzzabcdef"
     * StringKit.overlay("abcdef", "zzzz", 8, 10)  = "abcdefzzzz"
     * </pre>
     * @param str     被查找和替换的源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @param overlay 用来替换的字符串。可以为null，为null时当作空串
     * @param start   开始替换的位置，负数当作0，大于源字符串长度当作源字符串长度
     * @param end     停止替换的位置，负数当作0，大于源字符串长度当作源字符串长度
     * @return 替换后的字符串
     * @since 1.0.0
     */
    fun overlay(str: String?, overlay: String?, start: Int, end: Int): String =
        StringUtils.overlay(str, overlay, start, end)

    /**
     * 删除字符串末尾的换行符("\n"、"\r"、"\r\n")
     *
     * <pre>
     * StringKit.chomp(null)          = null
     * StringKit.chomp("")            = ""
     * StringKit.chomp("abc \r")      = "abc "
     * StringKit.chomp("abc\n")       = "abc"
     * StringKit.chomp("abc\r\n")     = "abc"
     * StringKit.chomp("abc\r\n\r\n") = "abc\r\n"
     * StringKit.chomp("abc\n\r")     = "abc\n"
     * StringKit.chomp("abc\n\rabc")  = "abc\n\rabc"
     * StringKit.chomp("\r")          = ""
     * StringKit.chomp("\n")          = ""
     * StringKit.chomp("\r\n")        = ""
     * </pre>
     * @param str 被查找和替换的源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @return 处理后的字符串
     * @since 1.0.0
     */
    fun chomp(str: String?): String? = StringUtils.chomp(str)

    /**
     * 删除指定字符串的最后一个字符(如果该字符串是以"\r\n"结尾，同样也删除这两个字符)
     *
     * <pre>
     * StringKit.chop(null)          = null
     * StringKit.chop("")            = ""
     * StringKit.chop("abc \r")      = "abc "
     * StringKit.chop("abc\n")       = "abc"
     * StringKit.chop("abc\r\n")     = "abc"
     * StringKit.chop("abc")         = "ab"
     * StringKit.chop("abc\nabc")    = "abc\nab"
     * StringKit.chop("a")           = ""
     * StringKit.chop("\r")          = ""
     * StringKit.chop("\n")          = ""
     * StringKit.chop("\r\n")        = ""
     * </pre>
     * @param str 被查找和替换的源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @return 处理后的字符串
     * @since 1.0.0
     */
    fun chop(str: String?): String? = StringUtils.chop(str)

    /**
     * 字符串自相连指定次数
     *
     * <pre>
     * StringKit.repeat(null, 2) = null
     * StringKit.repeat("", 0)   = ""
     * StringKit.repeat("", 2)   = ""
     * StringKit.repeat("a", 3)  = "aaa"
     * StringKit.repeat("ab", 2) = "abab"
     * StringKit.repeat("a", -2) = ""
     * </pre>
     * @param str    源字符串, 可以为null，为null时返回null，为空串时返回空串
     * @param repeat 重复的次数，负数当作0
     * @return 自相连后的字符串
     * @since 1.0.0
     */
    fun repeat(str: String?, repeat: Int): String? = StringUtils.repeat(str, repeat)

    /**
     * 字符串自相连指定次数,两两间用分隔串分隔
     *
     * <pre>
     * StringKit.repeat(null, null, 2) = null
     * StringKit.repeat(null, "x", 2)  = null
     * StringKit.repeat("", null, 0)   = ""
     * StringKit.repeat("", "", 2)     = ""
     * StringKit.repeat("", "x", 3)    = "xxx"
     * StringKit.repeat("?", ", ", 3)  = "?, ?, ?"
     * </pre>
     * @param str       源字符串, 可以为null，为null时返回null
     * @param separator 分隔串, 可以为null，为null时当作空串
     * @param repeat    重复的次数，负数当作0
     * @return 自相连后的字符串
     * @since 1.0.0
     */
    fun repeat(str: String?, separator: String?, repeat: Int): String? = StringUtils.repeat(str, separator, repeat)

    /**
     * 字符自相连指定次数
     *
     *
     * <pre>
     * StringKit.repeat('e', 0)  = ""
     * StringKit.repeat('e', 3)  = "eee"
     * StringKit.repeat('e', -2) = ""
     * </pre>
     * @param ch     重复的字符
     * @param repeat 重复的次数，负数当作0
     * @return 重复相连后的字符串
     * @see .repeat
     * @since 1.0.0
     */
    fun repeat(ch: Char, repeat: Int): String? {
        return StringUtils.repeat(ch, repeat)
    }

    //region pad
    /**
     * 用空格右补全源字符串到指定的长度
     *
     * <pre>
     * StringKit.rightPad(null, *)   = null
     * StringKit.rightPad("", 3)     = "   "
     * StringKit.rightPad("bat", 3)  = "bat"
     * StringKit.rightPad("bat", 5)  = "bat  "
     * StringKit.rightPad("bat", 1)  = "bat"
     * StringKit.rightPad("bat", -1) = "bat"
     * </pre>
     * @param str  源字符串, 可以为null，为null时返回null
     * @param size 要求的长度，负数当作0
     * @return 补全长度后的字符串
     * @since 1.0.0
     */
    fun rightPad(str: String?, size: Int): String? = StringUtils.rightPad(str, size)

    /**
     * 用指定字符右补全源字符串到指定的长度
     *
     * <pre>
     * StringKit.rightPad(null, *, *)     = null
     * StringKit.rightPad("", 3, 'z')     = "zzz"
     * StringKit.rightPad("bat", 3, 'z')  = "bat"
     * StringKit.rightPad("bat", 5, 'z')  = "batzz"
     * StringKit.rightPad("bat", 1, 'z')  = "bat"
     * StringKit.rightPad("bat", -1, 'z') = "bat"
     * </pre>
     * @param str     源字符串, 可以为null，为null时返回null
     * @param size    要求的长度，负数当作0
     * @param padChar 补全的字符
     * @return 补全长度后的字符串
     * @since 1.0.0
     */
    fun rightPad(str: String?, size: Int, padChar: Char): String? = StringUtils.rightPad(str, size, padChar)

    /**
     * 用指定字符串右补全源字符串到指定的长度
     *
     * <pre>
     * StringKit.rightPad(null, *, *)      = null
     * StringKit.rightPad("", 3, "z")      = "zzz"
     * StringKit.rightPad("bat", 3, "yz")  = "bat"
     * StringKit.rightPad("bat", 5, "yz")  = "batyz"
     * StringKit.rightPad("bat", 8, "yz")  = "batyzyzy"
     * StringKit.rightPad("bat", 1, "yz")  = "bat"
     * StringKit.rightPad("bat", -1, "yz") = "bat"
     * StringKit.rightPad("bat", 5, null)  = "bat  "
     * StringKit.rightPad("bat", 5, "")    = "bat  "
     * </pre>
     * @param str    源字符串, 可以为null，为null时返回null
     * @param size   要求的长度，负数当作0
     * @param padStr 补全的字符串, null或空串当作空格
     * @return 补全长度后的字符串
     * @since 1.0.0
     */
    fun rightPad(str: String?, size: Int, padStr: String?): String? = StringUtils.rightPad(str, size, padStr)

    /**
     * 用空格左补全源字符串到指定的长度
     *
     * <pre>
     * StringKit.leftPad(null, *)   = null
     * StringKit.leftPad("", 3)     = "   "
     * StringKit.leftPad("bat", 3)  = "bat"
     * StringKit.leftPad("bat", 5)  = "  bat"
     * StringKit.leftPad("bat", 1)  = "bat"
     * StringKit.leftPad("bat", -1) = "bat"
     * </pre>
     * @param str  源字符串, 可以为null，为null时返回null
     * @param size 要求的长度，负数当作0
     * @return 补全长度后的字符串
     * @since 1.0.0
     */
    fun leftPad(str: String?, size: Int): String? = StringUtils.left(str, size)

    /**
     * 用指定字符左补全源字符串到指定的长度
     *
     * <pre>
     * StringKit.leftPad(null, *, *)     = null
     * StringKit.leftPad("", 3, 'z')     = "zzz"
     * StringKit.leftPad("bat", 3, 'z')  = "bat"
     * StringKit.leftPad("bat", 5, 'z')  = "zzbat"
     * StringKit.leftPad("bat", 1, 'z')  = "bat"
     * StringKit.leftPad("bat", -1, 'z') = "bat"
     * </pre>
     * @param str     源字符串, 可以为null，为null时返回null
     * @param size    要求的长度，负数当作0
     * @param padChar 补全的字符
     * @return 补全长度后的字符串
     * @since 1.0.0
     */
    fun leftPad(str: String?, size: Int, padChar: Char): String? = StringUtils.leftPad(str, size, padChar)

    /**
     * 用指定字符串左补全源字符串到指定的长度
     *
     * <pre>
     * StringKit.leftPad(null, *, *)      = null
     * StringKit.leftPad("", 3, "z")      = "zzz"
     * StringKit.leftPad("bat", 3, "yz")  = "bat"
     * StringKit.leftPad("bat", 5, "yz")  = "yzbat"
     * StringKit.leftPad("bat", 8, "yz")  = "yzyzybat"
     * StringKit.leftPad("bat", 1, "yz")  = "bat"
     * StringKit.leftPad("bat", -1, "yz") = "bat"
     * StringKit.leftPad("bat", 5, null)  = "  bat"
     * StringKit.leftPad("bat", 5, "")    = "  bat"
     * </pre>
     * @param str    源字符串, 可以为null，为null时返回null
     * @param size   要求的长度，负数当作0
     * @param padStr 补全的字符串, null或空串当作空格
     * @return 补全长度后的字符串
     * @since 1.0.0
     */
    fun leftPad(str: String?, size: Int, padStr: String?): String? = StringUtils.leftPad(str, size, padStr)
    //endregion pad

    /**
     * 取得字符序列的长度，null返回0
     *
     * @param cs 字符序列，可以为null，null返回0
     * @return 字符序列的长度
     * @since 1.0.0
     */
    fun length(cs: CharSequence?): Int = StringUtils.length(cs)

    //region Centering
    /**
     * 用空格左右补全源字符串到指定长度
     * 等同于`center(str, size, " ")`.
     *
     * <pre>
     * StringKit.center(null, *)   = null
     * StringKit.center("", 4)     = "    "
     * StringKit.center("ab", -1)  = "ab"
     * StringKit.center("ab", 4)   = " ab "
     * StringKit.center("abcd", 2) = "abcd"
     * StringKit.center("a", 4)    = " a  "
     * </pre>
     * @param str  源字符串, 可以为null，为null时返回null
     * @param size 要求的长度, 负数当作0，小于源字符串长度将返回源字符串
     * @return 补全长度后的字符串
     * @since 1.0.0
     */
    fun center(str: String?, size: Int): String? = StringUtils.center(str, size)

    /**
     * 用指定字符左右补全源字符串到指定长度
     *
     * <pre>
     * StringKit.center(null, *, *)     = null
     * StringKit.center("", 4, ' ')     = "    "
     * StringKit.center("ab", -1, ' ')  = "ab"
     * StringKit.center("ab", 4, ' ')   = " ab"
     * StringKit.center("abcd", 2, ' ') = "abcd"
     * StringKit.center("a", 4, ' ')    = " a  "
     * StringKit.center("a", 4, 'y')    = "yayy"
     * </pre>
     * @param str     源字符串, 可以为null，为null时返回null
     * @param size    要求的长度, 负数当作0，小于源字符串长度将返回源字符串
     * @param padChar 用于补全的字符
     * @return 补全长度后的字符串
     * @since 1.0.0
     */
    fun center(str: String?, size: Int, padChar: Char): String? = StringUtils.center(str, size, padChar)

    /**
     * 用指定字符串左右补全源字符串到指定长度
     *
     * <pre>
     * StringKit.center(null, *, *)     = null
     * StringKit.center("", 4, " ")     = "    "
     * StringKit.center("ab", -1, " ")  = "ab"
     * StringKit.center("ab", 4, " ")   = " ab"
     * StringKit.center("abcd", 2, " ") = "abcd"
     * StringKit.center("a", 4, " ")    = " a  "
     * StringKit.center("a", 4, "yz")   = "yayz"
     * StringKit.center("abc", 7, null) = "  abc  "
     * StringKit.center("abc", 7, "")   = "  abc  "
     * </pre>
     * @param str    源字符串, 可以为null，为null时返回null
     * @param size   要求的长度, 负数当作0，小于源字符串长度将返回源字符串
     * @param padStr 用于补全的字符串
     * @return 补全长度后的字符串
     * @since 1.0.0
     */
    fun center(str: String?, size: Int, padStr: String?): String? = StringUtils.center(str, size, padStr)
    //endregion Centering

    //region Case conversion
    /**
     * 将源字符串全部转成大写
     *
     * <pre>
     * StringKit.upperCase(null)  = null
     * StringKit.upperCase("")    = ""
     * StringKit.upperCase("aBc") = "ABC"
     * </pre>
     *
     * **注意:** 根据jdk文档对[String.toUpperCase]的描述,
     * 转换的结果取决于当前的locale. 为了获得平台独立的转换, 请使用[.upperCase]
     * 方法(如[Locale.ENGLISH]).
     *
     * @param str 源字符串, 可以为null，为null时返回null
     * @return 转换后的字符串
     * @since 1.0.0
     */
    fun upperCase(str: String?): String? = StringUtils.upperCase(str)

    /**
     * 根据指定的locale，将源字符串全部转成大写
     *
     * <pre>
     * StringKit.upperCase(null, Locale.ENGLISH)  = null
     * StringKit.upperCase("", Locale.ENGLISH)    = ""
     * StringKit.upperCase("aBc", Locale.ENGLISH) = "ABC"
     * </pre>
     * @param str    源字符串, 可以为null，为null时返回null
     * @param locale 定义大小写转换规则的locale，不能为null
     * @return 转换后的字符串
     * @since 1.0.0
     */
    fun upperCase(str: String?, locale: Locale?): String? = StringUtils.upperCase(str, locale)

    /**
     * 将源字符串全部转成小写
     *
     * <pre>
     * StringKit.lowerCase(null)  = null
     * StringKit.lowerCase("")    = ""
     * StringKit.lowerCase("aBc") = "abc"
     * </pre>
     *
     * **注意:** 根据jdk文档对[String.toLowerCase]的描述,
     * 转换的结果取决于当前的locale. 为了获得平台独立的转换, 请使用[.lowerCase]
     * 方法(如[Locale.ENGLISH]).
     *
     * @param str 源字符串, 可以为null，为null时返回null
     * @return 转换后的字符串
     * @since 1.0.0
     */
    fun lowerCase(str: String?): String? = StringUtils.lowerCase(str)

    /**
     * 根据指定的locale，将源字符串全部转成小写
     *
     * <pre>
     * StringKit.lowerCase(null, Locale.ENGLISH)  = null
     * StringKit.lowerCase("", Locale.ENGLISH)    = ""
     * StringKit.lowerCase("aBc", Locale.ENGLISH) = "abc"
     * </pre>
     * @param str    源字符串, 可以为null，为null时返回null
     * @param locale 定义大小写转换规则的locale，不能为null
     * @return 转换后的字符串
     * @since 1.0.0
     */
    fun lowerCase(str: String?, locale: Locale?): String? = StringUtils.lowerCase(str, locale)

    /**
     * 将源字符串首字母大写
     *
     * <pre>
     * StringKit.capitalize(null)  = null
     * StringKit.capitalize("")    = ""
     * StringKit.capitalize("cat") = "Cat"
     * StringKit.capitalize("cAt") = "CAt"
     * </pre>
     * @param str 源字符串, 可以为null，为null时返回null
     * @return 首字母大写的字符串
     * @see org.apache.commons.lang3.text.WordUtils.capitalize
     * @see .uncapitalize
     * @since 1.0.0
     */
    fun capitalize(str: String?): String? = StringUtils.capitalize(str)

    /**
     * 将源字符串首字母小写
     *
     * <pre>
     * StringKit.uncapitalize(null)  = null
     * StringKit.uncapitalize("")    = ""
     * StringKit.uncapitalize("Cat") = "cat"
     * StringKit.uncapitalize("CAT") = "cAT"
     * </pre>
     * @param str 源字符串, 可以为null，为null时返回null
     * @return 首字母小写的字符串
     * @see org.apache.commons.lang3.text.WordUtils.uncapitalize
     * @see .capitalize
     * @since 1.0.0
     */
    fun uncapitalize(str: String?): String? = StringUtils.uncapitalize(str)

    /**
     * 将源字符串中的大写转成小写，小写转成大写
     *
     * <pre>
     * StringKit.swapCase(null)                 = null
     * StringKit.swapCase("")                   = ""
     * StringKit.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
     * </pre>
     * @param str 源字符串, 可以为null，为null时返回null
     * @return 转换后的字符串
     * @since 1.0.0
     */
    fun swapCase(str: String?): String? = StringUtils.swapCase(str)
    //endregion Case conversion

    /**
     * 计算子串在源字符串中出现的次数
     *
     * <pre>
     * StringKit.countMatches(null, *)       = 0
     * StringKit.countMatches("", *)         = 0
     * StringKit.countMatches("abba", null)  = 0
     * StringKit.countMatches("abba", "")    = 0
     * StringKit.countMatches("abba", "a")   = 2
     * StringKit.countMatches("abba", "ab")  = 1
     * StringKit.countMatches("abba", "xxx") = 0
     * </pre>
     * @param str 待查找的字符序列，可以为null，为null或空串返回0
     * @param sub 子串, 可以为null
     * @return 子串出现的次数
     * @since 1.0.0
     */
    fun countMatches(str: CharSequence?, sub: CharSequence?): Int = StringUtils.countMatches(str, sub)

    //region Character Tests
    /**
     * 测试字符序列是否只包含Unicode字母
     *
     * <pre>
     * StringKit.isAlpha(null)   = false
     * StringKit.isAlpha("")     = false
     * StringKit.isAlpha("  ")   = false
     * StringKit.isAlpha("abc")  = true
     * StringKit.isAlpha("ab2c") = false
     * StringKit.isAlpha("ab-c") = false
     * </pre>
     * @param cs 待查找的字符序列，可以为null，null或空串返回false
     * @return true: 只包含Unicode字母
     * @since 1.0.0
     */
    fun isAlpha(cs: CharSequence?): Boolean = StringUtils.isAlpha(cs)

    /**
     * 测试字符序列是否只包含Unicode字母或空格
     *
     * <pre>
     * StringKit.isAlphaSpace(null)   = false
     * StringKit.isAlphaSpace("")     = true
     * StringKit.isAlphaSpace("  ")   = true
     * StringKit.isAlphaSpace("abc")  = true
     * StringKit.isAlphaSpace("ab c") = true
     * StringKit.isAlphaSpace("ab2c") = false
     * StringKit.isAlphaSpace("ab-c") = false
     * </pre>
     * @param cs 待查找的字符序列，可以为null，null返回false，空串返回true
     * @return true: 非null并且只包含Unicode字母或空格
     * @since 1.0.0
     */
    fun isAlphaSpace(cs: CharSequence?): Boolean = StringUtils.isAlphaSpace(cs)

    /**
     * 测试字符序列是否只包含Unicode字母或数字
     *
     * <pre>
     * StringKit.isAlphanumeric(null)   = false
     * StringKit.isAlphanumeric("")     = false
     * StringKit.isAlphanumeric("  ")   = false
     * StringKit.isAlphanumeric("abc")  = true
     * StringKit.isAlphanumeric("ab c") = false
     * StringKit.isAlphanumeric("ab2c") = true
     * StringKit.isAlphanumeric("ab-c") = false
     * </pre>
     * @param cs 待查找的字符序列，可以为null，null或空串返回false
     * @return true: 只包含Unicode字母或数字
     * @since 1.0.0
     */
    fun isAlphanumeric(cs: CharSequence?): Boolean = StringUtils.isAlpha(cs)

    /**
     * 测试字符序列是否只包含Unicode字母、空格或数字
     *
     * <pre>
     * StringKit.isAlphanumericSpace(null)   = false
     * StringKit.isAlphanumericSpace("")     = true
     * StringKit.isAlphanumericSpace("  ")   = true
     * StringKit.isAlphanumericSpace("abc")  = true
     * StringKit.isAlphanumericSpace("ab c") = true
     * StringKit.isAlphanumericSpace("ab2c") = true
     * StringKit.isAlphanumericSpace("ab-c") = false
     * </pre>
     * @param cs 待查找的字符序列，可以为null，null返回false，空串返回true
     * @return true: 非null并且只包含Unicode字母、空格或数字
     * @since 1.0.0
     */
    fun isAlphanumericSpace(cs: CharSequence?): Boolean = StringUtils.isAlphanumeric(cs)

    /**
     * 测试字符序列是否只包含ASCII码的可打印的字符
     *
     * <pre>
     * StringKit.isAsciiPrintable(null)     = false
     * StringKit.isAsciiPrintable("")       = true
     * StringKit.isAsciiPrintable(" ")      = true
     * StringKit.isAsciiPrintable("Ceki")   = true
     * StringKit.isAsciiPrintable("ab2c")   = true
     * StringKit.isAsciiPrintable("!ab-c~") = true
     * StringKit.isAsciiPrintable("\u0020") = true
     * StringKit.isAsciiPrintable("\u0021") = true
     * StringKit.isAsciiPrintable("\u007e") = true
     * StringKit.isAsciiPrintable("\u007f") = false
     * StringKit.isAsciiPrintable("Ceki G\u00fclc\u00fc") = false
     * </pre>
     * @param cs 待查找的字符序列，可以为null，null返回false，空串返回true
     * @return true: 非null并且每个字符都在32到126的范围内
     * @since 1.0.0
     */
    fun isAsciiPrintable(cs: CharSequence?): Boolean = StringUtils.isAsciiPrintable(cs)

    /**
     * 测试字符序列是否只包含Unicode数字。 十进制的小数不是Unicode数字。
     *
     * <pre>
     * StringKit.isNumeric(null)   = false
     * StringKit.isNumeric("")     = false
     * StringKit.isNumeric("  ")   = false
     * StringKit.isNumeric("123")  = true
     * StringKit.isNumeric("12 3") = false
     * StringKit.isNumeric("ab2c") = false
     * StringKit.isNumeric("12-3") = false
     * StringKit.isNumeric("12.3") = false
     * </pre>
     * @param cs 待查找的字符序列，可以为null，null或空串返回false
     * @return true: 只包含Unicode数字
     * @since 1.0.0
     */
    fun isNumeric(cs: CharSequence?): Boolean = StringUtils.isNumeric(cs)

    /**
     * 测试字符序列是否只包含Unicode数字或空格。 十进制的小数不是Unicode数字。
     *
     * <pre>
     * StringKit.isNumericSpace(null)   = false
     * StringKit.isNumericSpace("")     = true
     * StringKit.isNumericSpace("  ")   = true
     * StringKit.isNumericSpace("123")  = true
     * StringKit.isNumericSpace("12 3") = true
     * StringKit.isNumericSpace("ab2c") = false
     * StringKit.isNumericSpace("12-3") = false
     * StringKit.isNumericSpace("12.3") = false
     * </pre>
     * @param cs 待查找的字符序列，可以为null，null返回false，空串返回true
     * @return true: 只包含Unicode数字或空格
     * @since 1.0.0
     */
    fun isNumericSpace(cs: CharSequence?): Boolean = StringUtils.isNumeric(cs)

    /**
     * 测试字符序列是否只包含空白字符
     *
     * <pre>
     * StringKit.isWhitespace(null)   = false
     * StringKit.isWhitespace("")     = true
     * StringKit.isWhitespace("  ")   = true
     * StringKit.isWhitespace("abc")  = false
     * StringKit.isWhitespace("ab2c") = false
     * StringKit.isWhitespace("ab-c") = false
     * </pre>
     * @param cs 待查找的字符序列，可以为null，null返回false，空串返回true
     * @return true：只包含空白字符
     * @since 1.0.0
     */
    fun isWhitespace(cs: CharSequence?): Boolean = StringUtils.isWhitespace(cs)

    /**
     * 测试字符序列是否只包含小写字母
     *
     * <pre>
     * StringKit.isAllLowerCase(null)   = false
     * StringKit.isAllLowerCase("")     = false
     * StringKit.isAllLowerCase("  ")   = false
     * StringKit.isAllLowerCase("abc")  = true
     * StringKit.isAllLowerCase("abC") = false
     * </pre>
     * @param cs 待查找的字符序列，可以为null，null或空串返回false
     * @return true：只包含小写字母
     * @since 1.0.0
     */
    fun isAllLowerCase(cs: CharSequence?): Boolean = StringUtils.isAllLowerCase(cs)

    /**
     * 测试字符序列是否只包含大写字母
     *
     * <pre>
     * StringKit.isAllUpperCase(null)   = false
     * StringKit.isAllUpperCase("")     = false
     * StringKit.isAllUpperCase("  ")   = false
     * StringKit.isAllUpperCase("ABC")  = true
     * StringKit.isAllUpperCase("aBC") = false
     * </pre>
     * @param cs 待查找的字符序列，可以为null，null或空串返回false
     * @return true：只包含大写字母
     * @since 1.0.0
     */
    fun isAllUpperCase(cs: CharSequence?): Boolean = StringUtils.isAllUpperCase(cs)
    //endregion Character Tests

    //region Defaults
    /**
     * 返回传入的字符串或null或空串
     *
     * <pre>
     * StringKit.defaultString(null)  = ""
     * StringKit.defaultString("")    = ""
     * StringKit.defaultString("bat") = "bat"
     * </pre>
     * @param str 待检查的字符串, 可以为null，null返回null，空串返回空串
     * @return 传入的字符串或null或空串
     * @see ObjectUtils.toString
     * @see String.valueOf
     * @since 1.0.0
     */
    fun defaultString(str: String?): String? {
        return StringUtils.defaultString(str)
    }

    /**
     * 返回传入的字符串，如果它为null时，则返回默认的值
     *
     * <pre>
     * StringKit.defaultString(null, "NULL")  = "NULL"
     * StringKit.defaultString("", "NULL")    = ""
     * StringKit.defaultString("bat", "NULL") = "bat"
     * </pre>
     * @param str        待检查的字符串, 可以为null，null返回默认值，空串返回空串
     * @param defaultStr 默认值, 可以为null
     * @return 传入的字符串，如果它为null时，则返回默认的值
     * @see ObjectUtils.toString
     * @see String.valueOf
     * @since 1.0.0
     */
    fun defaultString(str: String?, defaultStr: String?): String? = StringUtils.defaultString(str, defaultStr)

    /**
     * 返回传入的字符串，如果它为null或空串或空白字符时，则返回默认的值
     *
     * <pre>
     * StringKit.defaultIfBlank(null, "NULL")  = "NULL"
     * StringKit.defaultIfBlank("", "NULL")    = "NULL"
     * StringKit.defaultIfBlank(" ", "NULL")   = "NULL"
     * StringKit.defaultIfBlank("bat", "NULL") = "bat"
     * StringKit.defaultIfBlank("", null)      = null
     * </pre>
     * @param <T>        CharSequence的类型
     * @param str        待查找的字符序列，可以为null
     * @param defaultStr 默认值, 可以为null
     * @return 传入的字符串或默认值
     * @see StringKit.defaultString
     * @since 1.0.0
    </T> */
    fun <T : CharSequence?> defaultIfBlank(str: T, defaultStr: T): T = StringUtils.defaultIfBlank(str, defaultStr)

    /**
     * 返回传入的字符串，如果它为null或空串时，则返回默认的值
     *
     * <pre>
     * StringKit.defaultIfEmpty(null, "NULL")  = "NULL"
     * StringKit.defaultIfEmpty("", "NULL")    = "NULL"
     * StringKit.defaultIfEmpty(" ", "NULL")   = " "
     * StringKit.defaultIfEmpty("bat", "NULL") = "bat"
     * StringKit.defaultIfEmpty("", null)      = null
     * </pre>
     * @param <T>        CharSequence的类型
     * @param str        待查找的字符序列，可以为null
     * @param defaultStr 默认值, 可以为null
     * @return 传入的字符串或默认值
     * @see StringKit.defaultString
     * @since 1.0.0
    </T> */
    fun <T : CharSequence?> defaultIfEmpty(str: T, defaultStr: T): T = StringUtils.defaultIfEmpty(str, defaultStr)
    //endregion Defaults

    //region Reversing
    /**
     * 反转字符串
     *
     * <pre>
     * StringKit.reverse(null)  = null
     * StringKit.reverse("")    = ""
     * StringKit.reverse("bat") = "tab"
     * StringKit.reverse("反转字符串") = "串符字转反"
     * </pre>
     * @param str 源字符串, 可以为null，null返回null
     * @return 反转后的字符串
     * @since 1.0.0
     */
    fun reverse(str: String?): String? = StringUtils.reverse(str)

    /**
     * 根据分隔符反转字符串，分隔符间的字符串当作一个整体(本身不反转)
     *
     * <pre>
     * StringKit.reverseDelimited(null, *)      = null
     * StringKit.reverseDelimited("", *)        = ""
     * StringKit.reverseDelimited("a.b.c", 'x') = "a.b.c"
     * StringKit.reverseDelimited("a.b.c", ".") = "c.b.a"
     * StringKit.reverseDelimited("java.lang.String", ".") = "String.lang.java"
     * </pre>
     * @param str           源字符串, 可以为null，null返回null
     * @param separatorChar 分隔符
     * @return 反转后的字符串
     * @since 1.0.0
     */
    fun reverseDelimited(str: String?, separatorChar: Char): String? = StringUtils.reverseDelimited(str, separatorChar)
    //endregion Reversing

    //region Abbreviating
    /**
     * 省略字符串
     * 规则:
     *
     *  * 如果 `str` 的长度 比 `maxWidth` 还小, return `str`
     *  * 否则省略为： `(substring(str, 0, max-3) + "...")`.
     *  * 如果 `maxWidth` 比 `4` 小, 抛出
     * `IllegalArgumentException` 异常.
     *  * 永远不会返回长度超过 `maxWidth` 的字符串.
     *
     * <pre>
     * StringKit.abbreviate(null, *)      = null
     * StringKit.abbreviate("", 4)        = ""
     * StringKit.abbreviate("abcdefg", 6) = "abc..."
     * StringKit.abbreviate("abcdefg", 7) = "abcdefg"
     * StringKit.abbreviate("abcdefg", 8) = "abcdefg"
     * StringKit.abbreviate("abcdefg", 4) = "a..."
     * StringKit.abbreviate("abcdefg", 3) = IllegalArgumentException
     * </pre>
     *
     * @param str      源字符串, 可以为null，为null时返回null
     * @param maxWidth 返回的字符串的最大长度，必须大于等于4
     * @return 省略的字符串
     * @since 1.0.0
     */
    fun abbreviate(str: String?, maxWidth: Int): String? = StringUtils.abbreviate(str, maxWidth)

    /**
     * 省略字符串，功能类似`abbreviate(String, int)`，但可以指定左边界偏移量
     * 左边界偏移量不一定要求最左边的字符出现在结果字符串的最左边， 或紧跟省略号之后，但它一定会出现在结果字符串的某个地方
     *
     * <pre>
     * StringKit.abbreviate(null, *, *)                = null
     * StringKit.abbreviate("", 0, 4)                  = ""
     * StringKit.abbreviate("abcdefghijklmno", -1, 10) = "abcdefg..."
     * StringKit.abbreviate("abcdefghijklmno", 0, 10)  = "abcdefg..."
     * StringKit.abbreviate("abcdefghijklmno", 1, 10)  = "abcdefg..."
     * StringKit.abbreviate("abcdefghijklmno", 4, 10)  = "abcdefg..."
     * StringKit.abbreviate("abcdefghijklmno", 5, 10)  = "...fghi..."
     * StringKit.abbreviate("abcdefghijklmno", 6, 10)  = "...ghij..."
     * StringKit.abbreviate("abcdefghijklmno", 8, 10)  = "...ijklmno"
     * StringKit.abbreviate("abcdefghijklmno", 10, 10) = "...ijklmno"
     * StringKit.abbreviate("abcdefghijklmno", 12, 10) = "...ijklmno"
     * StringKit.abbreviate("abcdefghij", 0, 3)        = IllegalArgumentException
     * StringKit.abbreviate("abcdefghij", 5, 6)        = IllegalArgumentException
     * </pre>
     *
     * @param str      源字符串, 可以为null，为null时返回null
     * @param offset   源字符串的左边界偏移量
     * @param maxWidth 返回的字符串的最大长度，必须大于等于4
     * @return 省略的字符串
     * @throws IllegalArgumentException 结果长度小于4时
     * @since 1.0.0
     */
    fun abbreviate(str: String?, offset: Int, maxWidth: Int): String? = StringUtils.abbreviate(str, offset, maxWidth)

    /**
     * 用指定字符串替换源字符串中间的字符，以达到省略源字符串到指定长度的目的
     * 省略只有当以下条件满足时才发生：
     *  源字符串和用来替换的字符串两者都不为null或空串
     *  指定的目标长度小于源字符串的长度
     *  指定的目标长度大于0
     *  被省略的字符串要有足够的长度提供给替换字符串和第一个、最后一个字符
     * 否则，结果将是传入的源字符串
     *
     * <pre>
     * StringKit.abbreviateMiddle(null, null, 0)      = null
     * StringKit.abbreviateMiddle("abc", null, 0)      = "abc"
     * StringKit.abbreviateMiddle("abc", ".", 0)      = "abc"
     * StringKit.abbreviateMiddle("abc", ".", 3)      = "abc"
     * StringKit.abbreviateMiddle("abcdef", ".", 4)     = "ab.f"
     * </pre>
     *
     * @param str    源字符串, 可以为null，为null时返回null
     * @param middle 用来替换中间字符的字符串, 可以为null，为null表示不替换
     * @param length 返回的字符串的最大长度
     * @return 省略的字符串
     * @since 1.0.0
     */
    fun abbreviateMiddle(str: String?, middle: String?, length: Int): String? =
        StringUtils.abbreviateMiddle(str, middle, length)
    //endregion Abbreviating

    //region Difference
    /**
     * 比较两字符串，并返回它们不同的部分 (更精确的讲，返回第二个字符串从与第一个不同部分开始的剩下部分)
     *
     * <pre>
     * StringKit.difference(null, null) = null
     * StringKit.difference("", "") = ""
     * StringKit.difference("", "abc") = "abc"
     * StringKit.difference("abc", "") = ""
     * StringKit.difference("abc", "abc") = ""
     * StringKit.difference("ab", "abxyz") = "xyz"
     * StringKit.difference("abcde", "abxyz") = "xyz"
     * StringKit.difference("abcde", "xyz") = "xyz"
     * </pre>
     * @param str1 第一个字符串, 可以为null
     * @param str2 第一个字符串, 可以为null
     * @return 两字符串不同的部分，相同时返回空串，两个都为null时返回null
     * @since 1.0.0
     */
    fun difference(str1: String?, str2: String?): String? = StringUtils.difference(str1, str2)

    /**
     * 比较两字符串，并返回它们开始不同时的下标
     *
     * <pre>
     * StringKit.indexOfDifference(null, null) = -1
     * StringKit.indexOfDifference("", "") = -1
     * StringKit.indexOfDifference("", "abc") = 0
     * StringKit.indexOfDifference("abc", "") = 0
     * StringKit.indexOfDifference("abc", "abc") = -1
     * StringKit.indexOfDifference("ab", "abxyz") = 2
     * StringKit.indexOfDifference("abcde", "abxyz") = 2
     * StringKit.indexOfDifference("abcde", "xyz") = 0
     * </pre>
     * @param cs1 第一个字符串, 可以为null
     * @param cs2 第一个字符串, 可以为null
     * @return 开始不同时的下标; 如果两字符串相同返回-1
     * @since 1.0.0
     */
    fun indexOfDifference(cs1: CharSequence?, cs2: CharSequence?): Int = StringUtils.indexOfDifference(cs1, cs2)

    /**
     * 比较数组中的每个字符串，并返回它们开始不同时的下标
     *
     * <pre>
     * StringKit.indexOfDifference(null) = -1
     * StringKit.indexOfDifference(new String[] {}) = -1
     * StringKit.indexOfDifference(new String[] {"abc"}) = -1
     * StringKit.indexOfDifference(new String[] {null, null}) = -1
     * StringKit.indexOfDifference(new String[] {"", ""}) = -1
     * StringKit.indexOfDifference(new String[] {"", null}) = 0
     * StringKit.indexOfDifference(new String[] {"abc", null, null}) = 0
     * StringKit.indexOfDifference(new String[] {null, null, "abc"}) = 0
     * StringKit.indexOfDifference(new String[] {"", "abc"}) = 0
     * StringKit.indexOfDifference(new String[] {"abc", ""}) = 0
     * StringKit.indexOfDifference(new String[] {"abc", "abc"}) = -1
     * StringKit.indexOfDifference(new String[] {"abc", "a"}) = 1
     * StringKit.indexOfDifference(new String[] {"ab", "abxyz"}) = 2
     * StringKit.indexOfDifference(new String[] {"abcde", "abxyz"}) = 2
     * StringKit.indexOfDifference(new String[] {"abcde", "xyz"}) = 0
     * StringKit.indexOfDifference(new String[] {"xyz", "abcde"}) = 0
     * StringKit.indexOfDifference(new String[] {"i am a machine", "i am a robot"}) = 7
     * </pre>
     * @param css 字符串数组，可以为null，为null返回-1
     * @return 开始不同时的下标; 如果字符串都相同返回-1
     * @since 1.0.0
     */
    fun indexOfDifference(vararg css: CharSequence?): Int = StringUtils.indexOfDifference(*css)
    //endregion Difference

    /**
     * 比较数组中的每个字符串，并返回它们相同的前缀
     *
     * <pre>
     * StringKit.getCommonPrefix(null) = ""
     * StringKit.getCommonPrefix(new String[] {}) = ""
     * StringKit.getCommonPrefix(new String[] {"abc"}) = "abc"
     * StringKit.getCommonPrefix(new String[] {null, null}) = ""
     * StringKit.getCommonPrefix(new String[] {"", ""}) = ""
     * StringKit.getCommonPrefix(new String[] {"", null}) = ""
     * StringKit.getCommonPrefix(new String[] {"abc", null, null}) = ""
     * StringKit.getCommonPrefix(new String[] {null, null, "abc"}) = ""
     * StringKit.getCommonPrefix(new String[] {"", "abc"}) = ""
     * StringKit.getCommonPrefix(new String[] {"abc", ""}) = ""
     * StringKit.getCommonPrefix(new String[] {"abc", "abc"}) = "abc"
     * StringKit.getCommonPrefix(new String[] {"abc", "a"}) = "a"
     * StringKit.getCommonPrefix(new String[] {"ab", "abxyz"}) = "ab"
     * StringKit.getCommonPrefix(new String[] {"abcde", "abxyz"}) = "ab"
     * StringKit.getCommonPrefix(new String[] {"abcde", "xyz"}) = ""
     * StringKit.getCommonPrefix(new String[] {"xyz", "abcde"}) = ""
     * StringKit.getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) = "i am a "
     * </pre>
     * @param strs 字符串数组，可以为null，为null或元素都为null或没有相同前缀时返回空串，
     * @return 相同的前缀
     * @since 1.0.0
     */
    fun getCommonPrefix(vararg strs: String?): String? = StringUtils.getCommonPrefix(*strs)

    //region Misc
    /**
     * 比较两个字符串的“距离”(相似度)， 这个“距离”其实就是从源字符串变换到目标字符串需要进行的删除、插入和替换的次数。
     *
     * <pre>
     * StringKit.getLevenshteinDistance(null, *)             = IllegalArgumentException
     * StringKit.getLevenshteinDistance(*, null)             = IllegalArgumentException
     * StringKit.getLevenshteinDistance("","")               = 0
     * StringKit.getLevenshteinDistance("","a")              = 1
     * StringKit.getLevenshteinDistance("aaapppp", "")       = 7
     * StringKit.getLevenshteinDistance("frog", "fog")       = 1
     * StringKit.getLevenshteinDistance("fly", "ant")        = 3
     * StringKit.getLevenshteinDistance("elephant", "hippo") = 7
     * StringKit.getLevenshteinDistance("hippo", "elephant") = 7
     * StringKit.getLevenshteinDistance("hippo", "zzzzzzzz") = 8
     * StringKit.getLevenshteinDistance("hello", "hallo")    = 1
     * </pre>
     * @param s 第一个字符串, 不能为null
     * @param t 第一个字符串, 不能为null
     * @return 距离
     * @throws IllegalArgumentException 两参数之一为null时
     * @since 1.0.0
     */
    fun getLevenshteinDistance(s: CharSequence?, t: CharSequence?): Int = StringUtils.getLevenshteinDistance(s, t)

    /**
     * 如果两个字符串的“距离”(相似度)小于等于给定的极限值，就返回该“距离”，否则返回-1。
     * 这个“距离”其实就是从源字符串变换到目标字符串需要进行的删除、插入和替换的次数。
     *
     * <pre>
     * StringKit.getLevenshteinDistance(null, *, *)             = IllegalArgumentException
     * StringKit.getLevenshteinDistance(*, null, *)             = IllegalArgumentException
     * StringKit.getLevenshteinDistance(*, *, -1)               = IllegalArgumentException
     * StringKit.getLevenshteinDistance("","", 0)               = 0
     * StringKit.getLevenshteinDistance("aaapppp", "", 8)       = 7
     * StringKit.getLevenshteinDistance("aaapppp", "", 7)       = 7
     * StringKit.getLevenshteinDistance("aaapppp", "", 6))      = -1
     * StringKit.getLevenshteinDistance("elephant", "hippo", 7) = 7
     * StringKit.getLevenshteinDistance("elephant", "hippo", 6) = -1
     * StringKit.getLevenshteinDistance("hippo", "elephant", 7) = 7
     * StringKit.getLevenshteinDistance("hippo", "elephant", 6) = -1
     * </pre>
     *
     * @param s         第一个字符串, 不能为null
     * @param t         第一个字符串, 不能为null
     * @param threshold 目标上限值, 不能为负数
     * @return 距离或-1
     * @throws IllegalArgumentException 两字符串之一为null或极限值为负数时
     * @since 1.0.0
     */
    fun getLevenshteinDistance(s: CharSequence?, t: CharSequence?, threshold: Int): Int =
        StringUtils.getLevenshteinDistance(s, t, threshold)
    //endregion Misc

    //region startsWith
    /**
     * 检查字符串的前缀是否为给定的值
     *
     * <pre>
     * StringKit.startsWith(null, null)      = true
     * StringKit.startsWith(null, "abc")     = false
     * StringKit.startsWith("abcdef", null)  = false
     * StringKit.startsWith("abcdef", "abc") = true
     * StringKit.startsWith("ABCDEF", "abc") = false
     * </pre>
     * @param str    待查找的字符序列，可以为null，为null返回false
     * @param prefix 前缀字符串, 可以为null，为null返回false
     * @return true: 是字符串的前缀或两个参数都为null
     * @see java.lang.String.startsWith
     * @since 1.0.0
     */
    fun startsWith(str: CharSequence?, prefix: CharSequence?): Boolean = StringUtils.startsWith(str, prefix)

    /**
     * 大小写不敏感的前缀匹配
     *
     * <pre>
     * StringKit.startsWithIgnoreCase(null, null)      = true
     * StringKit.startsWithIgnoreCase(null, "abc")     = false
     * StringKit.startsWithIgnoreCase("abcdef", null)  = false
     * StringKit.startsWithIgnoreCase("abcdef", "abc") = true
     * StringKit.startsWithIgnoreCase("ABCDEF", "abc") = true
     * </pre>
     * @param str    待查找的字符序列，可以为null，为null返回false
     * @param prefix 前缀字符串, 可以为null，为null返回false
     * @return true: 是字符串的前缀(大小写不敏感)或两个参数都为null
     * @see java.lang.String.startsWith
     * @since 1.0.0
     */
    fun startsWithIgnoreCase(str: CharSequence?, prefix: CharSequence?): Boolean =
        StringUtils.startsWithIgnoreCase(str, prefix)

    /**
     * 检查字符串的前缀是否为给定的任何一个值（大小写不敏感）
     *
     * <pre>
     * StringKit.startsWithAny(null, null)      = false
     * StringKit.startsWithAny(null, new String[] {"abc"})  = false
     * StringKit.startsWithAny("abcxyz", null)     = false
     * StringKit.startsWithAny("abcxyz", new String[] {""}) = false
     * StringKit.startsWithAny("abcxyz", new String[] {"abc"}) = true
     * StringKit.startsWithAny("abcxyz", new String[] {null, "xyz", "abc"}) = true
     * </pre>
     * @param string        待查找的字符序列，可以为null
     * @param searchStrings 待匹配的前缀组, 可以为null
     * @return true: 任何一个为字符串的前缀(大小写不敏感)或两个参数都为null
     * @since 1.0.0
     */
    fun startsWithAny(string: CharSequence?, vararg searchStrings: CharSequence?): Boolean =
        StringUtils.startsWithAny(string, *searchStrings)
    //endregion startsWith

    //region endsWith
    /**
     * 检查字符串的后缀是否为给定的值
     * 该方法能安全地处理`null`而不抛出异常。两个`null`被当作相等。
     * 该比较是大小写相关。
     *
     * <pre>
     * StringKit.endsWith(null, null)      = true
     * StringKit.endsWith(null, "def")     = false
     * StringKit.endsWith("abcdef", null)  = false
     * StringKit.endsWith("abcdef", "def") = true
     * StringKit.endsWith("ABCDEF", "def") = false
     * StringKit.endsWith("ABCDEF", "cde") = false
     * </pre>
     * @param str    待查找的字符序列，可以为null
     * @param suffix 待匹配的后缀, 可以为null
     * @return true: 是字符串的后缀(大小写敏感)或两个参数都为null
     * @see java.lang.String.endsWith
     * @since 1.0.0
     */
    fun endsWith(str: CharSequence?, suffix: CharSequence?): Boolean = StringUtils.endsWith(str, suffix)

    /**
     * 检查字符串的后缀是否为给定的值(大小写不敏感)
     *
     * <pre>
     * StringKit.endsWithIgnoreCase(null, null)      = true
     * StringKit.endsWithIgnoreCase(null, "def")     = false
     * StringKit.endsWithIgnoreCase("abcdef", null)  = false
     * StringKit.endsWithIgnoreCase("abcdef", "def") = true
     * StringKit.endsWithIgnoreCase("ABCDEF", "def") = true
     * StringKit.endsWithIgnoreCase("ABCDEF", "cde") = false
     * </pre>
     * @param str    待查找的字符序列，可以为null
     * @param suffix 待匹配的后缀, 可以为null
     * @return true: 是字符串的后缀(大小不写敏感)或两个参数都为null
     * @see java.lang.String.endsWith
     * @since 1.0.0
     */
    fun endsWithIgnoreCase(str: CharSequence?, suffix: CharSequence?): Boolean =
        StringUtils.endsWithIgnoreCase(str, suffix)

    /**
     * 检查字符串的后缀是否为给定的任何一个值（大小写不敏感）
     *
     * <pre>
     * StringKit.endsWithAny(null, null)      = false
     * StringKit.endsWithAny(null, new String[] {"abc"})  = false
     * StringKit.endsWithAny("abcxyz", null)     = false
     * StringKit.endsWithAny("abcxyz", new String[] {""}) = true
     * StringKit.endsWithAny("abcxyz", new String[] {"xyz"}) = true
     * StringKit.endsWithAny("abcxyz", new String[] {null, "xyz", "abc"}) = true
     * </pre>
     * @param string        待查找的字符序列，可以为null
     * @param searchStrings 待匹配的前缀组, 可以为null或空串
     * @return 任何一个为字符串的后缀(大小写不敏感)或两个参数都为null
     * @since 1.0.0
     */
    fun endsWithAny(string: CharSequence?, vararg searchStrings: CharSequence?): Boolean =
        StringUtils.endsWithAny(string, *searchStrings)
    //endregion endsWith

    /**
     * 通过去掉前导和尾随空白并使用单个空格替换一系列空白字符，使空白标准化。 如果省略了该参数，上下文节点的字符串值将标准化并返回。
     *
     * @param str 源字符串, 可以为null，为null返回null
     * @return 替换后的字符串
     * @see .trim
     * @see [
     * http://www.w3.org/TR/xpath/.function-normalize-space](http://www.w3.org/TR/xpath/.function-normalize-space)
     * @since 1.0.0
     */
    fun normalizeSpace(str: String?): String? = StringUtils.normalizeSpace(str)

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.StringUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

}