package org.kuark.biz.sys.service

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.kuark.test.JunitApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [JunitApplication::class])
internal open class SysParamServiceTest {

    @Autowired
    private lateinit var sysParamService: SysParamService

    @Test
    fun getParamByModuleAndName() {
        assertNull(sysParamService.getParamByModuleAndName("", "name_no_exist"))
    }

}