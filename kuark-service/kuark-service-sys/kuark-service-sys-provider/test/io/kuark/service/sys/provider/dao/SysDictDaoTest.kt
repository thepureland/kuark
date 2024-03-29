package io.kuark.service.sys.provider.dao

import io.kuark.service.sys.common.vo.dict.SysDictSearchPayload
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class SysDictDaoTest: SpringTest() {

    @Autowired
    private lateinit var sysDictDao: SysDictDao

    @Test
    fun searchIdsByModuleAndType() {
    }

    @Test
    fun search() {
        val searchModel = SysDictSearchPayload().apply {
            module = "sys"
            dictType = "mon"
        }
        val list = sysDictDao.pagingSearch(searchModel)
        assertEquals(12, list.size)
        assertEquals("jan", list.first().itemCode)
        assertEquals("dec", list.last().itemCode)
    }

}