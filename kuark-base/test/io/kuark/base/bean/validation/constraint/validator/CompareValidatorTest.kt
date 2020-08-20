package io.kuark.base.bean.validation.constraint.validator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import io.kuark.base.bean.validation.constraint.annotaions.Compare
import io.kuark.base.bean.validation.kit.ValidationKit
import io.kuark.base.bean.validation.support.Depends
import io.kuark.base.support.logic.LogicOperator
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
        assertEquals("两次密码不同", violations.first().message)
    }

    /**
     * 测试校验多值比较
     */
    @Test
    fun validateMultiValues() {
        // 两组密码一样
        val bean1 = CompareValuesTestBean(arrayOf("1", "2"), arrayOf("1", "2"))
        assert(ValidationKit.validateBean(bean1).isEmpty())

        // 两组密码存在一样的
        val bean2 = CompareValuesTestBean(arrayOf("1", "2"), arrayOf("1", "3"))
        val violations = ValidationKit.validateBean(bean2)
        assertEquals("两组密码不同", violations.first().message)

        // 数组长度不一致
        val bean3 = CompareValuesTestBean(arrayOf("1", "2"), arrayOf("1"))
        assertThrows<ValidationException> { ValidationKit.validateBean(bean3) }

        // 数组元素不是Compare类型
        val bean4 = CompareValuesTestBean1(arrayOf("1", "2"), arrayOf(arrayOf("1")))
        assertThrows<ValidationException> { ValidationKit.validateBean(bean4) }

        // 不是数组类型
        val bean5 = CompareValuesTestBean2(intArrayOf(1, 2), intArrayOf(1, 2))
        assertThrows<ValidationException> { ValidationKit.validateBean(bean5) }
    }

    /**
     * 测试校验多组Compare约束
     */
    @Test
    fun validateMultiCompares() {
        // 两组Compare约束均通过
        val bean1 = ComparesTestBean("x", "xx", "xxx")
        assert(ValidationKit.validateBean(bean1).isEmpty())

        // 其中一组Compare约束不通过
        val bean2 = ComparesTestBean("x", "xxx", "xx")
        val violations = ValidationKit.validateBean(bean2)
        assertEquals("medium必须小于large", violations.first().message)
    }


    internal data class CompareTestBean(
        val validate: Boolean?,

        val password: String?,

        @get:Compare(
            depends = Depends(
                properties = ["validate"],
                values = ["true"]
            ),
            anotherProperty = "password",
            logic = LogicOperator.EQ,
            message = "两次密码不同"
        )
        val confirmPassword: String?
    )


    internal data class ComparesTestBean(
        val small: String?,

        @get:Compare.List(
            Compare(
                anotherProperty = "small",
                logic = LogicOperator.GT,
                message = "medium必须大于small"
            ),
            Compare(
                anotherProperty = "large",
                logic = LogicOperator.LT,
                message = "medium必须小于large"
            )
        )
        val medium: String?,

        val large: String?
    )


    internal data class CompareValuesTestBean(
        val passwords: Array<String>? = null,

        @get:Compare(
            anotherProperty = "passwords",
            logic = LogicOperator.EQ,
            message = "两组密码不同"
        )
        val confirmPasswords: Array<String>? = null
    ) {
        override fun equals(other: Any?): Boolean = true
        override fun hashCode(): Int = 0
    }


    internal data class CompareValuesTestBean1(
        val passwords: Array<String>? = null,

        @get:Compare(
            anotherProperty = "passwords",
            logic = LogicOperator.EQ,
            message = "两组密码不同"
        )
        val confirmPasswords: Array<Array<String>>? = null
    ) {
        override fun equals(other: Any?): Boolean = true
        override fun hashCode(): Int = 0
    }


    internal data class CompareValuesTestBean2(
        val passwords: IntArray? = null,

        @get:Compare(
            anotherProperty = "passwords",
            logic = LogicOperator.EQ,
            message = "两组密码不同"
        )
        val confirmPasswords: IntArray? = null
    ) {
        override fun equals(other: Any?): Boolean = true
        override fun hashCode(): Int = 0
    }

}