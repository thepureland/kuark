package org.kuark.base.bean.validation.constraint.validator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kuark.base.bean.validation.constraint.annotaions.Constraints
import org.kuark.base.bean.validation.constraint.annotaions.Exist
import org.kuark.base.bean.validation.kit.ValidationKit
import org.kuark.base.bean.validation.support.ConstraintType
import javax.validation.constraints.NotBlank

/**
 * ExistValidator测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class ExistValidatorTest {

    @Test
    fun validate() {
        // 数组类型，某些元素满足规则
        assert(ValidationKit.validateValue(TestExistBean::class, "contactWays", arrayOf("", null, "123")).isEmpty())

        // 数组类型，没有任何一个元素满足规则
        val violations = ValidationKit.validateValue(TestExistBean::class, "contactWays", arrayOf("", null))
        assertEquals("联系方式至少填一种", violations.first().message)

        // 数组类型，返回值为null，直接校验通过
        assert(ValidationKit.validateValue(TestExistBean::class, "contactWays", null).isEmpty())

        // 字符串类型，等效于直接用Constraints约束
        assert(ValidationKit.validateValue(TestExistBean::class, "name", " ").isNotEmpty())
    }

    internal data class TestExistBean(

        @get:Exist(
            Constraints(
                [ConstraintType.NotBlank],
                notBlank = NotBlank(message = "联系方式不能为空"),
            ),
            message = "联系方式至少填一种"
        )
        val contactWays: Array<String>?,

        @get:Exist(
            Constraints([ConstraintType.NotBlank], notBlank = NotBlank(message = "姓名不能为空"))
        )
        val name: String?

    ) {
        override fun equals(other: Any?): Boolean = true
        override fun hashCode(): Int = 0
    }

}