package io.kuark.context.validation

import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class ArgValidAspectTest: SpringTest() {

    @Autowired
    private lateinit var testBean: TestBean

    @Test
    fun test() {
        testBean.test("1", 1, 99)
    }


}