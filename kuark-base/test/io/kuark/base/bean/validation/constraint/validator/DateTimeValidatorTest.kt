package io.kuark.base.bean.validation.constraint.validator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import io.kuark.base.bean.validation.constraint.annotaions.DateTime
import io.kuark.base.bean.validation.kit.ValidationKit

/**
 * DateTime验证器测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class DateTimeValidatorTest {

    @Test
    fun validate() {
        val bean1 = TestDateTimeBean("2020-08-08", "08:08:08", "2020-08-08 08:08:08")
        assert(ValidationKit.validateBean(bean1, failFast = false).isEmpty())

        val bean2 = TestDateTimeBean("2020/08/08", "080808", "2020/08/08 08:08:08")
        assertEquals(3, ValidationKit.validateBean(bean2, failFast = false).size)
    }

    internal data class TestDateTimeBean(

        @get:DateTime(format = "yyyy-mm-dd")
        val birthday: String?,

        @get:DateTime(format = "hh:mm:ss")
        val schoolTime: String?,

        @get:DateTime(format = "yyyy-mm-dd hh:mm:ss")
        val createTime: String?

    )

}