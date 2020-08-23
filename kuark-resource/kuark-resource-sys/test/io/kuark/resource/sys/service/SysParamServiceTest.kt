package io.kuark.resource.sys.service

import io.kuark.test.SpringTest
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class SysParamServiceTest : SpringTest() {

    @Autowired
    private lateinit var sysParamService: SysParamService

    @Test
    fun getParamByModuleAndName() {
        assertNull(sysParamService.getParamByModuleAndName("", "name_no_exist"))
    }

}