package org.kuark.base.bean.validation.constraint.validator

import org.junit.jupiter.api.Test
import org.kuark.base.bean.validation.constraint.annotaions.CnIdCardNo
import org.kuark.base.bean.validation.kit.ValidationKit

internal class CnIdCardNoValidatorTest {

    @Test
    fun validate() {
        val bean1 = TestCnIdCardNoBean("210502198412020944", "210502841202094")
        assert(ValidationKit.validateProperty(bean1, "idCardNo1").isEmpty())
        assert(ValidationKit.validateProperty(bean1, "idCardNo2").isEmpty())

        val bean2 = TestCnIdCardNoBean("210502841202094", "210502198412020940")
        assert(ValidationKit.validateProperty(bean2, "idCardNo1").isNotEmpty())
        assert(ValidationKit.validateProperty(bean2, "idCardNo2").isNotEmpty())
    }

    internal data class TestCnIdCardNoBean(

        @get:CnIdCardNo
        val idCardNo1: String?,

        @get:CnIdCardNo(support15 = true)
        val idCardNo2: String?

    )

}