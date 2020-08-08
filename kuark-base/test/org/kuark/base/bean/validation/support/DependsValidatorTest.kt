package org.kuark.base.bean.validation.support

import org.junit.jupiter.api.Test

/**
 *
 */
internal class DependsValidatorTest {

    @Test
    fun testValidate() {


    }

    data class DependsTestBean1(

        val validate: Boolean,
        val name: String?
    )

}