package io.kuark.base.io

import org.junit.jupiter.api.Test
import java.io.File

internal class PathKitTest {

    @Test
    fun getClasspathIncludePackage() {
    }

    @Test
    fun getClasspath() {
    }

    @Test
    fun getRelativePath() {
    }

    @Test
    fun getProjectRootPath() {
    }

    @Test
    fun getRuntimePath() {
    }

    @Test
    fun getResourcePath() {
        // resources中
        assert(File(PathKit.getResourcePath("logo.png")).exists())

        // testresources中
        assert(File(PathKit.getResourcePath("TestExcelImporer.xls")).exists())

        // 目录
        assert(File(PathKit.getResourcePath("i18n")).exists())
    }
}