package org.kuark.base.bean.validation.constraint.validator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.kuark.base.bean.validation.constraint.annotaions.Constraint
import org.kuark.base.bean.validation.kit.ValidationKit
import org.kuark.base.bean.validation.support.IBeanValidator

/**
 * Constraint约束验证器测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class ConstraintValidatorTest {

    @Test
    fun validate() {
        val bean1 = TestRemoteBean("user1", false, null)
        assert(ValidationKit.validateBean(bean1).isEmpty())

        val bean2 = TestRemoteBean("user2", true, null)
        assertFalse(ValidationKit.validateBean(bean2).isEmpty())

        val bean3 = TestRemoteBean(null, true, "address")
        assertEquals(1, ValidationKit.validateBean(bean3, failFast = false).size)
    }

    internal data class TestRemoteBean(

        @get:Constraint(checkClass = ExistValidator::class, message = "用户名已存在")
        val username: String?,

        val mockExist: Boolean = false, // 模拟用户名是否存在

        @get:Constraint.List(
            Constraint(checkClass = Rule1Validator::class, message = "不满足规则1"),
            Constraint(checkClass = Rule2Validator::class, message = "不满足规则2")
        )
        val address: String?
    )

    internal class ExistValidator : IBeanValidator<TestRemoteBean> {

        override fun validate(bean: TestRemoteBean): Boolean {
            return !bean.mockExist
        }

    }

    internal class Rule1Validator : IBeanValidator<TestRemoteBean> {

        override fun validate(bean: TestRemoteBean): Boolean {
            return true
        }

    }

    internal class Rule2Validator : IBeanValidator<TestRemoteBean> {

        override fun validate(bean: TestRemoteBean): Boolean {
            return false
        }

    }

}