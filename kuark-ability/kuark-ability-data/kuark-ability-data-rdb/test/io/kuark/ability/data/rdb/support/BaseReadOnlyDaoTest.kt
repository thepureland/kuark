package io.kuark.ability.data.rdb.support

import io.kuark.ability.data.rdb.table.TestTableDao
import io.kuark.ability.data.rdb.table.TestTableKit
import io.kuark.test.SpringTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired

/**
 * BaseReadOnlyDao测试用例
 *
 * @author K
 * @since 1.0.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BaseReadOnlyDaoTest: SpringTest() {

    @Autowired
    private lateinit var testTableDao: TestTableDao

    @BeforeAll
    fun setUp() {
        TestTableKit.create()
        TestTableKit.insert()
    }

    @AfterAll
    fun tearDown() {
        TestTableKit.drop()
    }


    @Test
    fun table() {
        testTableDao.table()
    }

    @Test
    fun querySource() {
        testTableDao.querySource()
    }

    @Test
    fun entitySequence() {
        testTableDao.entitySequence()
    }

    @Test
    fun getById() {
        val entity = testTableDao.getById(-1)
        assertEquals("name1", entity.name)

        // 不存在指定主键对应的实体时
        org.junit.jupiter.api.assertThrows<NoSuchElementException> { testTableDao.getById(1) }
    }

    @Test
    fun getByIds() {
        assert(testTableDao.getByIds().isEmpty())

        var entities = testTableDao.getByIds(-1, -2, -3)
        assertEquals(3, entities.size)

        entities = testTableDao.getByIds(-1, -2, -3, countOfEachBatch = 2)
        assertEquals(3, entities.size)
    }

    @Test
    fun searchAll() {
        val result = testTableDao.searchAll()
        assertEquals(11, result.size)
    }

    @Test
    fun countAll() {
        assertEquals(11, testTableDao.countAll())
    }

}