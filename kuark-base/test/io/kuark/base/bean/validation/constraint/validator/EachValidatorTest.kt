package io.kuark.base.bean.validation.constraint.validator

import io.kuark.base.bean.validation.constraint.annotaions.Constraints
import io.kuark.base.bean.validation.constraint.annotaions.Each
import io.kuark.base.bean.validation.kit.ValidationKit
import org.hibernate.validator.constraints.Length
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.validation.constraints.NotBlank

/**
 * EachValidator测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class EachValidatorTest {

    @Test
    fun validate() {
        // 数组类型，存在某些元素对于其中一个规则不满足
        var violations = ValidationKit.validateValue(TestEachBean::class, "contactWays", arrayOf("", null, "123"))
        assertEquals("联系方式都不能为空", violations.first().message)

        // 数组类型，存在某些元素对于另一个规则不满足
        violations = ValidationKit.validateValue(TestEachBean::class, "contactWays", arrayOf("123"))
        assertEquals("联系方式的长度都必须在8到32之间", violations.first().message)

        // 数组类型，返回值为null，直接校验通过
        assert(ValidationKit.validateValue(TestEachBean::class, "contactWays", null).isEmpty())

        // 数组类型，每个元素都满足所有规则
        assert(ValidationKit.validateValue(TestEachBean::class, "contactWays", arrayOf("12345678", "abcdefghi")).isEmpty())

        // 字符串类型，等效于直接用Constraints约束
        assert(ValidationKit.validateValue(TestEachBean::class, "name", " ").isNotEmpty())
    }

    internal data class TestEachBean(

        @get:Each(
            Constraints(
                notBlank = NotBlank(message = "联系方式都不能为空"),
                length = Length(min = 8, max = 32, message = "联系方式的长度都必须在8到32之间")
            )
        )
        val contactWays: Array<String>?,

        @get:Each(
            Constraints(notBlank = NotBlank(message = "姓名不能为空"))
        )
        val name: String?


    ) {
        override fun equals(other: Any?): Boolean = true
        override fun hashCode(): Int = 0
    }

}