package org.kuark.biz.sys.service

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.kuark.test.SpringTest
import org.springframework.beans.factory.annotation.Autowired

internal class SysParamServiceTest : SpringTest() {

    @Autowired
    private lateinit var sysParamService: SysParamService

    @Test
    fun getParamByModuleAndName() {
        assertNull(sysParamService.getParamByModuleAndName("", "name_no_exist"))
    }

}