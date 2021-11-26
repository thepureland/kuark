package io.kuark.ability.sys.provider.reg.dao

import io.kuark.ability.sys.common.vo.reg.dict.RegDictSearchPayload
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class RegDictDaoTest: SpringTest() {

    @Autowired
    private lateinit var regDictDao: RegDictDao

    @Test
    fun searchIdsByModuleAndType() {
    }

    @Test
    fun search() {
        val searchModel = RegDictSearchPayload().apply {
            module = "sys"
            dictType = "mon"
        }
        val list = regDictDao.pagingSearch(searchModel)
        assertEquals(12, list.size)
        assertEquals("jan", list.first().itemCode)
        assertEquals("dec", list.last().itemCode)
    }

}