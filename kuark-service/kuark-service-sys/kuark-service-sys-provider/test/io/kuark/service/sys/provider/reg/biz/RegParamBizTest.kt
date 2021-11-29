package io.kuark.service.sys.provider.reg.biz

import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class RegParamBizTest: SpringTest() {

    @Autowired
    private lateinit var regParamService: io.kuark.service.sys.provider.reg.biz.RegParamBiz

    @Test
    fun getParamByModuleAndName() {
        assertNull(regParamService.getParamByModuleAndName("", "name_no_exist"))
    }

}