package io.kuark.base.bean.validation.constraint.validator

import io.kuark.base.bean.validation.constraint.annotaions.NotNullOn
import io.kuark.base.bean.validation.kit.ValidationKit
import io.kuark.base.bean.validation.support.Depends
import org.hibernate.validator.constraints.Length
import org.junit.jupiter.api.Test

/**
 * NotNullOnValidator测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class NotNullOnValidatorTest {

    @Test
    fun validate() {
        // 表达式不成立，可以为Null，值为Null时，属性上的其它注解无须再作校验
        val bean1 = TestNotNullOnBean(false, null)
        assert(ValidationKit.validateBean(bean1).isEmpty())

        // 表达式不成立，可以为Null，但此时值没有为Null，属性上的其他注解仍需一一校验，不通过的情况
        val bean2 = TestNotNullOnBean(false, "ABCDEF")
        assert(ValidationKit.validateBean(bean2).isNotEmpty())

        // 表达式不成立，可以为Null，但此时值没有为Null，属性上的其他注解仍需一一校验，通过的情况
        val bean3 = TestNotNullOnBean(false, "ABCD")
        assert(ValidationKit.validateBean(bean3).isEmpty())

        // 表达式成立，不可以为Null，但此时值为Null，校验失败
        val bean4 = TestNotNullOnBean(true, null)
        assert(ValidationKit.validateBean(bean4).isNotEmpty())

        // 表达式成立，不可以为Null，但此时值不为Null，属性上的其他注解仍需一一校验，不通过的情况
        val bean5 = TestNotNullOnBean(true, "ABCDEF")
        assert(ValidationKit.validateBean(bean5).isNotEmpty())

        // 表达式成立，不可以为Null，但此时值不为Null，属性上的其他注解仍需一一校验，通过的情况
        val bean6 = TestNotNullOnBean(true, "ABCD")
        assert(ValidationKit.validateBean(bean6).isEmpty())
    }


    internal data class TestNotNullOnBean(

        val validate: Boolean?,

        @get:NotNullOn(Depends(properties = ["validate"], values = ["true"]))
        @get:Length(min = 4, max = 4, message = "验证码位数必须为4")
        val captcha: String?

    )

}