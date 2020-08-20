package io.kuark.base.security

import org.apache.commons.codec.binary.Base64
import io.kuark.base.lang.string.EncodeKit
import io.kuark.base.log.LogFactory
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * 加密工具类
 *
 * 支持HMAC-SHA1消息签名 及 DES/AES对称加密.
 * 支持Hex与Base64两种编码方式.
 *
 * @author K
 * @since 1.0.0
 */
object CryptoKit {

    private val logger = LogFactory.getLog(this::class)

    private const val AES = "AES"
    private const val HMACSHA1 = "HmacSHA1"
    private const val HTB = "_HTB"

    private val KEY = CryptoKey.KEY_DEFAULT

    private const val PREFIX = "┼"
    private const val CHARSET = "UTF-8"

    private const val DEFAULT_HMACSHA1_KEYSIZE = 160 //RFC2401

    private const val DEFAULT_AES_KEYSIZE = 128
    private const val DEFAULT_IVSIZE = 16

    private val DIGITS = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    )


    private val random = SecureRandom()

    //-- HMAC-SHA1 funciton --//
    /**
     * 使用HMAC-SHA1进行消息签名, 返回字节数组,长度为20字节
     *
     * @param input 原始输入字符数组
     * @param key   HMAC-SHA1密钥
     * @return 签名后的字节数组
     * @since 1.0.0
     */
    fun hmacSha1(input: ByteArray, key: ByteArray): ByteArray {
        val secretKey = SecretKeySpec(key, HMACSHA1)
        val mac = Mac.getInstance(HMACSHA1)
        mac.init(secretKey)
        return mac.doFinal(input)
    }

    /**
     * 校验HMAC-SHA1签名是否正确.
     *
     * @param expected 已存在的签名
     * @param input    原始输入字符串
     * @param key      密钥
     * @return true: HMAC-SHA1签名正确
     * @since 1.0.0
     */
    fun isMacValid(expected: ByteArray, input: ByteArray, key: ByteArray): Boolean {
        val actual = hmacSha1(input, key)
        return expected.contentEquals(actual)
    }

    /**
     * 生成HMAC-SHA1密钥,返回字节数组,长度为160位(20字节).
     * HMAC-SHA1算法对密钥无特殊要求, RFC2401建议最少长度为160位(20字节).
     *
     * @return HMAC-SHA1密钥
     * @since 1.0.0
     */
    fun generateHmacSha1Key(): ByteArray {
        val keyGenerator = KeyGenerator.getInstance(HMACSHA1)
        keyGenerator.init(DEFAULT_HMACSHA1_KEYSIZE)
        val secretKey = keyGenerator.generateKey()
        return secretKey.encoded
    }

    //region AES
    /**
     * 使用AES加密原始字符串，返回其十六进制表示
     *
     * @param input    原始输入字符串.
     * @param password 密钥字符串.
     * @return 加密后的字符串(十六进制表示)
     * @since 1.0.0
     */
    fun aesEncrypt(input: String, password: String): String {
        val result = aes(
            input.toByteArray(charset(CHARSET)),
            password.toByteArray(charset(CHARSET)), Cipher.ENCRYPT_MODE
        )
        return aesEncryptBytes(result, password)
    }

    /**
     * 将使用aes加密字节数组进行解密
     *
     * @param content  使用aes加密字节数组
     * @param password 加密时使用的密钥
     * @return 原字符串的字节数组
     * @since 1.0.0
     */
    fun aesDecrypt(content: ByteArray, password: String): ByteArray {
        return aes(
            content, password.toByteArray(charset(CHARSET)),
            Cipher.DECRYPT_MODE
        )
    }

    private fun aes(input: ByteArray, password: ByteArray, mode: Int): ByteArray {
        val generator = KeyGenerator.getInstance(AES)
        val secureRandom = SecureRandom.getInstance("SHA1PRNG")
        secureRandom.setSeed(password)
        generator.init(128, secureRandom)
        val cipher = Cipher.getInstance(AES)
        cipher.init(mode, generator.generateKey())
        return cipher.doFinal(input)
    }

    /**
     * 将使用aes加密并转为十六进制的字符串进行解密
     *
     * @param contentHex 使用aes加密并转为十六进制的字符串
     * @param password   加密时使用的密钥
     * @return 原字符串
     * @since 1.0.0
     */
    fun aesDecrypt(contentHex: String, password: String): String {
        val bytes = aesDecryptBytes(contentHex, password)
        val result = aesDecrypt(bytes, password)
        try {
            return String(result, charset(CHARSET))
        } catch (e: UnsupportedEncodingException) {
            logger.error(e)
        }
        return ""
    }

    /**
     * 使用AES加密原始字符串，返回其十六进制表示
     *
     * @param input 原始输入字符串.
     * @return 加密后的字符串(十六进制表示)
     * @since 1.0.0
     */
    fun aesEncrypt(input: String): String = PREFIX + aesEncrypt(input, KEY)

    /**
     * 将使用aes加密并转为十六进制的字符串进行解密，兼容未加密的历史数据
     *
     * @param contentHex 使用aes加密并转为十六进制的字符串
     * @return 原字符串
     * @since 1.0.0
     */
    fun aesDecrypt(contentHex: String): String {
        return if (contentHex.startsWith(PREFIX)) { // 有加密过的
            val content = contentHex.replaceFirst(PREFIX.toRegex(), "")
            aesDecrypt(content, KEY)
        } else { // 未加密的历史数据
            contentHex
        }
    }

    //endregion

    //endregion
    /**
     *
     *
     * 生成随机向量,默认大小为cipher.getBlockSize(), 16字节.
     *
     *
     * @return 向量的字节数组
     * @since 1.0.0
     */
    fun generateIV(): ByteArray {
        val bytes = ByteArray(DEFAULT_IVSIZE)
        random.nextBytes(bytes)
        return bytes
    }

    /**
     * 将字节数组编码为十六进制表示的字符数组
     *
     * @param data 字节数组
     * @return 十六进制表示的字符数组
     * @since 1.0.0
     */
    fun encodeHex(data: ByteArray): CharArray {
        val l = data.size
        val out = CharArray(l shl 1)
        // two characters form the hex value.
        var i = 0
        var j = 0
        while (i < l) {
            out[j++] = DIGITS[0xF0 and data[i].toInt() ushr 4]
            out[j++] = DIGITS[0x0F and data[i].toInt()]
            i++
        }
        return out
    }

    /**
     * 将十六进制编码的字节数组解码
     *
     * @param bytes 十六进制编码的字节数组
     * @return 解码后的字节数组
     * @since 1.0.0
     */
    fun decodeHex(bytes: ByteArray): ByteArray {
        val iLen = bytes.size
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        val arrOut = ByteArray(iLen / 2)
        var i = 0
        while (i < iLen) {
            val strTmp = String(bytes, i, 2)
            arrOut[i / 2] = strTmp.toInt(16).toByte()
            i += 2
        }
        return arrOut
    }

    /**
     * DES3加密方法，IM提供的代码
     *
     * @param str
     * @param saltTxt
     * @return
     */
    fun encryptDES3(str: String, saltTxt: String): String {
        val md5Key = getMd5(saltTxt) //16bytes
        val key = SecretKeySpec(md5Key, "DESede")
        val ecipher = Cipher.getInstance("DESede/ECB/PKCS5Padding")
        ecipher.init(Cipher.ENCRYPT_MODE, key)
        val data = str.toByteArray(charset(CHARSET))
        val encryptedArray = ecipher.doFinal(data)
        return Base64().encodeToString(encryptedArray)
    }

    /**
     * DES3解密方法，IM提供的代码
     * @param encryptedString
     * @param saltTxt
     * @return
     */
    fun decryptDES3(encryptedString: String, saltTxt: String): String {
        val encryptedArray: ByteArray = Base64.decodeBase64(encryptedString)
        val md5Key = getMd5(saltTxt) //16bytes
        val key = SecretKeySpec(md5Key, "DESede")
        val dcipher = Cipher.getInstance("DESede/ECB/PKCS5Padding")
        dcipher.init(Cipher.DECRYPT_MODE, key)
        val decryptArray = dcipher.doFinal(encryptedArray)
        return String(decryptArray, charset(CHARSET))
    }

    /**
     * MD5加密方法，IM提供的代码
     *
     * @param keyString
     * @return
     */
    fun getMd5(keyString: String): ByteArray {
        val messageDigest: MessageDigest
        val rawKey = ByteArray(24)
        try {
            messageDigest = MessageDigest.getInstance("MD5")
            messageDigest.update(keyString.toByteArray(charset(CHARSET)), 0, keyString.length)
            val md5 = messageDigest.digest()
            System.arraycopy(md5, 0, rawKey, 0, 16)
            System.arraycopy(md5, 0, rawKey, 16, 8)
        } catch (e: Exception) {
            logger.error(e)
        }
        return rawKey
    }

    /**
     * 判断解密方式：转换大小写
     *
     * @param input
     * @param password
     * @return
     */
    private fun aesDecryptBytes(input: String, password: String): ByteArray {
        return if (password.isNotBlank() && password.startsWith(HTB)) {
            EncodeKit.decodeHex(input.toLowerCase())
        } else {
            EncodeKit.decodeHex(input)
        }
    }

    /**
     * 判断加密方式：转换大小写
     *
     * @param bytes
     * @param password
     * @return
     */
    private fun aesEncryptBytes(bytes: ByteArray, password: String): String {
        return if (password.isNotBlank() && password.startsWith(HTB)) {
            EncodeKit.encodeHex(bytes).toUpperCase()
        } else {
            EncodeKit.encodeHex(bytes)
        }
    }

}