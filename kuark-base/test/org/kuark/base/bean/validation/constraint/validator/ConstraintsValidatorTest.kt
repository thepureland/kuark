package org.kuark.base.bean.validation.constraint.validator

import org.hibernate.validator.constraints.Length
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kuark.base.bean.validation.constraint.annotaions.Constraints
import org.kuark.base.bean.validation.kit.ValidationKit
import org.kuark.base.bean.validation.support.ConstraintType
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

internal class ConstraintsValidatorTest {

    @Test
    fun validate() {
        val bean1 = TestConstraintsBean(null)
        assert(ValidationKit.validateBean(bean1).isNotEmpty())

        val bean2 = TestConstraintsBean("1234")
        assert(ValidationKit.validateBean(bean2).isNotEmpty())

        val bean3 = TestConstraintsBean("ABC")
        assert(ValidationKit.validateBean(bean3).isNotEmpty())

        val bean4 = TestConstraintsBean("1234567")
        assertEquals("验证码必须为大写英文字母", ValidationKit.validateBean(bean4).first().message)

        val bean5 = TestConstraintsBean("ABCDE")
        assert(ValidationKit.validateBean(bean5).isEmpty())

        val bean6 = TestConstraintsBean("1234567")
        val violations = ValidationKit.validateBean(bean6, failFast = false)
        assertEquals(2, violations.size)
    }


    internal data class TestConstraintsBean(

        @get:Constraints(
            [ConstraintType.NotNull, ConstraintType.Pattern, ConstraintType.Length],
            notNull = NotNull(),
            pattern = Pattern(regexp = "[A-Z]+", message = "验证码必须为大写英文字母"),
            length = Length(min = 4, max = 6, message = "验证码位数必须为4到6位")
        )
        val captcha: String?

    )

}