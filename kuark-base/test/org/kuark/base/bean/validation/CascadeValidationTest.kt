package org.kuark.base.bean.validation

import junit.framework.Assert.assertEquals
import org.hibernate.validator.constraints.Length
import org.junit.jupiter.api.Test
import org.kuark.base.bean.validation.kit.ValidationKit
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

/**
 * 级联校验测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class CascadeValidationTest {

    @Test
    fun testCascade() {
        val bean1 = TestCascadeBean("123", TestBean(61), listOf(TestBean(17), TestBean(null)))
        val violations = ValidationKit.validateBean(bean1, failFast = false)
        assertEquals(5, violations.size)
    }

    internal data class TestCascadeBean(

        @get:Length(min = 6, max = 32, message = "name长度必须在6到32之间")
        @get:Pattern(regexp = "[a-zA-Z]+", message = "name必须为字母")
        val name: String?,

        @get:Valid
        @get:NotNull
        val testBean: TestBean,

        @get:Valid
        val testBeans: List<TestBean>

    )

    internal data class TestBean(

        @get:NotNull
        @get:Max(60, message = "必须60岁以下")
        @get:Min(18, message = "必须满18岁")
        val age: Int?

    )

}