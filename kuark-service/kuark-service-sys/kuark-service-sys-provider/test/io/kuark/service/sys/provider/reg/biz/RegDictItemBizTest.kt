package io.kuark.service.sys.provider.reg.biz

import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class RegDictItemBizTest : SpringTest() {

    @Autowired
    private lateinit var regDictItemBiz: io.kuark.service.sys.provider.reg.ibiz.IRegDictItemBiz

    @Test
    fun getItemsByModuleAndType() {
        assertTrue(regDictItemBiz.getItemsByModuleAndType("kuark", "sex").size >= 2)
        assertTrue(regDictItemBiz.getItemsByModuleAndType("", "sex").size >= 2)
        assertTrue(regDictItemBiz.getItemsByModuleAndType("", "sex_no_exist").isEmpty())

//        val result = regDictItemService.testBatchCache("", listOf("1", "2"), arrayOf("3"))
//        print(result)

    }

//    @BatchCacheable(valueClass = List::class)
//    open fun testBatchCache(subSys: String, modules: List<String>, types: Array<String>): Map<String, List<SysDictItem>> {
//        return mapOf("1" to listOf(), "2" to listOf())
//    }

}