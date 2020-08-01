package org.kuark.biz.sys.service

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.kuark.test.SpringTest
import org.springframework.beans.factory.annotation.Autowired

internal class SysDictItemServiceTest : SpringTest() {

    @Autowired
    private lateinit var sysDictItemService: SysDictItemService

    @Test
    fun getItemsByModuleAndType() {
        assertTrue(sysDictItemService.getItemsByModuleAndType("kuark", "sex").size >= 2)
        assertTrue(sysDictItemService.getItemsByModuleAndType("", "sex").size >= 2)
        assertTrue(sysDictItemService.getItemsByModuleAndType("", "sex_no_exist").isEmpty())

        val result = sysDictItemService.testBatchCache("", listOf("1", "2"), arrayOf("3"))
        print(result)

    }

}