package io.kuark.service.sys.provider.biz

import io.kuark.service.sys.provider.biz.impl.SysParamBiz
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class SysParamBizTest: SpringTest() {

    @Autowired
    private lateinit var sysParamService: SysParamBiz

    @Test
    fun getParamByModuleAndName() {
        assertNull(sysParamService.getParamFromCache("", "name_no_exist"))
    }

}