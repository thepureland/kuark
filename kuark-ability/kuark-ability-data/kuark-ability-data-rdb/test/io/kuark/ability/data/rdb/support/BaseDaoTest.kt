package io.kuark.ability.data.rdb.support

import io.kuark.ability.data.rdb.table.TestTable
import io.kuark.ability.data.rdb.table.TestTableDao
import io.kuark.ability.data.rdb.table.TestTableKit
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException

/**
 * BaseDao测试用例
 *
 * @author K
 * @since 1.0.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal open class BaseDaoTest : SpringTest() {


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


    //region Insert

    @Test
    @Transactional
    open fun insert() {
        val entity = TestTable {
            id = 0
            name = "name0"
        }
        val id = testTableDao.insert(entity)
        assertEquals(0, id)
        assertEquals(12, testTableDao.searchAll().size)
    }

    @Test
    @Transactional
    open fun batchInsert() {
        val entities = listOf(
            TestTable {
                id = 21
                name = "name21"
            },
            TestTable {
                id = 22
                name = "name22"
            },
            TestTable {
                id = 23
                name = "name23"
            },
        )
        val count = testTableDao.batchInsert(entities, 2)
        assertEquals(3, count)
        assertEquals(14, testTableDao.searchAll().size)
    }

    //endregion Insert


    //region Update

    @Test
    @Transactional
    open fun update() {
        var entity = testTableDao.getById(-1)
        entity.name = "name"
        val success = testTableDao.update(entity)
        assert(success)
        entity = testTableDao.getById(-1)
        assertEquals("name", entity.name)
    }

    @Test
    @Transactional
    open fun batchUpdate() {
        var entities = testTableDao.getByIds(-1, -2, -3)
        entities.forEach {
            it.name = "name"
        }
        val count = testTableDao.batchUpdate(entities)
        assertEquals(3, count)
        entities = testTableDao.getByIds(-1, -2, -3)
        assertEquals("name", entities[0].name)
        assertEquals("name", entities[1].name)
        assertEquals("name", entities[2].name)

        // 存在主键为null的实体
        assertThrows<IllegalStateException> { testTableDao.batchUpdate(listOf(TestTable {})) }
    }

    //endregion Update


    //region Delete

    @Test
    @Transactional
    open fun deleteById() {
        assert(testTableDao.deleteById(-1))
        assertThrows<NoSuchElementException> { testTableDao.getById(-1) }
        assertFalse(testTableDao.deleteById(1))
    }

    @Test
    @Transactional
    open fun delete() {
        val entity = testTableDao.getById(-1)
        assert(testTableDao.delete(entity))
        assertThrows<NoSuchElementException> { testTableDao.getById(-1) }

        // 主键为null
        assertThrows<IllegalStateException> { testTableDao.delete(TestTable {}) }
    }

    //endregion Delete

}