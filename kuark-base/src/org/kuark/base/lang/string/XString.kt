package org.kuark.base.lang.string

import org.apache.commons.lang3.StringUtils
import org.kuark.base.security.CryptoKit
import org.kuark.base.security.DigestKit
import java.math.BigDecimal
import java.math.BigInteger
import java.util.regex.Matcher
import kotlin.math.ceil
import kotlin.reflect.KClass


/**
 * kotlin.String扩展函数
 *
 * @author K
 * @since 1.0.0
 */


/**
 * 将String类型的值转为指定类型的值
 *
 * @param returnType 目标类型
 * @return 指定类型的值
 */
fun <T: Any> String.toType(returnType: KClass<out T>): T { //TODO junit
    return when (returnType) {
        String::class -> this as T
        Byte::class -> this.toByte() as T
        Char::class -> this.toCharArray().first() as T
        Short::class -> this.toShort() as T
        Int::class -> this.toInt() as T
        Boolean::class -> this.toBoolean() as T
        Long::class -> this.toLong() as T
        Float::class -> this.toFloat() as T
        Double::class -> this.toDouble() as T
        BigDecimal::class -> this.toBigDecimal() as T
        BigInteger::class -> this.toBigInteger() as T
        else -> throw Exception("不支持的类型【$returnType】!")
    }
}


/**
 * 查找子串，并用指定字符串替换之（替换所有出现的地方），支持多对替换规则
 *
 * @param map Map<要查找的字符串, 用来替换的字符串>
 * @return 替换后的字符串
 * @since 1.0.0
 */
fun String.replaceEach(map: Map<String?, String?>): String {
    return if (map.isNotEmpty()) {
        this.replaceEach(map.keys.toTypedArray(), map.values.toTypedArray())
    } else this
}

/**
 * 将字符串转换为十六进制表示的值
 *
 * @return 转换后的十六进制表示的字符串
 * @since 1.0.0
 */
fun String.toHexStr(): CharSequence = String(CryptoKit.encodeHex(this.toString().toByteArray()))

/**
 * 解码十六进制表示的字符串
 *
 * @return 解码后的字符串
 * @since 1.0.0
 */
fun String.decodeHexStr(): CharSequence = String(CryptoKit.decodeHex(this.toString().toByteArray()))

/**
 * 对字符串进行MD5加密后，再进行十六进制编码
 *
 * @param saltStr 盐
 * @return 加密的字符串
 * @since 1.0.0
 */
fun String.toMd5HexStr(saltStr: CharSequence): String = DigestKit.getMD5(this.toString(), saltStr.toString())

/**
 * 将字符串按给定的长度均分(最后一组可能不是等分的)
 *
 * <pre>
 * "".divideAverage(*) = []
 * *.divideAverage(0) = []
 * *.divideAverage(-3) = []
 * "123456".divideAverage(3) = ["12", "34", "56"]
 * "1234567".divideAverage(3) = ["123", "456", "7"]
 * </pre>
 *
 * @param groupLen 每份长度
 * @return 等分后每个分组组成的数组
 * @since 1.0.0
 */
fun String.divideAverage(groupLen: Int): Array<String?> {
    if (groupLen <= 0) {
        return arrayOf()
    }
    val strLen = this.length
    val eachCount = ceil(strLen.toDouble() / groupLen).toInt()
    val groups = mutableListOf<String>()
    for (i in 0 until groupLen) {
        val beginIndex = i * eachCount
        var endIndex = if (i == groupLen - 1) { // 最后一组
            strLen
        } else {
            beginIndex + eachCount
        }
        groups.add(this.substring(beginIndex, endIndex))
    }
    return groups.toTypedArray()
}

/**
 * 将“驼峰”式写法的字符串转为用“_”分割的字符串
 *
 * <pre>
 * "".humpToUnderscore() = ""
 * " ".humpToUnderscore() = " "
 * "humpToUnderscore".humpToUnderscore() = "HUMP_TO_UNDERSCORE"
 * </pre>
 *
 * @return “_”分割的字符串, 并且是大写的
 * @since 1.0.0
 */
fun String.humpToUnderscore(): String {
    val sb = StringBuilder()
    sb.append(this[0])
    for (i in 1 until this.length) {
        if (Character.isUpperCase(this[i])) {
            sb.append("_")
        }
        sb.append(this[i])
    }
    return sb.toString().toUpperCase()
}

/**
 * 将“_”分割的字符串转为“驼峰”式写法的字符串, 如：HUMP_TO_Underscore -> humpToUnderscore
 *
 * <pre>
 * "".underscoreToHump() = ""
 * " ".underscoreToHump() = " "
 * "HUMP_TO_Underscore".underscoreToHump() = "humpToUnderscore"
 * </pre>
 *
 * @return “驼峰”式写法的字符串
 * @since 1.0.0
 */
fun String.underscoreToHump(): String {
    val sb = StringBuilder()
    val words = this.split("_")
    for (word in words) {
        sb.append(word.toLowerCase().capitalize())
    }
    return sb.first().toLowerCase() + sb.substring(1)
}

/**
 * 替换模板中的参数(以"${"和"}"括起来)
 *
 * @param paramMap 参数map
 * @return 替换后的字符串
 * @since 1.0.0
 */
fun String.fillTemplateByObjectMap(paramMap: Map<String, Any>): CharSequence {
    var templateStr = this.toString()
    for ((paramName, value) in paramMap) {
        templateStr = templateStr.replace(
            "\\$\\{" + paramName + "\\}".toRegex(), Matcher.quoteReplacement(value.toString())
        )
    }
    return templateStr
}


// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// 封装org.apache.commons.lang3.StringUtils
// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

/**
 * 检查给定是字符串是否包含任何空白字符
 *
 * @return true：如果字符串非空至少包含1个空白字符, false： 如果不包含
 * @since 1.0.0
 */
fun String.containsWhitespace(): Boolean = StringUtils.containsWhitespace(this)


//region ContainsAny
/**
 * 检查是否字符序列包含给定的字符组 searchChars 的任何一个字符
 *
 * <pre>
 * "".containsAny(*)                  = false
 * *.containsAny([])                  = false
 * "zzabyycdxx".containsAny(['z','a']) = true
 * "zzabyycdxx".containsAny(['b','y']) = true
 * "aba".containsAny(['z'])           = false
 * </pre>
 *
 * @param searchChars 要查找的字符组
 * @return true：任何给定的字符被找到，false：未找到
 * @since 1.0.0
 */
fun String.containsAny(vararg searchChars: Char): Boolean = StringUtils.containsAny(this, *searchChars)

/**
 * 检查是否字符序列包含给定的字符组 searchChars 的任何一个字符
 *
 * <pre>
 * "".containsAny(*)              = false
 * *.containsAny("")              = false
 * "zzabyycdxx".containsAny("za") = true
 * "zzabyycdxx".containsAny("by") = true
 * "aba".containsAny("z")          = false
 * </pre>
 *
 * @param searchChars 要查找的字符组, 可以为null
 * @return true：任何给定的字符被找到，false：未找到
 * @since 1.0.0
 */
fun String.containsAny(searchChars: CharSequence?): Boolean = StringUtils.containsAny(this, searchChars)
//endregion ContainsAny

//region IndexOfAnyBut chars
/**
 * 在字符序列中查找给定的一组字符 searchChars，返回第一次不出现给定的任何字符的位置
 *
 * <pre>
 * "".indexOfAnyBut(*)                    = -1
 * *.indexOfAnyBut([])                    = -1
 * "zzabyycdxx".indexOfAnyBut(['z', 'a']) = 3
 * "aba".indexOfAnyBut(['z'])             = 0
 * "aba".indexOfAnyBut(['a', 'b'])        = -1
 * </pre>
 *
 * @param searchChars 要查找的字符组
 * @return 任何第一次不匹配的字符的下标。如果没有找到，将返回-1
 * @since 1.0.0
 */
fun String.indexOfAnyBut(vararg searchChars: Char): Int = StringUtils.indexOfAnyBut(this, *searchChars)

/**
 * 在字符序列中查找给定的一组字符 searchChars，返回第一次不出现给定的任何字符的位置
 *
 * <pre>
 * "".indexOfAnyBut(*)              = -1
 * *.indexOfAnyBut("")              = -1
 * "zzabyycdxx".indexOfAnyBut("za") = 3
 * "zzabyycdxx".indexOfAnyBut("")   = -1
 * "aba".indexOfAnyBut("ab")        = -1
 * </pre>
 *
 * @param searchChars 要查找的字符组
 * @return 任何第一次不匹配的字符的下标。如果没有找，将返回-1
 * @since 1.0.0
 */
fun String.indexOfAnyBut(searchChars: CharSequence): Int = StringUtils.indexOfAnyBut(this, searchChars)
//endregion IndexOfAnyBut chars

//region ContainsOnly
/**
 * 检查字符序列是否只由 valid 中的字符组成
 *
 * <pre>
 * "".containsOnly(*)         = true
 * "ab".containsOnly('')      = false
 * "abab".containsOnly('abc') = true
 * "ab1".containsOnly('abc')  = false
 * "abz".containsOnly('abc')  = false
 * </pre>
 *
 * @param valid 有效的字符组
 * @return true: 如果只由valid中的字符组成或cs为空串， false: 包含其他字符
 * @since 1.0.0
 */
fun String.containsOnly(vararg valid: Char): Boolean = StringUtils.containsOnly(this, *valid)

/**
 * 检查字符序列是否只由 validChars 中的字符组成
 *
 * <pre>
 * "".containsOnly(*)         = true
 * "ab".containsOnly("")      = false
 * "abab".containsOnly("abc") = true
 * "ab1".containsOnly("abc")  = false
 * "abz".containsOnly("abc")  = false
 * </pre>
 *
 * @param validChars 有效的字符组
 * @return true: 如果只由validChars中的字符组成或为空串， false: 包含其他字符
 * @since 1.0.0
 */
fun String.containsOnly(validChars: String?): Boolean = StringUtils.containsOnly(this, validChars)
//endregion ContainsOnly

//region ContainsNone
/**
 * 检查字符序列是否都不由 searchChars 中的字符组成
 *
 * <pre>
 * "".containsNone(*)         = true
 * "ab".containsNone('')      = true
 * "abab".containsNone('xyz') = true
 * "ab1".containsNone('xyz')  = true
 * "abz".containsNone('xyz')  = false
 * </pre>
 *
 * @param searchChars 无效的字符组
 * @return true: 如果都不由searchChars中的字符组成或为空串，false: 包含searchChars中的任何字符
 * @since 1.0.0
 */
fun String.containsNone(vararg searchChars: Char): Boolean = StringUtils.containsNone(this, *searchChars)

/**
 * 检查字符序列是否都不由字符串 invalidChars 中的字符组成
 *
 * <pre>
 * "".containsNone(*)         = true
 * "ab".containsNone("")      = true
 * "abab".containsNone("xyz") = true
 * "ab1".containsNone("xyz")  = true
 * "abz".containsNone("xyz")  = false
 * </pre>
 *
 * @param invalidChars 无效的字符组
 * @return true: 如果都不由searchChars中的字符组成或为空串, false: 包含searchChars中的任何字符
 * @since 1.0.0
 */
fun String.containsNone(invalidChars: String): Boolean = StringUtils.containsNone(this, invalidChars)
//endregion ContainsNone


//region Left/Right/Mid
/**
 * 返回字符串最左边的 len 个字符
 *
 * <pre>
 * *.left(-ve)     = ""
 * "".left(*)      = ""
 * "abc".left(0)   = ""
 * "abc".left(2)   = "ab"
 * "abc".left(4)   = "abc"
 * </pre>
 *
 * @param len 子串的长度
 * @return 最左边的字符串, 为空串或len为负数将返回空串
 * @since 1.0.0
 */
fun String.left(len: Int): String? = StringUtils.leftPad(this, len)

/**
 * 返回字符串最右边的 len 个字符
 *
 * <pre>
 * *.right(-ve)     = ""
 * "".right(*)      = ""
 * "abc".right(0)   = ""
 * "abc".right(2)   = "bc"
 * "abc".right(4)   = "abc"
 * </pre>
 *
 * @param len 子串的长度
 * @return 最左边的字符串, 为空串或len为负数将返回空串
 * @since 1.0.0
 */
fun String.right(len: Int): String? = StringUtils.right(this, len)

/**
 * 返回字符串从 pos 位置开始的 len 个字符
 *
 * <pre>
 * *.mid(*, -ve)     = ""
 * "".mid(0, *)      = ""
 * "abc".mid(0, 2)   = "ab"
 * "abc".mid(0, 4)   = "abc"
 * "abc".mid(2, 4)   = "c"
 * "abc".mid(4, 2)   = ""
 * "abc".mid(-2, 2)  = "ab"
 * </pre>
 *
 * @param pos 开始位置, 负数将被当作0
 * @param len 子串的长度
 * @return 从 pos 位置开始的 len 个字符, 为空串或len为负数将返回空串
 * @since 1.0.0
 */
fun String.mid(pos: Int, len: Int): String? = StringUtils.mid(this, pos, len)
//endregion Left/Right/Mid

//region Substring between
/**
 * 从字符串中获取嵌在两个相同字符串 tag 中间的子串
 *
 * <pre>
 * "".substringBetween("")             = ""
 * "".substringBetween("tag")          = ""
 * "tagabctag".substringBetween("")    = ""
 * "tagabctag".substringBetween("tag") = "abc"
 * </pre>
 *
 * @param tag 子串前后的字符串
 * @return 子串, 未找到将返回空串，tag为空串将返回空串
 * @since 1.0.0
 */
fun String.substringBetween(tag: String): String? = StringUtils.substringBetween(this, tag) ?: ""

/**
 * 从字符串中获取嵌在字符串 open 和 close 中间的子串，返回第一次匹配的结果
 *
 * <pre>
 * "wx[b]yz".substringBetween("[", "]") = "b"
 * "".substringBetween("", "")          = ""
 * "".substringBetween("", "]")         = ""
 * "".substringBetween("[", "]")        = ""
 * "yabcz".substringBetween("", "")     = ""
 * "yabcz".substringBetween("y", "z")   = "abc"
 * "yabczyabcz".substringBetween("y", "z")   = "abc"
 * </pre>
 *
 * @param open  子串前的字符串
 * @param close 子串后的字符串
 * @return 子串, 未找到将返回空串，open/close为空串将返回空串
 * @since 1.0.0
 */
fun String.substringBetween(open: String, close: String): String =
    StringUtils.substringBetween(this, open, close) ?: ""

/**
 * 从字符串中获取嵌在字符串 open 和 close 中间的子串，返回全部匹配结果
 *
 * <pre>
 * "[a][b][c]".substringsBetween("[", "]") = ["a","b","c"]
 * "".substringsBetween("[", "]")          = []
 * </pre>
 *
 * @param open  标识子串开始的字符串
 * @param close 标识子串结束的字符串
 * @return 子串数组, 未找到返回空数组
 * @since 1.0.0
 */
fun String.substringsBetween(open: String, close: String): Array<String> =
    StringUtils.substringsBetween(this, open, close)
//endregion Substring between

//region Splitting

/**
 * 根据字符的类型(由`java.lang.Character.getType(char)`返回)分隔字符串。多个连续的相同类型的字符将被当作同一组返回。
 *
 * <pre>
 * "".splitByCharacterType()           = []
 * "ab de fg".splitByCharacterType()   = ["ab", " ", "de", " ", "fg"]
 * "ab   de fg".splitByCharacterType() = ["ab", "   ", "de", " ", "fg"]
 * "ab:cd:ef".splitByCharacterType()   = ["ab", ":", "cd", ":", "ef"]
 * "number5".splitByCharacterType()    = ["number", "5"]
 * "fooBar".splitByCharacterType()     = ["foo", "B", "ar"]
 * "foo200Bar".splitByCharacterType()  = ["foo", "200", "B", "ar"]
 * "ASFRules".splitByCharacterType()   = ["ASFR", "ules"]
 * </pre>
 *
 * @return 子串数组
 * @since 1.0.0
 */
fun String.splitByCharacterType(): Array<String> = StringUtils.splitByCharacterTypeCamelCase(this)

/**
 * 根据字符的类型(由`java.lang.Character.getType(char)`返回)分隔字符串。
 * 多个连续的相同类型的字符将被当作同一组返回。 以下情况除外： 大写字母紧跟着小写字母(驼峰)，这样，该大写字母将属于小写字母的组
 *
 * <pre>
 * "".splitByCharacterTypeCamelCase()           = []
 * "ab de fg".splitByCharacterTypeCamelCase()   = ["ab", " ", "de", " ", "fg"]
 * "ab   de fg".splitByCharacterTypeCamelCase() = ["ab", "   ", "de", " ", "fg"]
 * "ab:cd:ef".splitByCharacterTypeCamelCase()   = ["ab", ":", "cd", ":", "ef"]
 * "number5".splitByCharacterTypeCamelCase()    = ["number", "5"]
 * "fooBar".splitByCharacterTypeCamelCase()     = ["foo", "Bar"]
 * "foo200Bar".splitByCharacterTypeCamelCase()  = ["foo", "200", "Bar"]
 * "ASFRules".splitByCharacterTypeCamelCase()   = ["ASF", "Rules"]
 * </pre>
 *
 * @return 子串数组
 * @since 1.0.0
 */
fun String.splitByCharacterTypeCamelCase(): Array<String>? = StringUtils.splitByCharacterType(this)
//endregion Splitting

/**
 * 删除所有空白字符
 *
 * <pre>
 * "".deleteWhitespace()           = ""
 * "abc".deleteWhitespace()        = "abc"
 * "   ab  c  ".deleteWhitespace() = "abc"
 * </pre>
 *
 * @return 没有空白字符的字符串
 * @since 1.0.0
 */
fun String.deleteWhitespace(): String = StringUtils.deleteWhitespace(this)

//region Remove

/**
 * 如果子串在主串的开头部分（大小写不敏感），则删除该子串，否则返回源主串
 *
 * <pre>
 * "".removeStartIgnoreCase(*)        = ""
 * *.removeStartIgnoreCase(null)      = *
 * "www.domain.com".removeStartIgnoreCase("www.")   = "domain.com"
 * "www.domain.com".removeStartIgnoreCase("WWW.")   = "domain.com"
 * "domain.com".removeStartIgnoreCase("www.")       = "domain.com"
 * "www.domain.com".removeStartIgnoreCase("domain") = "www.domain.com"
 * "abc".removeStartIgnoreCase("")    = "abc"
 * </pre>
 *
 * @param remove 要删除的子串，可以为null，为null或为空串时返回源字符串
 * @return 去掉开头部分的子串后的字符串
 * @since 1.0.0
 */
fun String.removePrefixIgnoreCase(remove: String?): String? = StringUtils.removeStart(this, remove)

/**
 * 如果子串在主串的末尾（大小写不敏感），则删除该子串，否则返回源主串
 *
 * <pre>
 * "".removeEndIgnoreCase(*)        = ""
 * *.removeEndIgnoreCase(null)      = *
 * "www.domain.com".removeEndIgnoreCase(".com.")  = "www.domain.com"
 * "www.domain.com".removeEndIgnoreCase(".com")   = "www.domain"
 * "www.domain.com".removeEndIgnoreCase("domain") = "www.domain.com"
 * "abc".removeEndIgnoreCase("")    = "abc"
 * "www.domain.com".removeEndIgnoreCase(".COM") = "www.domain")
 * "www.domain.COM".removeEndIgnoreCase(".com") = "www.domain")
 * </pre>
 *
 * @param remove 要删除的子串，可以为null，为null或为空串时返回源字符串
 * @return 去掉末尾的子串后的字符串
 * @since 1.0.0
 */
fun String.removeSuffixIgnoreCase(remove: String?): String? = StringUtils.removeEndIgnoreCase(this, remove)

//endregion Remove

//region Replacing

/**
 * 查找子串，并用指定字符串替换之, 限定替换次数
 *
 * <pre>
 * "".replace(*, *, *)           = ""
 * "any".replace(null, *, *)     = "any"
 * "any".replace(*, null, *)     = "any"
 * "any".replace("", *, *)       = "any"
 * "any".replace(*, *, 0)        = "any"
 * "abaa".replace("a", null, -1) = "abaa"
 * "abaa".replace("a", "", -1)   = "b"
 * "abaa".replace("a", "z", 0)   = "abaa"
 * "abaa".replace("a", "z", 1)   = "zbaa"
 * "abaa".replace("a", "z", 2)   = "zbza"
 * "abaa".replace("a", "z", -1)  = "zbzz"
 * </pre>
 *
 * @param searchString 要查找的字符串, 为null或空串时返回源字符串
 * @param replacement  用来替换的字符串, 为null时返回源字符串
 * @param max          最大替换次数
 * @return 替换后的字符串
 * @since 1.0.0
 */
fun String.replace(searchString: String?, replacement: String?, max: Int): String =
    StringUtils.replace(this, searchString, replacement, max)

/**
 * 查找子串，并用指定字符串替换之（替换所有出现的地方），支持多对替换规则
 *
 * <pre>
 * "".replaceEach(*, *)          = ""
 * "aba".replaceEach(null, null) = "aba"
 * "aba".replaceEach(String[0], null) = "aba"
 * "aba".replaceEach(null, String[0]) = "aba"
 * "aba".replaceEach(String[]{"a"}, null)  = "aba"
 * "aba".replaceEach(String[]{"a"}, String[]{""})  = "b"
 * "aba".replaceEach(String[]{null}, String[]{"a"})  = "aba"
 * "abcde".replaceEach(String[]{"ab", "d"}, String[]{"w", "t"})  = "wcte"
 * "abcde".replaceEach(String[]{"ab", "d"}, String[]{"d", "t"})  = "dcte"
 * </pre>
 *
 * @param searchList      要查找的字符串数组
 * @param replacementList 用来替换的字符串数组，与查找的数组元素一一对应。为null时返回源字符串
 * @return 替换后的字符串
 * @throws IllegalArgumentException 如果两个数组的长度不一致时(null或空数组是允许的)
 * @since 1.0.0
 */
fun String.replaceEach(searchList: Array<String?>?, replacementList: Array<String?>?): String =
    StringUtils.replaceEach(this, searchList, replacementList)

/**
 * 查找子串，并用指定字符串循环替换之（替换所有出现的地方），支持多对替换规则
 *
 * <pre>
 * "".replaceEachRepeatedly(*, *) = ""
 * "aba".replaceEachRepeatedly(null, null) = "aba"
 * "aba".replaceEachRepeatedly(String[0], null) = "aba"
 * "aba".replaceEachRepeatedly(null, String[0]) = "aba"
 * "aba".replaceEachRepeatedly(String[]{"a"}, null, *) = "aba"
 * "aba".replaceEachRepeatedly(String[]{"a"}, String[]{""}) = "b"
 * "aba".replaceEachRepeatedly(String[]{null}, String[]{"a"}) = "aba"
 * "abcde".replaceEachRepeatedly(String[]{"ab", "d"}, String[]{"w", "t"}) = "wcte"
 * "abcde".replaceEachRepeatedly(String[]{"ab", "d"}, String[]{"d", "t"}) = "tcte"
 * "abcde".replaceEachRepeatedly(String[]{"ab", "d"}, String[]{"d", "ab"}) = IllegalStateException
 * </pre>
 *
 * @param searchList      要查找的字符串数组，为null时返回源字符串
 * @param replacementList 用来替换的字符串数组，与查找的数组元素一一对应。为null时返回源字符串
 * @return 替换后的字符串
 * @throws IllegalStateException    死循环时
 * @throws IllegalArgumentException 如果两个数组的长度不一致时(null或空数组是允许的)
 * @since 1.0.0
 */
fun String.replaceEachRepeatedly(searchList: Array<String?>?, replacementList: Array<String>?): String =
    StringUtils.replaceEachRepeatedly(this, searchList, replacementList)

//endregion Replacing

/**
 * 删除字符串末尾的换行符("\n"、"\r"、"\r\n")
 *
 * <pre>
 * "".chomp()            = ""
 * "abc \r".chomp()      = "abc "
 * "abc\n".chomp()       = "abc"
 * "abc\r\n".chomp()     = "abc"
 * "abc\r\n\r\n".chomp() = "abc\r\n"
 * "abc\n\r".chomp()     = "abc\n"
 * "abc\n\rabc".chomp()  = "abc\n\rabc"
 * "\r".chomp()          = ""
 * "\n".chomp()          = ""
 * "\r\n".chomp()        = ""
 * </pre>
 *
 * @return 处理后的字符串
 * @since 1.0.0
 */
fun String.chomp(): String = StringUtils.chomp(this)

/**
 * 删除字符串的最后一个字符(如果该字符串是以"\r\n"结尾，同样也删除这两个字符)
 *
 * <pre>
 * "".chop()            = ""
 * "abc \r".chop()      = "abc "
 * "abc\n".chop()       = "abc"
 * "abc\r\n".chop()     = "abc"
 * "abc".chop()         = "ab"
 * "abc\nabc".chop()    = "abc\nab"
 * "a".chop()           = ""
 * "\r".chop()          = ""
 * "\n".chop()          = ""
 * "\r\n".chop()        = ""
 * </pre>
 *
 * @return 处理后的字符串
 * @since 1.0.0
 */
fun String.chop(): String = StringUtils.chop(this)

/**
 * 字符串自相连指定次数,两两间用分隔串分隔
 *
 * <pre>
 * "".repeat(null, 0)   = ""
 * "".repeat("", 2)     = ""
 * "".repeat("x", 3)    = "xxx"
 * "?".repeat(", ", 3)  = "?, ?, ?"
 * </pre>
 *
 * @param separator 分隔串, 为null时当作空串
 * @param repeat    重复的次数，负数当作0
 * @return 自相连后的字符串
 * @since 1.0.0
 */
fun String.repeatAndSeparate(separator: String?, repeat: Int): String? = StringUtils.repeat(this, separator, repeat)

//region Centering

/**
 * 用指定字符左右补全源字符串到指定长度
 *
 * <pre>
 * "".center(4, ' ')     = "    "
 * "ab".center(-1, ' ')  = "ab"
 * "ab".center(4, ' ')   = " ab"
 * "abcd".center(2, ' ') = "abcd"
 * "a".center(4, ' ')    = " a  "
 * "a".center(4, 'y')    = "yayy"
 * </pre>
 *
 * @param size 要求的长度, 负数当作0，小于源字符串长度将返回源字符串
 * @param padChar 用于补全的字符
 * @return 补全长度后的字符串
 * @since 1.0.0
 */
fun String.center(size: Int, padChar: Char): String? = StringUtils.center(this, size, padChar)

/**
 * 用指定字符串左右补全源字符串到指定长度
 *
 * <pre>
 * "".center(4, " ")     = "    "
 * "ab".center(-1, " ")  = "ab"
 * "ab".center(4, " ")   = " ab"
 * "abcd".center(2, " ") = "abcd"
 * "a".center(4, " ")    = " a  "
 * "a".center(4, "yz")   = "yayz"
 * "abc".center(7, null) = "  abc  "
 * "abc".center(7, "")   = "  abc  "
 * </pre>
 *
 * @param size   要求的长度, 负数当作0，小于源字符串长度将返回源字符串
 * @param padStr 用于补全的字符串
 * @return 补全长度后的字符串
 * @since 1.0.0
 */
fun String.center(size: Int, padStr: String?): String? = StringUtils.center(this, size, padStr)

//endregion Centering

//region Case conversion

/**
 * 将源字符串首字母小写
 *
 * <pre>
 * "".uncapitalize()    = ""
 * "Cat".uncapitalize() = "cat"
 * "CAT".uncapitalize() = "cAT"
 * </pre>
 *
 * @return 首字母小写的字符串
 * @since 1.0.0
 */
fun String.uncapitalize(): String = StringUtils.uncapitalize(this)

/**
 * 将源字符串中的大写转成小写，小写转成大写
 *
 * <pre>
 * "".swapCase()                   = ""
 * "The dog has a BONE".swapCase() = "tHE DOG HAS A bone"
 * </pre>
 *
 * @return 转换后的字符串
 * @since 1.0.0
 */
fun String.swapCase(): String = StringUtils.swapCase(this)

//endregion Case conversion

/**
 * 计算子串在源字符串中出现的次数
 *
 * <pre>
 * "".countMatches(*)         = 0
 * "abba".countMatches(null)  = 0
 * "abba".countMatches("")    = 0
 * "abba".countMatches("a")   = 2
 * "abba".countMatches("ab")  = 1
 * "abba".countMatches("xxx") = 0
 * </pre>
 *
 * @param sub 子串, 可以为null
 * @return 子串出现的次数
 * @since 1.0.0
 */
fun String.countMatches(sub: CharSequence?): Int = StringUtils.countMatches(this, sub)

//region Character Tests
/**
 * 测试字符序列是否只包含Unicode字母
 *
 * <pre>
 * "".isAlpha()     = false
 * "  ".isAlpha()   = false
 * "abc".isAlpha()  = true
 * "ab2c".isAlpha() = false
 * "ab-c".isAlpha() = false
 * </pre>
 *
 * @return true: 只包含Unicode字母
 * @since 1.0.0
 */
fun String.isAlpha(): Boolean = StringUtils.isAlpha(this)

/**
 * 测试字符序列是否只包含Unicode字母或空格
 *
 * <pre>
 * "".isAlphaSpace()     = true
 * "  ".isAlphaSpace()   = true
 * "abc".isAlphaSpace()  = true
 * "ab c".isAlphaSpace() = true
 * "ab2c".isAlphaSpace() = false
 * "ab-c".isAlphaSpace() = false
 * </pre>
 *
 * @return true: 只包含Unicode字母或空格
 * @since 1.0.0
 */
fun String.isAlphaSpace(): Boolean = StringUtils.isAlphaSpace(this)

/**
 * 测试字符序列是否只包含Unicode字母或数字
 *
 * <pre>
 * "".isAlphanumeric()     = false
 * "  ".isAlphanumeric()   = false
 * "abc".isAlphanumeric()  = true
 * "ab c".isAlphanumeric() = false
 * "ab2c".isAlphanumeric() = true
 * "ab-c".isAlphanumeric() = false
 * </pre>
 *
 * @return true: 只包含Unicode字母或数字
 * @since 1.0.0
 */
fun String.isAlphanumeric(): Boolean = StringUtils.isAlpha(this)

/**
 * 测试字符序列是否只包含Unicode字母、空格或数字
 *
 * <pre>
 * "".isAlphanumericSpace()     = true
 * "  ".isAlphanumericSpace()   = true
 * "abc".isAlphanumericSpace()  = true
 * "ab c".isAlphanumericSpace() = true
 * "ab2c".isAlphanumericSpace() = true
 * "ab-c".isAlphanumericSpace() = false
 * </pre>
 *
 * @return true: 只包含Unicode字母、空格或数字
 * @since 1.0.0
 */
fun String.isAlphanumericSpace(): Boolean = StringUtils.isAlphanumeric(this)

/**
 * 测试字符序列是否只包含ASCII码的可打印的字符
 *
 * <pre>
 * "".isAsciiPrintable()       = true
 * " ".isAsciiPrintable()      = true
 * "Ceki".isAsciiPrintable()   = true
 * "ab2c".isAsciiPrintable()   = true
 * "!ab-c~".isAsciiPrintable() = true
 * "\u0020".isAsciiPrintable() = true
 * "\u0021".isAsciiPrintable() = true
 * "\u007e".isAsciiPrintable() = true
 * "\u007f".isAsciiPrintable() = false
 * "Ceki G\u00fclc\u00fc".isAsciiPrintable() = false
 * </pre>
 *
 * @return true: 每个字符都在32到126的范围内
 * @since 1.0.0
 */
fun String.isAsciiPrintable(): Boolean = StringUtils.isAsciiPrintable(this)

/**
 * 测试字符序列是否只包含Unicode数字。 十进制的小数不是Unicode数字。
 *
 * <pre>
 * "".isNumeric()     = false
 * "  ".isNumeric()   = false
 * "123".isNumeric()  = true
 * "12 3".isNumeric() = false
 * "ab2c".isNumeric() = false
 * "12-3".isNumeric() = false
 * "12.3".isNumeric() = false
 * </pre>
 *
 * @return true: 只包含Unicode数字
 * @since 1.0.0
 */
fun String.isNumeric(): Boolean = StringUtils.isNumeric(this)

/**
 * 测试字符序列是否只包含Unicode数字或空格。 十进制的小数不是Unicode数字。
 *
 * <pre>
 * "".isNumericSpace()     = true
 * "  ".isNumericSpace()   = true
 * "123".isNumericSpace()  = true
 * "12 3".isNumericSpace() = true
 * "ab2c".isNumericSpace() = false
 * "12-3".isNumericSpace() = false
 * "12.3".isNumericSpace() = false
 * </pre>
 *
 * @return true: 只包含Unicode数字或空格
 * @since 1.0.0
 */
fun String.isNumericSpace(): Boolean = StringUtils.isNumeric(this)

/**
 * 测试字符序列是否只包含空白字符
 *
 * <pre>
 * "".isWhitespace()     = true
 * "  ".isWhitespace()   = true
 * "abc".isWhitespace()  = false
 * "ab2c".isWhitespace() = false
 * "ab-c".isWhitespace() = false
 * </pre>
 *
 * @return true：只包含空白字符
 * @since 1.0.0
 */
fun String.isWhitespace(): Boolean = StringUtils.isWhitespace(this)

/**
 * 测试字符序列是否只包含小写字母
 *
 * <pre>
 * "".isAllLowerCase()     = false
 * "  ".isAllLowerCase()   = false
 * "abc".isAllLowerCase()  = true
 * "abC".isAllLowerCase() = false
 * </pre>
 *
 * @return true：只包含小写字母
 * @since 1.0.0
 */
fun String.isAllLowerCase(): Boolean = StringUtils.isAllLowerCase(this)

/**
 * 测试字符序列是否只包含大写字母
 *
 * <pre>
 * "".isAllUpperCase()     = false
 * "  ".isAllUpperCase()   = false
 * "ABC".isAllUpperCase()  = true
 * "aBC".isAllUpperCase() = false
 * </pre>
 *
 * @return true：只包含大写字母
 * @since 1.0.0
 */
fun String.isAllUpperCase(): Boolean = StringUtils.isAllUpperCase(this)

//endregion Character Tests

/**
 * 根据分隔符反转字符串，分隔符间的字符串当作一个整体(本身不反转)
 *
 * <pre>
 * "".reverseDelimited(*)        = ""
 * "a.b.c".reverseDelimited('x') = "a.b.c"
 * "a.b.c".reverseDelimited(".") = "c.b.a"
 * "java.lang.String".reverseDelimited(".") = "String.lang.java"
 * </pre>
 *
 * @param separatorChar 分隔符
 * @return 反转后的字符串
 * @since 1.0.0
 */
fun String.reverseDelimited(separatorChar: Char): String? = StringUtils.reverseDelimited(this, separatorChar)

//region Abbreviating
/**
 * 省略字符串
 * 规则:
 *  * 如果 `str` 的长度 比 `maxWidth` 还小, return `str`
 *  * 否则省略为： `(substring(str, 0, max-3) + "...")`.
 *  * 如果 `maxWidth` 比 `4` 小, 抛出
 * `IllegalArgumentException` 异常.
 *  * 永远不会返回长度超过 `maxWidth` 的字符串.
 *
 * <pre>
 * "".abbreviate(4)        = ""
 * "abcdefg".abbreviate(6) = "abc..."
 * "abcdefg".abbreviate(7) = "abcdefg"
 * "abcdefg".abbreviate(8) = "abcdefg"
 * "abcdefg".abbreviate(4) = "a..."
 * "abcdefg".abbreviate(3) = IllegalArgumentException
 * </pre>
 *
 * @param maxWidth 返回的字符串的最大长度，必须大于等于4
 * @return 省略的字符串
 * @since 1.0.0
 */
fun String.abbreviate(maxWidth: Int): String? = StringUtils.abbreviate(this, maxWidth)

/**
 * 省略字符串，功能类似`abbreviate(int)`，但可以指定左边界偏移量
 * 左边界偏移量不一定要求最左边的字符出现在结果字符串的最左边， 或紧跟省略号之后，但它一定会出现在结果字符串的某个地方
 *
 * <pre>
 * "".abbreviate(0, 4)                  = ""
 * "abcdefghijklmno".abbreviate(-1, 10) = "abcdefg..."
 * "abcdefghijklmno".abbreviate(0, 10)  = "abcdefg..."
 * "abcdefghijklmno".abbreviate(1, 10)  = "abcdefg..."
 * "abcdefghijklmno".abbreviate(4, 10)  = "abcdefg..."
 * "abcdefghijklmno".abbreviate(5, 10)  = "...fghi..."
 * "abcdefghijklmno".abbreviate(6, 10)  = "...ghij..."
 * "abcdefghijklmno".abbreviate(8, 10)  = "...ijklmno"
 * "abcdefghijklmno".abbreviate(10, 10) = "...ijklmno"
 * "abcdefghijklmno".abbreviate(12, 10) = "...ijklmno"
 * "abcdefghij".abbreviate(0, 3)        = IllegalArgumentException
 * "abcdefghij".abbreviate(5, 6)        = IllegalArgumentException
 * </pre>
 *
 * @param offset   源字符串的左边界偏移量
 * @param maxWidth 返回的字符串的最大长度，必须大于等于4
 * @return 省略的字符串
 * @throws IllegalArgumentException 结果长度小于4时
 * @since 1.0.0
 */
fun String.abbreviate(offset: Int, maxWidth: Int): String? = StringUtils.abbreviate(this, offset, maxWidth)

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
 * "abc".abbreviateMiddle(null, 0)      = "abc"
 * "abc".abbreviateMiddle(".", 0)      = "abc"
 * "abc".abbreviateMiddle(".", 3)      = "abc"
 * "abcdef".abbreviateMiddle(".", 4)     = "ab.f"
 * </pre>
 *
 * @param middle 用来替换中间字符的字符串, 可以为null，为null表示不替换
 * @param length 返回的字符串的最大长度
 * @return 省略的字符串
 * @since 1.0.0
 */
fun String.abbreviateMiddle(middle: String?, length: Int): String? =
    StringUtils.abbreviateMiddle(this, middle, length)

//endregion Abbreviating

//region Difference
/**
 * 比较两字符串，并返回它们不同的部分 (更精确的讲，返回第二个字符串从与第一个不同部分开始的剩下部分)
 *
 * <pre>
 * "".difference("") = ""
 * "".difference("abc") = "abc"
 * "abc".difference("") = ""
 * "abc".difference("abc") = ""
 * "ab".difference("abxyz") = "xyz"
 * "abcde".difference("abxyz") = "xyz"
 * "abcde".difference("xyz") = "xyz"
 * </pre>
 *
 * @param str2 第二个字符串
 * @return 两字符串不同的部分，相同时返回空串
 * @since 1.0.0
 */
fun String.difference(str2: String): String? = StringUtils.difference(this, str2)

/**
 * 比较两字符串，并返回它们开始不同时的下标
 *
 * <pre>
 * "".indexOfDifference("") = -1
 * "".indexOfDifference("abc") = 0
 * "abc".indexOfDifference("") = 0
 * "abc".indexOfDifference("abc") = -1
 * "ab".indexOfDifference("abxyz") = 2
 * "abcde".indexOfDifference("abxyz") = 2
 * "abcde".indexOfDifference("xyz") = 0
 * </pre>
 *
 * @param cs2 第一个字符串, 可以为null
 * @return 开始不同时的下标; 如果两字符串相同返回-1
 * @since 1.0.0
 */
fun String.indexOfDifference(cs2: CharSequence): Int = StringUtils.indexOfDifference(this, cs2)

/**
 * 比较数组中的每个字符串，并返回它们开始不同时的下标
 *
 * <pre>
 * {}.indexOfDifference() = -1
 * {"abc"}.indexOfDifference() = -1
 * {null, null}.indexOfDifference() = -1
 * {"", ""}.indexOfDifference() = -1
 * {"", null}.indexOfDifference() = 0
 * {"abc", null, null}.indexOfDifference() = 0
 * {null, null, "abc"}.indexOfDifference() = 0
 * {"", "abc"}.indexOfDifference() = 0
 * {"abc", ""}.indexOfDifference() = 0
 * {"abc", "abc"}.indexOfDifference() = -1
 * {"abc", "a"}.indexOfDifference() = 1
 * {"ab", "abxyz"}.indexOfDifference() = 2
 * {"abcde", "abxyz"}.indexOfDifference() = 2
 * {"abcde", "xyz"}.indexOfDifference() = 0
 * {"xyz", "abcde"}.indexOfDifference() = 0
 * {"i am a machine", "i am a robot"}.indexOfDifference() = 7
 * </pre>
 *
 * @return 开始不同时的下标; 如果字符串都相同返回-1
 * @since 1.0.0
 */
fun Array<String?>.indexOfDifference(): Int = StringUtils.indexOfDifference(*this)
//endregion Difference

/**
 * 比较数组中的每个字符串，并返回它们相同的前缀
 *
 * <pre>
 * {}.getCommonPrefix() = ""
 * {"abc"}.getCommonPrefix() = "abc"
 * {null, null}.getCommonPrefix() = ""
 * {"", ""}.getCommonPrefix() = ""
 * {"", null}.getCommonPrefix() = ""
 * {"abc", null, null}.getCommonPrefix() = ""
 * {null, null, "abc"}.getCommonPrefix() = ""
 * {"", "abc"}.getCommonPrefix() = ""
 * {"abc", ""}.getCommonPrefix() = ""
 * {"abc", "abc"}.getCommonPrefix() = "abc"
 * {"abc", "a"}.getCommonPrefix() = "a"
 * {"ab", "abxyz"}.getCommonPrefix() = "ab"
 * {"abcde", "abxyz"}.getCommonPrefix() = "ab"
 * {"abcde", "xyz"}.getCommonPrefix() = ""
 * {"xyz", "abcde"}.getCommonPrefix() = ""
 * {"i am a machine", "i am a robot"}.getCommonPrefix() = "i am a "
 * </pre>
 *
 * @return 相同的前缀
 * @since 1.0.0
 */
fun Array<String?>.getCommonPrefix(): String = StringUtils.getCommonPrefix(*this)

//region Misc
/**
 * 比较两个字符串的“距离”(相似度)， 这个“距离”其实就是从源字符串变换到目标字符串需要进行的删除、插入和替换的次数。
 *
 * <pre>
 * "".getLevenshteinDistance(,"")               = 0
 * "".getLevenshteinDistance(,"a")              = 1
 * "aaapppp".getLevenshteinDistance("")       = 7
 * "frog".getLevenshteinDistance("fog")       = 1
 * "fly".getLevenshteinDistance("ant")        = 3
 * "elephant".getLevenshteinDistance("hippo") = 7
 * "hippo".getLevenshteinDistance("elephant") = 7
 * "hippo".getLevenshteinDistance("zzzzzzzz") = 8
 * "hello".getLevenshteinDistance("hallo")    = 1
 * </pre>
 *
 * @param t 第二个字符串
 * @return 距离
 * @throws IllegalArgumentException 两参数之一为null时
 * @since 1.0.0
 */
fun String.getLevenshteinDistance(t: CharSequence): Int = StringUtils.getLevenshteinDistance(this, t)

/**
 * 如果两个字符串的“距离”(相似度)小于等于给定的极限值，就返回该“距离”，否则返回-1。
 * 这个“距离”其实就是从源字符串变换到目标字符串需要进行的删除、插入和替换的次数。
 *
 * <pre>
 * *.getLevenshteinDistance(*, -1)               = IllegalArgumentException
 * "".getLevenshteinDistance(,"", 0)               = 0
 * "aaapppp".getLevenshteinDistance("", 8)       = 7
 * "aaapppp".getLevenshteinDistance("", 7)       = 7
 * "aaapppp".getLevenshteinDistance("", 6))      = -1
 * "elephant".getLevenshteinDistance("hippo", 7) = 7
 * "elephant".getLevenshteinDistance("hippo", 6) = -1
 * "hippo".getLevenshteinDistance("elephant", 7) = 7
 * "hippo".getLevenshteinDistance("elephant", 6) = -1
 * </pre>
 *
 * 
 * @param t         第二个字符串
 * @param threshold 目标上限值, 不能为负数
 * @return 距离或-1
 * @throws IllegalArgumentException 极限值为负数时
 * @since 1.0.0
 */
fun String.getLevenshteinDistance(t: CharSequence, threshold: Int): Int =
    StringUtils.getLevenshteinDistance(this, t, threshold)
//endregion Misc

/**
 * 检查字符串的前缀是否为给定的任何一个值（大小写不敏感）
 *
 * <pre>
 * "abcxyz".startsWithAny(null)     = false
 * abcxyz".startsWithAny({""}) = false
 * abcxyz".startsWithAny({"abc"}) = true
 * abcxyz".startsWithAny({null, "xyz", "abc"}) = true
 * </pre>
 * 
 * @param searchStrings 待匹配的前缀组,
 * @return true: 任何一个为字符串的前缀(大小写不敏感)
 * @since 1.0.0
 */
fun String.startsWithAny(vararg searchStrings: CharSequence?): Boolean =
    StringUtils.startsWithAny(this, *searchStrings)

/**
 * 检查字符串的后缀是否为给定的任何一个值（大小写不敏感）
 *
 * <pre>
 * "abcxyz".endsWithAny(null)     = false
 * "abcxyz".endsWithAny(String[] {""}) = true
 * "abcxyz".endsWithAny(String[] {"xyz"}) = true
 * "abcxyz".endsWithAny(String[] {null, "xyz", "abc"}) = true
 * </pre>
 * 
 * @param searchStrings 待匹配的前缀组
 * @return 任何一个为字符串的后缀(大小写不敏感)
 * @since 1.0.0
 */
fun String.endsWithAny(vararg searchStrings: CharSequence?): Boolean =
    StringUtils.endsWithAny(this, *searchStrings)

/**
 * 通过去掉前导和尾随空白并使用单个空格替换一系列空白字符，使空白标准化。
 *
 * @return 替换后的字符串
 * @see .trim
 * @see [
 * http://www.w3.org/TR/xpath/.function-normalize-space](http://www.w3.org/TR/xpath/.function-normalize-space)
 * @since 1.0.0
 */
fun String.normalizeSpace(): String = StringUtils.normalizeSpace(this)

// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// 封装org.apache.commons.lang3.StringUtils
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^