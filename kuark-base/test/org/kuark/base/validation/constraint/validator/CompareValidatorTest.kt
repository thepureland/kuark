package org.kuark.base.validation.constraint.validator

import org.junit.jupiter.api.Test
import org.kuark.base.validation.constraint.annotaions.Compare
import org.kuark.base.validation.constraint.annotaions.Depends
import org.kuark.base.validation.kit.ValidationKit
import org.kuark.base.validation.support.CompareLogic

/**
 * Compare约束验证器测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class CompareValidatorTest {

    /**
     * 测试有Depends的情况
     */
    @Test
    fun validateDepends() {
        // Depends约束校验不通过，无须进一步校验Compare约束
        val bean1 = CompareTestBean(null, "123456", "123")
        assert(ValidationKit.validateBean(bean1).isEmpty())
    }

    /**
     * 测试有Depends且是单值比较的情况
     */
    @Test
    fun validateDependsSingleValue() {
        // Depends约束校验通过且密码相同的情况
        val bean1 = CompareTestBean(true, "123456", "123456")
        assert(ValidationKit.validateBean(bean1).isEmpty())

        // Depends约束校验通过且密码不同的情况
        val bean2 = CompareTestBean(true, "123456", "123")
        val violations = ValidationKit.validateBean(bean2)
        assert(violations.isNotEmpty())
        assert(violations.first().message == "两次密码不同")
    }

    /**
     * 测试校验多值比较
     */
    @Test
    fun validateMultiValues() {
        // 两组密码一样
        val bean1 = ComparesTestBean(arrayOf("1", "2"), arrayOf("1", "2"))
        assert(ValidationKit.validateBean(bean1).isEmpty())

        // 两组密码存在一样的
        val bean2 = ComparesTestBean(arrayOf("1", "2"), arrayOf("1", "3"))
        assert(ValidationKit.validateBean(bean2).isNotEmpty())
    }

    @Compare(
        depends = Depends(property = ["validate"]),
        firstProperty = "password",
        secondProperty = "confirmPassword",
        logic = CompareLogic.EQ,
        message = "两次密码不同"
    )
    data class CompareTestBean(
        val validate: Boolean?,
        val password: String?,
        val confirmPassword: String?
    )

    @Compare(
        firstProperty = "passwords",
        secondProperty = "confirmPasswords",
        logic = CompareLogic.EQ,
        message = "两组密码不同"
    )
    data class ComparesTestBean(
        val passwords: Array<String>? = null,
        val confirmPasswords: Array<String>? = null
    )

}