package io.kuark.base.lang.string

import org.apache.commons.lang3.RandomStringUtils
import java.security.SecureRandom
import java.util.*
import kotlin.math.abs

/**
 * 随机字符串工具类
 *
 * @author K
 * @since 1.0.0
 */
object RandomStringKit {

    private val random = SecureRandom()

    /**
     * 封装JDK自带的UUID, 中间无"-"分割.
     *
     * @return 中间无"-"分割的UUID
     * @author K
     * @since 1.0.0
     */
    fun uuid(): String = UUID.randomUUID().toString().replace("-".toRegex(), "")

    /**
     * 使用SecureRandom随机生成Long.
     *
     * @return 随机Long
     * @author K
     * @since 1.0.0
     */
    fun randomLong(): String = abs(random.nextLong()).toString()

    /**
     * 基于Base62编码的SecureRandom随机生成bytes.
     *
     * @param length 长度
     * @return Base62编码的字符串
     * @author K
     * @since 1.0.0
     */
    fun randomBase62(length: Int): String {
        val randomBytes = ByteArray(length)
        random.nextBytes(randomBytes)
        return EncodeKit.encodeBase62(randomBytes)
    }

    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
    // 封装org.apache.commons.lang3.RandomStringUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    /**
     * 创建一个指定长度的随机串
     * 字符将从字符集中选择
     *
     * @param count 要创建的随机串的长度
     * @return 随机串
     * @author K
     * @since 1.0.0
     */
    fun random(count: Int): String = RandomStringUtils.random(count)

    /**
     * 创建一个指定长度的随机串
     * 字符将从ASCII码的`32` 到 `126`中选择
     *
     * @param count 要创建的随机串的长度
     * @return 随机串
     * @author K
     * @since 1.0.0
     */
    fun randomAscii(count: Int): String = RandomStringUtils.randomAscii(count)

    /**
     * 创建一个指定长度的随机串
     * 字符将从字母中选择
     *
     * @param count 要创建的随机串的长度
     * @return 随机串
     * @author K
     * @since 1.0.0
     */
    fun randomAlphabetic(count: Int): String = RandomStringUtils.randomAlphabetic(count)

    /**
     * 创建一个指定长度的随机串
     * 字符将从字母或数字中选择
     *
     * @param count 要创建的随机串的长度
     * @return 随机串
     * @author K
     * @since 1.0.0
     */
    fun randomAlphanumeric(count: Int): String = RandomStringUtils.randomAlphanumeric(count)

    /**
     * 创建一个指定长度的随机串
     * 字符将从数字中选择
     *
     * @param count 要创建的随机串的长度
     * @return 随机串
     * @author K
     * @since 1.0.0
     */
    fun randomNumeric(count: Int): String = RandomStringUtils.randomNumeric(count)

    /**
     * 创建一个指定长度的随机串
     * 字符将从字母或数字中选择
     *
     * @param count 要创建的随机串的长度
     * @param letters 如果为 `true`, 生成的字符将从字母中选择
     * @param numbers 如果为 `true`, 生成的字符将从数字中选择
     * @return 随机串
     * @author K
     * @since 1.0.0
     */
    fun random(count: Int, letters: Boolean, numbers: Boolean): String =
        RandomStringUtils.random(count, letters, numbers)

    /**
     * 创建一个指定长度的随机串
     * 字符将从字母或数字中选择
     *
     * @param count 要创建的随机串的长度
     * @param start 字符集范围的开始位置
     * @param end 字符集范围的结束位置
     * @param letters 如果为 `true`, 生成的字符将从字母中选择
     * @param numbers 如果为 `true`, 生成的字符将从数字中选择
     * @return 随机串
     * @author K
     * @since 1.0.0
     */
    fun random(count: Int, start: Int, end: Int, letters: Boolean, numbers: Boolean): String =
        RandomStringUtils.random(count, start, end, letters, numbers)

    /**
     * 创建一个随机串, 使用默认的随机源
     * 该方法与[.random]具有相同的语义,
     * 但是不使用外部提供的随机源, 它使用内部静态的[Random]实例.
     *
     * @param count 要创建的随机串的长度
     * @param start 字符集范围的开始位置
     * @param end 字符集范围的结束位置
     * @param letters 只允许字母?
     * @param numbers 只允许数字?
     * @param chars 字符可变数组 如果为 `null`, 将使用所有字符
     * @return 随机串
     * @throws ArrayIndexOutOfBoundsException 如果指定字符集中的元素不足`(end - start) + 1` 个
     * @author K
     * @since 1.0.0
     */
    fun random(
        count: Int, start: Int, end: Int, letters: Boolean, numbers: Boolean, vararg chars: Char
    ): String = RandomStringUtils.random(count, start, end, letters, numbers, *chars)

    /**
     * 创建一个随机串, 使用提供的随机源
     * 如果`start` 和 `end`都为`0`, `start` 和 `end`
     * 将分别被设置为 `' '` 和 `'z'`, ASCII码可打印字段将被使用, 除非`letters` 和
     * `numbers`都为`false`, 这样`start` 和 `end`将 分别被设置为
     * `0` 和 `Integer.MAX_VALUE`. 如果`chars`不为`null`,
     * `start` 到 `end` 间的字符将被选择
     * 该方法接受一个用户提供的随机源[Random]实例. 每次调用时使用同一个随机源和固定的种子, 相同的随机串将重复产生并且是可预见的.
     *
     * @param count 要创建的随机串的长度
     * @param start 字符集范围的开始位置
     * @param end 字符集范围的结束位置
     * @param letters 只允许字母?
     * @param numbers 只允许数字?
     * @param chars 字符数组 如果为 `null`, 将使用所有字符
     * @param random 随机源
     * @return 随机串
     * @throws ArrayIndexOutOfBoundsException 如果指定字符集中的元素不足`(end - start) + 1` 个
     * @throws IllegalArgumentException 如果 `count` &lt; 0.
     * @author K
     * @since 1.0.0
     */
    fun random(
        count: Int, start: Int, end: Int, letters: Boolean, numbers: Boolean, chars: CharArray?, random: Random?
    ): String = RandomStringUtils.random(count, start, end, letters, numbers, chars, random)

    /**
     * 创建一个指定长度的随机串
     * 字符将从指定的字符串中选择
     *
     * @param count 要创建的随机串的长度
     * @param chars 提供字符的字符串
     * @return 随机串
     * @throws IllegalArgumentException 如果 `count` &lt; 0.
     * @author K
     * @since 1.0.0
     */
    fun random(count: Int, chars: String? = null): String = RandomStringUtils.random(count, chars)

    /**
     * 创建一个指定长度的随机串
     * 字符将从指定的字符数组中选择
     *
     * @param count 要创建的随机串的长度
     * @param chars 提供字符的字符数组, 可以为 null
     * @return 随机串
     * @throws IllegalArgumentException 如果 `count` &lt; 0.
     * @author K
     * @since 1.0.0
     */
    fun random(count: Int, vararg chars: Char): String = RandomStringUtils.random(count, *chars)

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // 封装org.apache.commons.lang3.RandomStringUtils
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

}