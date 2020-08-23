package io.kuark.base.security

import io.kuark.base.lang.string.EncodeKit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DigestKitTest {

    @Test
    fun getMD5() {
    }

    @Test
    fun testGetMD5() {
    }

    @Test
    fun isMatchMD5() {
    }

    @Test
    fun md5() {
    }

    @Test
    fun sha1() {
        val input = "user"
        val salt = DigestKit.generateSalt(8) // 随机盐

        assertEquals(
            "12dea96fec20593566ab75692c9949596833adc9", EncodeKit.encodeHex(DigestKit.sha1(input.toByteArray()))
        )

        println(EncodeKit.encodeHex(DigestKit.sha1(input.toByteArray(), salt)))
        println(EncodeKit.encodeHex(DigestKit.sha1(input.toByteArray(), salt, 1024)))
    }

    @Test
    fun testSha1() {
    }

    @Test
    fun testSha11() {
    }

    @Test
    fun testSha12() {
    }

    @Test
    fun digest() {
    }

    @Test
    fun generateSalt() {
    }
}