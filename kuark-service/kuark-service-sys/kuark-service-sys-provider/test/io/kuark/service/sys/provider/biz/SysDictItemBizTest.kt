package io.kuark.service.sys.provider.biz

import io.kuark.service.sys.provider.biz.ibiz.ISysDictItemBiz
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class SysDictItemBizTest : SpringTest() {

    @Autowired
    private lateinit var sysDictItemBiz: ISysDictItemBiz

    @Test
    fun getItemsByModuleAndType() {
        assertTrue(sysDictItemBiz.getItemsByModuleAndType("kuark", "sex").size >= 2)
        assertTrue(sysDictItemBiz.getItemsByModuleAndType("", "sex").size >= 2)
        assertTrue(sysDictItemBiz.getItemsByModuleAndType("", "sex_no_exist").isEmpty())

//        val result = regDictItemService.testBatchCache("", listOf("1", "2"), arrayOf("3"))
//        print(result)

    }

//    @BatchCacheable(valueClass = List::class)
//    open fun testBatchCache(subSys: String, modules: List<String>, types: Array<String>): Map<String, List<SysDictItem>> {
//        return mapOf("1" to listOf(), "2" to listOf())
//    }

}