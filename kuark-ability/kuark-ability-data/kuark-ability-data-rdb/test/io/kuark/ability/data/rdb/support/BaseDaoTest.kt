package io.kuark.ability.data.rdb.support

import io.kuark.ability.data.rdb.table.TestTable
import io.kuark.ability.data.rdb.table.TestTableDao
import io.kuark.ability.data.rdb.table.TestTableKit
import io.kuark.ability.data.rdb.table.TestTables
import io.kuark.base.query.Criteria
import io.kuark.base.query.enums.Operator
import io.kuark.base.support.payload.SearchPayload
import io.kuark.base.support.payload.UpdatePayload
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.ktorm.dsl.eq
import org.ktorm.dsl.like
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

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
        assertEquals(12, testTableDao.allSearch().size)
    }

    @Test
    @Transactional
    open fun insertOnly() {
        val entity = TestTable {
            id = 0
            name = "name0"
            weight = 70.0
            height = 175
        }
        val id = testTableDao.insertOnly(entity, TestTable::id.name, TestTable::name.name)
        assertEquals(0, id)
        val result = testTableDao.getById(0)!!
        assert(result.weight == null)
        assert(result.height == null)
    }

    @Test
    @Transactional
    open fun insertExclude() {
        val entity = TestTable {
            id = 0
            name = "name0"
            weight = 70.0
            height = 175
        }
        val id = testTableDao.insertExclude(entity, TestTable::weight.name, TestTable::height.name)
        assertEquals(0, id)
        val result = testTableDao.getById(0)!!
        assert(result.weight == null)
        assert(result.height == null)
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
        assertEquals(14, testTableDao.allSearch().size)
    }

    @Test
    @Transactional
    open fun batchInsertOnly() {
        val entities = listOf(
            TestTable {
                id = 21
                name = "name21"
                weight = 70.0
                height = 175
            },
            TestTable {
                id = 22
                name = "name22"
                weight = 70.0
                height = 175
            },
            TestTable {
                id = 23
                name = "name23"
                weight = 70.0
                height = 175
            },
        )
        val count = testTableDao.batchInsertOnly(entities, 4, TestTable::id.name, TestTable::name.name)
        assertEquals(3, count)
        assertEquals(14, testTableDao.allSearch().size)
        val result = testTableDao.getById(21)!!
        assert(result.weight == null)
        assert(result.height == null)
    }

    @Test
    @Transactional
    open fun batchInsertExclude() {
        val entities = listOf(
            TestTable {
                id = 21
                name = "name21"
                weight = 70.0
                height = 175
            },
            TestTable {
                id = 22
                name = "name22"
                weight = 70.0
                height = 175
            },
            TestTable {
                id = 23
                name = "name23"
                weight = 70.0
                height = 175
            },
        )
        val count = testTableDao.batchInsertExclude(entities, 4, TestTable::weight.name, TestTable::height.name)
        assertEquals(3, count)
        assertEquals(14, testTableDao.allSearch().size)
        val result = testTableDao.getById(21)!!
        assert(result.weight == null)
        assert(result.height == null)
    }
    //endregion Insert


    //region Update
    @Test
    @Transactional
    open fun update() {
        var entity = testTableDao.getById(-1)!!
        entity.name = "name"
        val success = testTableDao.update(entity)
        assert(success)
        entity = testTableDao.getById(-1)!!
        assertEquals("name", entity.name)
    }

    @Test
    @Transactional
    open fun updateWhen() {
        var entity = testTableDao.getById(-1)!!
        entity.name = "name"

        // 满足Criteria条件
        var criteria = Criteria.add(TestTable::name.name, Operator.EQ, "name1")
        assert(testTableDao.updateWhen(entity, criteria))
        entity = testTableDao.getById(-1)!!
        assertEquals("name", entity.name)

        // 不满足Criteria条件
        entity.name = "name1"
        criteria = Criteria.add(TestTable::name.name, Operator.EQ, "non-exists")
        assert(!testTableDao.updateWhen(entity, criteria))
        entity = testTableDao.getById(-1)!!
        assertEquals("name", entity.name)
    }

    @Test
    @Transactional
    open fun updateProperties() {
        val properties = mapOf(TestTable::name.name to -2, TestTable::name.name to "new-name") // 主键应该要不会被更新
        assert(testTableDao.updateProperties(-1, properties))
        assertEquals("new-name", testTableDao.getById(-1)!!.name)
        assertEquals("name2", testTableDao.getById(-2)!!.name)
    }

    @Test
    @Transactional
    open fun updatePropertiesWhen() {
        // 满足Criteria条件
        var criteria = Criteria.add(TestTable::name.name, Operator.EQ, "name1")
        val properties = mapOf(TestTable::id.name to -2, TestTable::name.name to "name") // 主键应该要不会被更新
        assert(testTableDao.updatePropertiesWhen(-1, properties, criteria))
        var entity = testTableDao.getById(-1)!!
        assertEquals("name", entity.name)

        // 不满足Criteria条件
        criteria = Criteria.add(TestTable::name.name, Operator.EQ, "non-exists")
        assert(!testTableDao.updatePropertiesWhen(-1, mapOf(TestTable::name.name to "name1"), criteria))
        entity = testTableDao.getById(-1)!!
        assertEquals("name", entity.name)
    }

    @Test
    @Transactional
    open fun updateOnly() {
        val entity = testTableDao.getById(-1)!!
        entity.name = "new-name"
        assert(testTableDao.updateOnly(entity, TestTable::name.name))
        assertEquals("new-name", testTableDao.getById(-1)!!.name)
        assertEquals("name2", testTableDao.getById(-2)!!.name)
    }

    @Test
    @Transactional
    open fun updateOnlyWhen() {
        var entity = testTableDao.getById(-1)!!
        entity.name = "name"
        entity.weight = null

        // 满足Criteria条件
        var criteria = Criteria.add(TestTable::name.name, Operator.EQ, "name1")
        assert(testTableDao.updateOnlyWhen(entity, criteria, TestTable::name.name))
        entity = testTableDao.getById(-1)!!
        assertEquals("name", entity.name)
        assertEquals(56.5, entity.weight)

        // 不满足Criteria条件
        entity.name = "name1"
        criteria = Criteria.add(TestTable::name.name, Operator.EQ, "non-exists")
        assert(!testTableDao.updateOnlyWhen(entity, criteria, TestTable::name.name))
        entity = testTableDao.getById(-1)!!
        assertEquals("name", entity.name)
    }

    @Test
    @Transactional
    open fun updateExcludeProperties() {
        var entity = testTableDao.getById(-1)!!
        entity.name = "name"
        entity.weight = null
        assert(testTableDao.updateExcludeProperties(entity, TestTable::weight.name))
        entity = testTableDao.getById(-1)!!
        assertEquals("name", entity.name)
        assertEquals(56.5, entity.weight)
    }

    @Test
    @Transactional
    open fun updateExcludePropertiesWhen() {
        var entity = testTableDao.getById(-1)!!
        entity.name = "name"
        entity.weight = null

        // 满足Criteria条件
        var criteria = Criteria.add(TestTable::name.name, Operator.EQ, "name1")
        assert(testTableDao.updateExcludePropertiesWhen(entity, criteria, TestTable::weight.name))
        entity = testTableDao.getById(-1)!!
        assertEquals("name", entity.name)
        assertEquals(56.5, entity.weight)

        // 不满足Criteria条件
        entity.name = "name1"
        criteria = Criteria.add(TestTable::name.name, Operator.EQ, "non-exists")
        assert(!testTableDao.updateExcludePropertiesWhen(entity, criteria))
        entity = testTableDao.getById(-1)!!
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
        assertThrows<IllegalArgumentException> { testTableDao.batchUpdate(listOf(TestTable {})) }
    }

    @Test
    @Transactional
    open fun batchUpdateWhen() {
        val entities = listOf(
            TestTable {
                id = -1
                name = "11"
            },
            TestTable {
                id = -2
                name = "12"
            }
        )

        // 满足Criteria条件
        var criteria = Criteria.add(TestTable::name.name, Operator.EQ, "unexists")
        assertEquals(0, testTableDao.batchUpdateWhen(entities, criteria))

        // 不满足Criteria条件
        criteria = Criteria.add("name", Operator.EQ, "name2")
        assertEquals(1, testTableDao.batchUpdateWhen(entities, criteria))
    }

    @Test
    @Transactional
    open fun batchUpdateWhenByUpdatePayload() {
        class SearchPayload1 : SearchPayload() {
            var name: String? = null
            var weight: Double? = null
            var noExistProp: String? = "noExistProp"
            override var returnProperties: List<String>? = listOf("id", "name", "height")
        }

        class UpdatePayload1 : UpdatePayload<SearchPayload1>() {
            var name: String? = null
            var birthday: LocalDateTime? = null
        }

        val updatePayload1 = UpdatePayload1().apply {
            name = "name"
            nullProperties = listOf("weight")
        }


        // 无条件
        assertThrows<IllegalArgumentException> {
            testTableDao.batchUpdateWhen(updatePayload1)
        }
        val searchPayload1 = SearchPayload1()
        updatePayload1.searchPayload = searchPayload1
        assertThrows<IllegalArgumentException> {
            testTableDao.batchUpdateWhen(updatePayload1)
        }

        // 有指定nullProperties的值
        var count = testTableDao.batchUpdateWhen(updatePayload1) { column, _ ->
            if (column.name == TestTables.name.name) {
                column.ieq("nAme1")
            } else {
                null
            }
        }
        assertEquals(1, count)
        var entity = testTableDao.getById(-1)!!
        assertEquals("name", entity.name)
        assert(entity.birthday != null)
        assertEquals(null, entity.weight)

        // 未指定nullProperties的值
        searchPayload1.name = "name2"
        updatePayload1.nullProperties = null
        count = testTableDao.batchUpdateWhen(updatePayload1)
        assertEquals(1, count)
        entity = testTableDao.getById(-1)!!
        assertEquals("name", entity.name)
        assert(entity.birthday != null)
    }

    @Test
    @Transactional
    open fun batchUpdateProperties() {
        val criteria = Criteria.add(TestTable::name.name, Operator.LIKE, "name1")
        val properties = mapOf(TestTable::active.name to false, TestTable::height.name to null)
        assertEquals(3, testTableDao.batchUpdateProperties(criteria, properties))
        criteria.addAnd(TestTable::active.name, Operator.EQ, false)
        criteria.addAnd(TestTable::height.name, Operator.IS_NULL, null)
        assertEquals(3, testTableDao.count(criteria))
    }

    @Test
    @Transactional
    open fun batchUpdateOnly() {
        val entities = listOf(
            TestTable {
                id = -1
                name = "11"
                height = 0
                weight = 0.0
            },
            TestTable {
                id = -2
                name = "11"
                height = 0
                weight = 0.0
            }
        )
        val properties = arrayOf(TestTable::id.name, TestTable::name.name, TestTable::weight.name)
        assertEquals(2, testTableDao.batchUpdateOnly(entities, 3, *properties))
        assertEquals(2, testTableDao.oneSearch(TestTable::name.name, "11").size)
        assertEquals(2, testTableDao.oneSearch(TestTable::weight.name, 0.0).size)
        assertEquals(0, testTableDao.oneSearch(TestTable::height.name, 0).size)
    }

    @Test
    @Transactional
    open fun batchUpdateOnlyWhen() {
        val entities = listOf(
            TestTable {
                id = -1
                name = "11"
                height = 0
                weight = 0.0
            },
            TestTable {
                id = -2
                name = "11"
                height = 0
                weight = 0.0
            }
        )
        var criteria = Criteria.add(TestTable::name.name, Operator.LIKE_S, "name1")
        val properties = arrayOf(TestTable::id.name, TestTable::name.name, TestTable::weight.name)
        assertEquals(1, testTableDao.batchUpdateOnlyWhen(entities, criteria, 1, *properties))
        assertEquals(1, testTableDao.oneSearch(TestTable::name.name, "11").size)
        assertEquals(1, testTableDao.oneSearch(TestTable::weight.name, 0.0).size)
        assertEquals(0, testTableDao.oneSearch(TestTable::height.name, 0).size)
    }

    @Test
    @Transactional
    open fun batchUpdateExcludeProperties() {
        val entities = listOf(
            TestTable {
                id = -1
                name = "11"
                weight = 0.0
            },
            TestTable {
                id = -2
                name = "11"
                weight = 0.0
            }
        )
        assertEquals(2, testTableDao.batchUpdateExcludeProperties(entities, 1, TestTable::weight.name))
        assertEquals(2, testTableDao.oneSearch(TestTable::name.name, "11").size)
        assertEquals(0, testTableDao.oneSearch(TestTable::weight.name, 0.0).size)
    }

    @Test
    @Transactional
    open fun batchUpdateExcludePropertiesWhen() {
        val entities = listOf(
            TestTable {
                id = -1
                name = "11"
                weight = 0.0
            },
            TestTable {
                id = -2
                name = "11"
                weight = 0.0
            }
        )
        var criteria = Criteria.add(TestTable::name.name, Operator.LIKE_S, "name1")
        assertEquals(1, testTableDao.batchUpdateExcludePropertiesWhen(entities, criteria, 1, TestTable::weight.name))
        assertEquals(1, testTableDao.oneSearch(TestTable::name.name, "11").size)
        assertEquals(0, testTableDao.oneSearch(TestTable::weight.name, 0.0).size)
    }
    //endregion Update


    //region Delete
    @Test
    @Transactional
    open fun deleteById() {
        assert(testTableDao.deleteById(-1))
        assert(testTableDao.getById(-1) == null)
        assert(!testTableDao.deleteById(1))
    }

    @Test
    @Transactional
    open fun delete() {
        val entity = testTableDao.getById(-1)
        assert(testTableDao.delete(entity!!))
        assert(testTableDao.getById(-1) == null)

        // 主键为null
        assertThrows<IllegalStateException> { testTableDao.delete(TestTable {}) }
    }

    @Test
    @Transactional
    open fun batchDelete() {
        val ids = listOf(-1, -2)
        val count = testTableDao.batchDelete(ids)
        assertEquals(2, count)
        assert(testTableDao.inSearch(TestTable::id.name, ids).isEmpty())
    }

    @Test
    @Transactional
    open fun batchDeleteCriteria() {
        val criteria = Criteria.add(TestTable::name.name, Operator.LIKE_E, "1")
        assertEquals(2, testTableDao.batchDeleteCriteria(criteria))
        assertEquals(0, testTableDao.count(criteria))
    }

    @Test
    @Transactional
    open fun batchDeleteWhen() {
        class SearchPayload1 : SearchPayload() {
            var name: String? = null
            var weight: Double? = null
            var noExistProp: String? = "noExistProp"
        }

        val searchPayload1 = SearchPayload1()

        // 无条件
        assertThrows<IllegalArgumentException> {
            testTableDao.batchDeleteWhen()
        }
        assertThrows<IllegalArgumentException> {
            testTableDao.batchDeleteWhen(searchPayload1)
        }
        assertThrows<IllegalArgumentException> {
            testTableDao.batchDeleteWhen { _, _ -> null }
        }

        // 仅whereConditionFactory
        var count = testTableDao.batchDeleteWhen { column, _ ->
            if (column.name == TestTables.name.name) {
                column.eq("name2")
            } else null
        }
        assertEquals(1, count)
        assertEquals(null, testTableDao.getById(-2))

        // 仅SearchPayload项
        searchPayload1.name = "name1"
        count = testTableDao.batchDeleteWhen(searchPayload1)
        assertEquals(1, count)
        assertEquals(null, testTableDao.getById(-1))

        // SearchPayload项 & whereConditionFactory
        searchPayload1.name = "me3"
        count = testTableDao.batchDeleteWhen(searchPayload1) { column, value ->
            if (column.name == TestTables.name.name) {
                column.like("%${value}")
            } else null
        }
        assertEquals(1, count)
        assertEquals(null, testTableDao.getById(-3))
    }

    //endregion Delete

}