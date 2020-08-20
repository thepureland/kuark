package io.kuark.base.bean.validation.constraint.validator

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import io.kuark.base.bean.validation.constraint.annotaions.AtLeast
import io.kuark.base.bean.validation.kit.ValidationKit
import javax.validation.ValidationException

/**
 * AtLeastValidator测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class AtLeastValidatorTest {

    /**
     * 测试参数非法情况
     */
    @Test
    fun testIllegalArguments() {
        val bean1 = TestIllegalArgumentsBean("")
        assertThrows<ValidationException> { ValidationKit.validateBean(bean1) }
    }

    /**
     * 测试单个AtLeast
     */
    @Test
    fun testAtLeast() {
        // 有2个不为null，通过
        val bean1 = TestAtLeastBean("1", "2", null, null)
        assert(ValidationKit.validateBean(bean1).isEmpty())

        // 有3个不为null，通过
        val bean2 = TestAtLeastBean("1", "2", "3", null)
        assert(ValidationKit.validateBean(bean2).isEmpty())

        // 只有1个不为null，失败
        val bean3 = TestAtLeastBean("1", null, null, null)
        assert(ValidationKit.validateBean(bean3).isNotEmpty())
    }

    /**
     * 测试多个AtLeast
     */
    @Test
    fun testAtLeastList() {
        val bean1 = TestAtLeastListBean("1", null, null, "4")
        assert(ValidationKit.validateBean(bean1).isEmpty())
    }

    @AtLeast(properties = ["p1", "p2", "p3"], count = 4, message = "p1、p2、p3三个至少四个不能为null")
    internal data class TestIllegalArgumentsBean(
        val p: String
    )

    @AtLeast(properties = ["p1", "p2", "p3"], count = 2, message = "p1、p2、p3三个至少两个不能为null")
    internal data class TestAtLeastBean(

        val p1: String?,

        val p2: String?,

        val p3: String?,

        val p4: String?

    )

    @AtLeast.List(
        AtLeast(properties = ["p1", "p2"], message = "p1、p2两个至少一个不能为null"),
        AtLeast(properties = ["p3", "p4"], message = "p3、p4两个至少一个不能为null")
    )
    internal data class TestAtLeastListBean(

        val p1: String?,

        val p2: String?,

        val p3: String?,

        val p4: String?

    )

}