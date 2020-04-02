package org.kuark.base.security

import org.apache.commons.lang3.Validate
import org.kuark.base.lang.string.EncodeKit
import org.kuark.base.log.LoggerFactory
import java.io.InputStream
import java.security.MessageDigest
import java.security.SecureRandom

/**
 * <p>
 * 支持SHA-1/MD5消息摘要的工具类
 * </p>
 *
 * <p>
 * 返回ByteSource，可进一步被编码为Hex, Base64或UrlSafeBase64
 * </p>
 *
 * @since 1.0.0
 */
object DigestKit {

    private val logger = LoggerFactory.getLogger(this::class)
    const val SHA1 = "SHA-1"
    const val MD5 = "MD5"

    private val random = SecureRandom()

    //region MD5
    /**
     * 对字符串进行MD5加密
     *
     * @param original 源字符串
     * @param salt 盐
     * @return 加密并转成16进制后的字符串
     * @since 1.0.0
     */
    fun getMD5(original: String, salt: String): String {
        val s = salt.toByteArray()
        val md5s = digest(original.toByteArray(), MD5, s, 1)
        return EncodeKit.encodeHex(md5s)
    }

    /**
     * 对字符串进行MD5加密
     *
     * @param original 源字符串字节数组
     * @param salt     盐
     * @return 加密并转成16进制后的字符串
     * @since 1.0.0
     */
    fun getMD5(original: ByteArray?, salt: String?): String? {
        if (original == null || original.isEmpty()) {
            return null
        }
        val s = salt?.toByteArray()
        val md5s = digest(original, MD5, s, 1)
        return EncodeKit.encodeHex(md5s)
    }

    /**
     * 测试字符串用md5加密后是否和给定的md5串相等
     *
     * @param str
     * 未加密的串
     * @param salt
     * 盐
     * @param md5Str
     * 加密后的串
     * @return true:源串加密后与给定的md5串相等，反之为false
     * @since 1.0.0
     */
    fun isMatchMD5(str: String, salt: String, md5Str: String): Boolean {
        var match = getMD5(str, salt) == md5Str
        if (!match && salt.isNotBlank()) {
            match = getMD5(str, "") == md5Str // 处理以前未加盐的历史数据
        }
        return match
    }

    /**
     * 对文件进行md5散列.
     *
     * @param input 文件输入流
     * @return 散列后的文件字节数组
     * @since 1.0.0
     */
    fun md5(input: InputStream): ByteArray = digest(input, MD5)

    //endregion

    //region SHA1
    /**
     * 对输入字符串字节数组进行sha1散列.
     *
     * @param input 字符串字节数组
     * @return 进行sha1散列后的字节数组
     * @since 1.0.0
     */
    fun sha1(input: ByteArray): ByteArray = digest(input, SHA1, null, 1)

    /**
     * 对输入字符串字节数组进行sha1散列.
     *
     * @param input 字符串字节数组
     * @param salt 加盐值字节数组
     * @return 进行sha1散列后的字节数组
     * @since 1.0.0
     */
    fun sha1(input: ByteArray, salt: ByteArray): ByteArray = digest(input, SHA1, salt, 1)

    /**
     * 对输入字符串字节数组进行sha1散列.
     *
     * @param input 字符串字节数组
     * @param salt 加盐值字节数组
     * @param iterations 迭代次数
     * @return 进行sha1散列后的字节数组
     * @since 1.0.0
     */
    fun sha1(input: ByteArray, salt: ByteArray, iterations: Int): ByteArray = digest(input, SHA1, salt, iterations)

    /**
     * 对文件进行sha1散列.
     *
     * @param input 文件输入流
     * @return 散列后的文件字节数组
     * @since 1.0.0
     */
    fun sha1(input: InputStream): ByteArray = digest(input, SHA1)

    //endregion

    private fun digest(input: InputStream, algorithm: String): ByteArray {
        val messageDigest = MessageDigest.getInstance(algorithm)
        val bufferLength = 8 * 1024
        val buffer = ByteArray(bufferLength)
        var read = input.read(buffer, 0, bufferLength)
        while (read > -1) {
            messageDigest.update(buffer, 0, read)
            read = input.read(buffer, 0, bufferLength)
        }
        return messageDigest.digest()
    }

    /**
     * 对字符串进行散列, 支持md5与sha1算法.
     *
     * @param input 字符串字节数组
     * @param algorithm 算法名称，ALGORITHM_SHA1或ALGORITHM_MD5
     * @param salt 加盐值字节数组
     * @param iterations 迭代次数
     * @return 进行散列后的字节数组
     * @since 1.0.0
     */
    fun digest(input: ByteArray?, algorithm: String?, salt: ByteArray?, iterations: Int): ByteArray {
        val digest = MessageDigest.getInstance(algorithm)
        if (salt != null) {
            digest.update(salt)
        }
        var result = digest.digest(input)
        for (i in 1 until iterations) {
            digest.reset()
            result = digest.digest(result)
        }
        return result
    }

    /**
     * 生成随机的Byte[]作为salt.
     *
     * @param numBytes byte数组的大小
     * @return 加盐值字节数组
     * @since 1.0.0
     */
    fun generateSalt(numBytes: Int): ByteArray? {
        Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes)
        val bytes = ByteArray(numBytes)
        random.nextBytes(bytes)
        return bytes
    }

}