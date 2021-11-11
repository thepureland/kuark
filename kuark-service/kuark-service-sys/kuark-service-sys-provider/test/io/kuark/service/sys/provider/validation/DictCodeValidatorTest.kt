package io.kuark.service.sys.provider.validation

import io.kuark.base.bean.validation.constraint.annotaions.DictCode
import io.kuark.base.bean.validation.kit.ValidationKit
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

internal class DictCodeValidatorTest:SpringTest() {

    @Test
    fun isValid() {
        data class TestData(
            @get:DictCode("sex", "kuark:user")
            val sex: String
        )

        assert(ValidationKit.validateBean(TestData("1")).isEmpty())
        assertFalse(ValidationKit.validateBean(TestData("100")).isEmpty())
    }

}
