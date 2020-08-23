package io.kuark.base.security

import io.kuark.base.lang.string.EncodeKit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CryptoKitTest {

    @Test
    fun hmacSha1() {
    }

    @Test
    fun isMacValid() {
        val input = "foo message"

        //key可为任意字符串
        //byte[] key = "a foo key".getBytes();

        //key可为任意字符串
        //byte[] key = "a foo key".getBytes();
        val key = CryptoKit.generateHmacSha1Key()
        assertEquals(20, key.size.toLong())

        val macResult = CryptoKit.hmacSha1(input.toByteArray(), key)
        println("hmac-sha1 key in hex      :" + EncodeKit.encodeHex(key))
        println("hmac-sha1 in hex result   :" + EncodeKit.encodeHex(macResult))

        assert(CryptoKit.isMacValid(macResult, input.toByteArray(), key))
    }

    @Test
    fun generateHmacSha1Key() {
    }

    @Test
    fun aesEncrypt() {
    }

    @Test
    fun aesDecrypt() {
    }

    @Test
    fun testAesDecrypt() {
    }

    @Test
    fun testAesEncrypt() {
    }

    @Test
    fun testAesDecrypt1() {
    }

    @Test
    fun generateIV() {
    }

    @Test
    fun encodeHex() {
    }

    @Test
    fun decodeHex() {
    }

    @Test
    fun encryptDES3() {
    }

    @Test
    fun decryptDES3() {
    }

    @Test
    fun getMd5() {
    }
}