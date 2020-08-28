package io.kuark.base.data.xls

import io.kuark.base.io.PathKit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import javax.validation.constraints.NotNull

/**
 * AbstractExcelImporter测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class AbstractExcelImporterTest {

    @Test
    fun import() {
        val path = PathKit.getProjectRootPath() + "/build/resources/test/TestExcelImporer.xls"

        // 行对象类为数据类方式
        assertEquals(3, TestStudentExcelImporter().import(File(path)).size)

        // 行对象类为普通类方式
        assertEquals(3, TestPersonExcelImporter().import(File(path)).size)
    }

    private class TestStudentExcelImporter : AbstractExcelImporter<TestStudent>() {

        override fun getPropertyNames(): List<String> = listOf("name", "sex", "age", "height")

        override fun getSheetName(): String = "students"

        override fun save(rowObjects: List<TestStudent>) {
            // 模拟保存数据到DB
        }

    }

    internal data class TestStudent(

        @get:NotNull
        val height: Double,

        @get:NotNull
        val name: String,

        @get:NotNull
        val sex: String,

        @get:NotNull
        val age: Int

    )

    private class TestPersonExcelImporter : AbstractExcelImporter<TestPerson>() {

        override fun getPropertyNames(): List<String> = listOf("name", "sex", "age", "height")

        override fun getSheetName(): String = "students"

        override fun save(rowObjects: List<TestPerson>) {
            // 模拟保存数据到DB
        }

    }

    internal class TestPerson {

        @get:NotNull
        var name: String? = null

        @get:NotNull
        var sex: String? = null

        @get:NotNull
        var age: Int? = null

        @get:NotNull
        var height: Double? = null

    }

}