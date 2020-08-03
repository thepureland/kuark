package org.kuark.base.validation.constraint.validator

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kuark.base.validation.constraint.annotaions.Compare
import org.kuark.base.validation.constraint.annotaions.Depends
import org.kuark.base.validation.kit.ValidationKit
import org.kuark.base.validation.support.CompareLogic
import javax.validation.ValidationException

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
        assert(ValidationKit.validate(bean1).isEmpty())
    }

    /**
     * 测试有Depends且是单值比较的情况
     */
    @Test
    fun validateDependsSingleValue() {
        // Depends约束校验通过且密码相同的情况
        val bean1 = CompareTestBean(true, "123456", "123456")
        assert(ValidationKit.validate(bean1).isEmpty())

        // Depends约束校验通过且密码不同的情况
        val bean2 = CompareTestBean(true, "123456", "123")
        val violations = ValidationKit.validate(bean2)
        assert(violations.isNotEmpty())
        assert(violations.first().message == "两次密码不同")
    }

    /**
     * 测试校验多值比较
     */
    @Test
    fun validateMultiValues() {
        // 两组密码一样
        val bean1 = CompareValuesTestBean(arrayOf("1", "2"), arrayOf("1", "2"))
        assert(ValidationKit.validate(bean1).isEmpty())

        // 两组密码存在一样的
        val bean2 = CompareValuesTestBean(arrayOf("1", "2"), arrayOf("1", "3"))
        val violations = ValidationKit.validate(bean2)
        assert(violations.isNotEmpty())
        assert(violations.first().message == "两组密码不同")

        // 数组长度不一致
        val bean3 = CompareValuesTestBean(arrayOf("1", "2"), arrayOf("1"))
        assertThrows<ValidationException> { ValidationKit.validate(bean3) }

        // 数组元素不是Compare类型
        val bean4 = CompareValuesTestBean1(arrayOf("1", "2"), arrayOf(arrayOf("1")))
        assertThrows<ValidationException> { ValidationKit.validate(bean4) }

        // 不是数组类型
        val bean5 = CompareValuesTestBean2(intArrayOf(1, 2), intArrayOf(1, 2))
        assertThrows<ValidationException> { ValidationKit.validate(bean5) }
    }

    /**
     * 测试校验多组Compare约束
     */
    @Test
    fun validateMultiCompares() {
        // 两组Compare约束均通过
        val bean1 = ComparesTestBean("x", "xx", "xxx")
        assert(ValidationKit.validate(bean1).isEmpty())

        // 其中一组Compare约束不通过
        val bean2 = ComparesTestBean("x", "xxx", "xx")
        val violations = ValidationKit.validate(bean2)
        assert(violations.isNotEmpty())
        assert(violations.first().message == "medium必须小于large")
    }


    data class CompareTestBean(
        val validate: Boolean?,

        val password: String?,

        @get:Compare(
            depends = Depends(property = ["validate"]),
            anotherProperty = "password",
            logic = CompareLogic.EQ,
            message = "两次密码不同"
        )
        val confirmPassword: String?
    )


    data class ComparesTestBean(
        val small: String?,

        @get:Compare.List(
            Compare(
                anotherProperty = "small",
                logic = CompareLogic.GT,
                message = "medium必须大于small"
            ),
            Compare(
                anotherProperty = "large",
                logic = CompareLogic.LT,
                message = "medium必须小于large"
            )
        )
        val medium: String?,

        val large: String?
    )


    data class CompareValuesTestBean(
        val passwords: Array<String>? = null,

        @get:Compare(
            anotherProperty = "passwords",
            logic = CompareLogic.EQ,
            message = "两组密码不同"
        )
        val confirmPasswords: Array<String>? = null
    ) {
        override fun equals(other: Any?): Boolean = true
        override fun hashCode(): Int = 0
    }


    data class CompareValuesTestBean1(
        val passwords: Array<String>? = null,

        @get:Compare(
            anotherProperty = "passwords",
            logic = CompareLogic.EQ,
            message = "两组密码不同"
        )
        val confirmPasswords: Array<Array<String>>? = null
    ) {
        override fun equals(other: Any?): Boolean = true
        override fun hashCode(): Int = 0
    }


    data class CompareValuesTestBean2(
        val passwords: IntArray? = null,

        @get:Compare(
            anotherProperty = "passwords",
            logic = CompareLogic.EQ,
            message = "两组密码不同"
        )
        val confirmPasswords: IntArray? = null
    ) {
        override fun equals(other: Any?): Boolean = true
        override fun hashCode(): Int = 0
    }

}