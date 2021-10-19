package io.kuark.base.lang

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SystemKitTest {

    @Test
    fun setEnvVars() {
        SystemKit.setEnvVars(mapOf("var" to "val"))
        assertEquals("val", System.getenv("var"))
    }

    @Test
    fun executeCommand() {
        SystemKit.executeCommand("java", "-version")
        if (SystemKit.isWindowsOS()) {
//TODO kotlin 1.5.10 捕获不到该 IOException
//            assertThrows<IOException> { SystemKit.executeCommand("cmd /c ping www.baidu.com") } // 不能合着写
            assert(SystemKit.executeCommand("cmd", "/c", "ping www.baidu.com").first)
        } else {
            assert(SystemKit.executeCommand("/bin/sh", "c", "ls -l").first)
        }
    }


    @Test
    fun getLINE_SEPARATOR() {
    }

    @Test
    fun getOSName() {
    }

    @Test
    fun isDebug() {
    }

    @Test
    fun isWindowsOs() {
    }

    @Test
    fun getUser() {
    }

    @Test
    fun getJavaHome() {
    }

    @Test
    fun getJavaIoTmpDir() {
    }

    @Test
    fun getUserDir() {
    }

    @Test
    fun getUserHome() {
    }

    @Test
    fun isJavaAwtHeadless() {
    }
}