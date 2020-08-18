package org.kuark.base.bean.validation.constraint.validator

import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.Range
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kuark.base.bean.validation.constraint.annotaions.Constraints
import org.kuark.base.bean.validation.kit.ValidationKit
import org.kuark.base.bean.validation.support.ConstraintType
import org.kuark.base.support.logic.AndOr
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null
import javax.validation.constraints.Pattern

/**
 * ConstraintsValidator测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class ConstraintsValidatorTest {

    @Test
    fun validate() {
        // 测试AndOr.OR，值为null，但因有NotNull约束，校验不通过
        val bean1 = TestConstraintsBean(null)
        assert(ValidationKit.validateProperty(bean1, "captcha").isNotEmpty())

        // 测试AndOr.OR，不满足其中一个规则，校验不通过
        val bean2 = TestConstraintsBean("1234")
        assert(ValidationKit.validateProperty(bean2, "captcha").isNotEmpty())

        // 测试AndOr.OR，不满足另一个规则，校验不通过
        val bean3 = TestConstraintsBean("ABC")
        assert(ValidationKit.validateProperty(bean3, "captcha").isNotEmpty())

        // 测试AndOr.OR，快速失败模式下，校验不通过时的提示应该是哪个规则不通过就提示对应规则的message
        val bean4 = TestConstraintsBean("1234567")
        assertEquals("验证码必须为大写英文字母", ValidationKit.validateProperty(bean4, "captcha").first().message)

        // 测试AndOr.OR，非快速失败模式下，校验不通过时将返回所有失败提示message，但是消息顺序不固定
        val bean5 = TestConstraintsBean("1234567")
        var violations = ValidationKit.validateProperty(bean5, "captcha", failFast = false)
        assertEquals(2, violations.size)

        // 测试AndOr.OR，校验都通过的情况
        val bean6 = TestConstraintsBean("ABCDE")
        assert(ValidationKit.validateProperty(bean6, "captcha").isEmpty())

        // 测试Range约束（实际校验实现Min和Max两个约束组成）
        assert(ValidationKit.validateValue(TestConstraintsBean::class, "age", null).isEmpty())
        assert(ValidationKit.validateValue(TestConstraintsBean::class, "age", 19).isEmpty())
        violations = ValidationKit.validateValue(TestConstraintsBean::class, "age", 17)
        assertEquals("年龄必须在18到60岁之间", violations.first().message)

        // 测试AndOr.OR，其中一个规则成立时
        assert(ValidationKit.validateValue(TestConstraintsBean::class, "name", null).isEmpty())

        // 测试AndOr.OR，另一个规则成立时
        assert(ValidationKit.validateValue(TestConstraintsBean::class, "name", "abc").isEmpty())

        // 测试AndOr.OR，当所有校验都失败时，提示的是Constraints的message信息
        violations = ValidationKit.validateValue(TestConstraintsBean::class, "name", "ABC")
        assertEquals("名称校验不通过", violations.first().message)
    }


    internal data class TestConstraintsBean(

        @get:Constraints(
            [ConstraintType.NotNull, ConstraintType.Pattern, ConstraintType.Length],
            notNull = NotNull(),
            pattern = Pattern(regexp = "[A-Z]+", message = "验证码必须为大写英文字母"),
            length = Length(min = 4, max = 6, message = "验证码位数必须为4到6位")
        )
        val captcha: String?,

        @get:Constraints(
            [ConstraintType.Range],
            range = Range(min = 18, max = 60, message = "年龄必须在18到60岁之间")
        )
        val age: Int? = 19,

        @get:Constraints(
            [ConstraintType.Null, ConstraintType.Pattern],
            andOr = AndOr.OR,
            beNull = Null(),
            pattern = Pattern(regexp = "[a-z]+", message = "名称必须是小写字母"),
            message = "名称校验不通过"
        )
        val name: String? = ""

    )

}