package io.kuark.ability.data.rdb.support

import io.kuark.ability.data.rdb.table.TestTable
import io.kuark.ability.data.rdb.table.TestTableDao
import io.kuark.ability.data.rdb.table.TestTableKit
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import java.text.MessageFormat
import java.util.*

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
        val column = testTableDao.table()["is_active"]

        println(testTableDao.table().isActive.name)

        println(TestTable::isActive.name)

        testTableDao.table()

        val entity = testTableDao.getById(-1)
        assertEquals("name1", entity!!.name)

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


//    @Test
//    @Throws(Exception::class)
//    fun testSave() {
//        var bean = TestBean()
//        bean.setId(org.soul.data.rdb.mybatis.TestBeanMapperTest.TEST_BEAN_ID)
//        bean.setLoginName(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST)
//        org.junit.Assert.assertTrue(mapper.insert(bean))
//        var results: List<Map<String?, Any?>> =
//            jdbcTemplate.queryForList("SELECT * FROM test_soul WHERE id = " + org.soul.data.rdb.mybatis.TestBeanMapperTest.TEST_BEAN_ID)
//        org.junit.Assert.assertEquals(1, results.size.toLong())
//        org.junit.Assert.assertEquals(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST, results[0]["name"])
//        bean = TestBean()
//        bean.setLoginName(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST2)
//        org.junit.Assert.assertTrue(mapper.insert(bean))
//        results =
//            jdbcTemplate.queryForList("SELECT * FROM test_soul WHERE name = '" + org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST2 + "'")
//        org.junit.Assert.assertEquals(1, results.size.toLong())
//        org.junit.Assert.assertEquals(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST2, results[0]["name"])
//        org.junit.Assert.assertNotNull(results[0]["id"])
//        org.junit.Assert.assertNotNull(bean.getId())
//    }
//
//    @Test
//    fun testSaveOnly() {
//        val bean = TestBean()
//        bean.setLoginName(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST)
//        bean.setBirthday(Date())
//        org.junit.Assert.assertTrue(mapper.insertOnly(bean, TestBean.PROP_LOGIN_NAME))
//        val results: List<Map<String, Any>> =
//            jdbcTemplate.queryForList("SELECT * FROM test_soul WHERE name = '" + org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST + "'")
//        org.junit.Assert.assertEquals(1, results.size.toLong())
//        org.junit.Assert.assertEquals(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST, results[0]["name"])
//        org.junit.Assert.assertNull(results[0]["birthday"])
//        org.junit.Assert.assertNotNull(results[0]["id"])
//        org.junit.Assert.assertNotNull(bean.getId())
//    }
//
//    @Test
//    fun testSaveExclude() {
//        val bean = TestBean()
//        bean.setLoginName(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST)
//        bean.setBirthday(Date())
//        org.junit.Assert.assertTrue(mapper.insertExclude(bean, TestBean.PROP_BIRTHDAY, TestBean.PROP_ID))
//        val results: List<Map<String, Any>> =
//            jdbcTemplate.queryForList("SELECT * FROM test_soul WHERE name = '" + org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST + "'")
//        org.junit.Assert.assertEquals(1, results.size.toLong())
//        org.junit.Assert.assertEquals(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST, results[0]["name"])
//        org.junit.Assert.assertNull(results[0]["birthday"])
//        org.junit.Assert.assertNotNull(results[0]["id"])
//        org.junit.Assert.assertNotNull(bean.getId())
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testBatchSave() {
//        val bean1 = TestBean()
//        bean1.setId(org.soul.data.rdb.mybatis.TestBeanMapperTest.TEST_BEAN_ID)
//        bean1.setLoginName(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST)
//        val bean2 = TestBean()
//        bean2.setId(org.soul.data.rdb.mybatis.TestBeanMapperTest.TEST_BEAN_ID + 1)
//        bean2.setLoginName(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST)
//        val beans: List<TestBean> = Arrays.asList<TestBean>(bean1, bean2)
//        assertEquals(2, mapper.batchInsert(beans))
//        val sqlPattern = "SELECT * FROM test_soul WHERE id = {0} or id = {1}"
//        val sql = MessageFormat.format(
//            sqlPattern,
//            org.soul.data.rdb.mybatis.TestBeanMapperTest.TEST_BEAN_ID.toString() + "",
//            org.soul.data.rdb.mybatis.TestBeanMapperTest.TEST_BEAN_ID + 1 + ""
//        )
//        val results: List<Map<String, Any>> = jdbcTemplate.queryForList(sql)
//        org.junit.Assert.assertEquals(2, results.size.toLong())
//    }
//
//    @Test
//    fun testBatchSaveOnly() {
//        val bean1 = TestBean()
//        bean1.setLoginName(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST)
//        bean1.setBirthday(Date())
//        val bean2 = TestBean()
//        bean2.setLoginName(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST)
//        bean2.setBirthday(Date())
//        val beans: List<TestBean> = Arrays.asList<TestBean>(bean1, bean2)
//        assertEquals(2, mapper.batchInsertOnly(beans, TestBean.PROP_LOGIN_NAME))
//        val sql =
//            "SELECT * FROM test_soul WHERE name = '" + org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST + "'"
//        val results: List<Map<String, Any>> = jdbcTemplate.queryForList(sql)
//        org.junit.Assert.assertEquals(2, results.size.toLong())
//        org.junit.Assert.assertEquals(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST, results[0]["name"])
//        org.junit.Assert.assertNull(results[0]["birthday"])
//        org.junit.Assert.assertNotNull(results[0]["id"])
//        org.junit.Assert.assertNull(bean1.getId())
//        org.junit.Assert.assertEquals(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST, results[1]["name"])
//        org.junit.Assert.assertNull(results[1]["birthday"])
//        org.junit.Assert.assertNotNull(results[1]["id"])
//        org.junit.Assert.assertNull(bean2.getId())
//    }
//
//    @Test
//    fun testBatchSaveExclude() {
//        val bean1 = TestBean()
//        bean1.setLoginName(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST)
//        bean1.setBirthday(Date())
//        val bean2 = TestBean()
//        bean2.setLoginName(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST)
//        bean2.setBirthday(Date())
//        val beans: List<TestBean> = Arrays.asList<TestBean>(bean1, bean2)
//        assertEquals(2, mapper.batchInsertExclude(beans, TestBean.PROP_BIRTHDAY, TestBean.PROP_ID))
//        val sql =
//            "SELECT * FROM test_soul WHERE name = '" + org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST + "'"
//        val results: List<Map<String, Any>> = jdbcTemplate.queryForList(sql)
//        org.junit.Assert.assertEquals(2, results.size.toLong())
//        org.junit.Assert.assertEquals(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST, results[0]["name"])
//        org.junit.Assert.assertNull(results[0]["birthday"])
//        org.junit.Assert.assertNotNull(results[0]["id"])
//        org.junit.Assert.assertNull(bean1.getId())
//        org.junit.Assert.assertEquals(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST, results[1]["name"])
//        org.junit.Assert.assertNull(results[1]["birthday"])
//        org.junit.Assert.assertNotNull(results[1]["id"])
//        org.junit.Assert.assertNull(bean2.getId())
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testUpdate() {
//        val bean: TestBean = mapper.get(-1)
//        bean.setLoginName("testName")
//        val success: Boolean = mapper.update(bean)
//        org.junit.Assert.assertTrue(success)
//        val results: List<Map<String, Any>> = jdbcTemplate.queryForList("SELECT * FROM test_soul WHERE id = -1")
//        org.junit.Assert.assertEquals(1, results.size.toLong())
//        org.junit.Assert.assertEquals("testName", results[0]["name"])
//    }
//
//    @Test
//    fun testUpdateWhen() {
//        val bean: TestBean = mapper.get(-1)
//        bean.setLoginName("testName")
//        var criteria: Criteria = Criteria.add("height", Operator.EQ, 170)
//        var success: Boolean = mapper.updateWhen(bean, criteria)
//        org.junit.Assert.assertFalse(success)
//        var results: List<Map<String?, Any?>> = jdbcTemplate.queryForList("SELECT * FROM test_soul WHERE id = -1")
//        org.junit.Assert.assertEquals(1, results.size.toLong())
//        org.junit.Assert.assertNotEquals("testName", results[0]["name"])
//        bean.setCipher(null)
//        criteria = Criteria.add("height", Operator.EQ, 168)
//        success = mapper.updateWhen(bean, criteria)
//        org.junit.Assert.assertTrue(success)
//        results = jdbcTemplate.queryForList("SELECT * FROM test_soul WHERE id = -1")
//        org.junit.Assert.assertEquals(1, results.size.toLong())
//        org.junit.Assert.assertEquals("testName", results[0]["name"])
//    }
//
//    @Test
//    fun testUpdateProperties() {
//        val properties: MutableMap<String, Any> = java.util.HashMap()
//        properties["id"] = -2
//        properties["loginName"] = "new-name"
//        org.junit.Assert.assertTrue(mapper.updateProperties(-1, properties))
//        assertEquals("new-name", mapper.get(-1).getLoginName())
//        assertEquals("name2", mapper.get(-2).getLoginName())
//    }
//
//    @Test
//    fun testUpdatePropertiesWhen() {
//        val properties: MutableMap<String, Any> = java.util.HashMap()
//        properties["id"] = -2 // 主键应该要不会被更新
//        properties["loginName"] = "new-name"
//        var criteria: Criteria = Criteria.add("name", Operator.EQ, "unexists")
//        org.junit.Assert.assertFalse(mapper.updatePropertiesWhen(-1, properties, criteria))
//        assertEquals("Kevice", mapper.get(-1).getLoginName())
//        assertEquals("name2", mapper.get(-2).getLoginName())
//        criteria = Criteria.add("name", Operator.EQ, "Kevice")
//        org.junit.Assert.assertTrue(mapper.updatePropertiesWhen(-1, properties, criteria))
//        assertEquals("new-name", mapper.get(-1).getLoginName())
//        assertEquals("name2", mapper.get(-2).getLoginName())
//    }
//
//    @Test
//    fun testUpdateOnly() {
//        val bean: TestBean = mapper.get(-1)
//        bean.setLoginName("new-name")
//        val properties = arrayOf("loginName")
//        org.junit.Assert.assertTrue(mapper.updateOnly(bean, properties))
//        assertEquals("new-name", mapper.get(-1).getLoginName())
//    }
//
//    @Test
//    fun testUpdateOnlyWhen() {
//        val bean: TestBean = mapper.get(-1)
//        bean.setLoginName("new-name")
//        val properties = arrayOf("loginName")
//        var criteria: Criteria = Criteria.add("name", Operator.EQ, "unexists")
//        org.junit.Assert.assertFalse(mapper.updateOnlyWhen(bean, criteria, properties))
//        assertEquals("Kevice", mapper.get(-1).getLoginName())
//        criteria = Criteria.add("name", Operator.EQ, "Kevice")
//        org.junit.Assert.assertTrue(mapper.updateOnlyWhen(bean, criteria, properties))
//        assertEquals("new-name", mapper.get(-1).getLoginName())
//    }
//
//    @Test
//    fun testUpdateExcludeProperties() {
//        val bean: TestBean = mapper.get(-1)
//        bean.setLoginName("new-name")
//        bean.setActiveStatus(false)
//        bean.setHeight(170)
//        bean.setBirthday(null)
//        val excludeProperties = arrayOf("activeStatus", "height")
//        org.junit.Assert.assertTrue(mapper.updateExcludeProperties(bean, excludeProperties))
//        val newBean: TestBean = mapper.get(-1)
//        // 改变的属性
//        assertEquals("new-name", newBean.getLoginName())
//        org.junit.Assert.assertNull(newBean.getBirthday())
//
//        // 不变的属性
//        org.junit.Assert.assertTrue(newBean.getActiveStatus())
//        assertEquals(168, newBean.getHeight().intValue())
//    }
//
//    @Test
//    fun testUpdateExcludePropertiesWhen() {
//        val bean: TestBean = mapper.get(-1)
//        bean.setLoginName("new-name")
//        bean.setActiveStatus(false)
//        bean.setHeight(170)
//        bean.setBirthday(null)
//        val excludeProperties = arrayOf("activeStatus", "height")
//        var criteria: Criteria = Criteria.add("name", Operator.EQ, "unexists")
//        org.junit.Assert.assertFalse(mapper.updateExcludePropertiesWhen(bean, criteria, excludeProperties))
//        var newBean: TestBean = mapper.get(-1)
//        assertEquals(168, newBean.getHeight().intValue())
//        bean.setCipher(null)
//        criteria = Criteria.add("name", Operator.EQ, "Kevice")
//        org.junit.Assert.assertTrue(mapper.updateExcludePropertiesWhen(bean, criteria, excludeProperties))
//        newBean = mapper.get(-1)
//        // 改变的属性
//        assertEquals("new-name", newBean.getLoginName())
//        org.junit.Assert.assertNull(newBean.getBirthday())
//        // 不变的属性
//        org.junit.Assert.assertTrue(newBean.getActiveStatus())
//        assertEquals(168, newBean.getHeight().intValue())
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testBatchUpdate() {
//        val bean1 = TestBean()
//        bean1.setId(-1)
//        bean1.setLoginName("11")
//        val bean2 = TestBean()
//        bean2.setId(-2)
//        bean2.setLoginName("22")
//        val beans: List<TestBean> = Arrays.asList<TestBean>(bean1, bean2)
//        assertEquals(2, mapper.batchUpdate(beans))
//    }
//
//    @Test
//    fun testBatchUpdateWhen() {
//        val bean1 = TestBean()
//        bean1.setId(-1)
//        bean1.setLoginName("11")
//        val bean2 = TestBean()
//        bean2.setId(-2)
//        bean2.setLoginName("22")
//        val beans: List<TestBean> = Arrays.asList<TestBean>(bean1, bean2)
//        var criteria: Criteria = Criteria.add("name", Operator.EQ, "unexists")
//        assertEquals(0, mapper.batchUpdateWhen(beans, criteria))
//        criteria = Criteria.add("name", Operator.EQ, "name2")
//        assertEquals(1, mapper.batchUpdateWhen(beans, criteria))
//    }
//
//    @Test
//    fun testBatchUpdateProperties() {
//        val criteria: Criteria = Criteria.add("loginName", Operator.LIKE, "name")
//        val properties: MutableMap<String, Any?> = java.util.HashMap(2, 1f)
//        properties["activeStatus"] = false
//        properties["height"] = null
//        assertEquals(10, mapper.batchUpdateProperties(criteria, properties))
//        assertEquals(10, mapper.oneSearch("activeStatus", false).size())
//        assertEquals(10, mapper.oneSearch("height", null).size())
//    }
//
//    @Test
//    fun testBatchUpdateOnly() {
//        val bean1 = TestBean()
//        bean1.setId(-1)
//        bean1.setLoginName("11")
//        bean1.setHeight(0)
//        bean1.setWeight(0)
//        val bean2 = TestBean()
//        bean2.setId(-2)
//        bean2.setLoginName("11")
//        bean2.setHeight(0)
//        bean2.setWeight(0)
//        val testBeans: List<TestBean> = ListTool.newArrayList(bean1, bean2)
//        val properties = arrayOf("id", "loginName", "weight")
//        assertEquals(2, mapper.batchUpdateOnly(testBeans, properties))
//        assertEquals(2, mapper.oneSearch("loginName", "11").size())
//        assertEquals(2, mapper.oneSearch("weight", 0).size())
//        assertEquals(0, mapper.oneSearch("height", 0).size())
//    }
//
//    @Test
//    fun testBatchUpdateOnlyWhen() {
//        val bean1 = TestBean()
//        bean1.setId(-1)
//        bean1.setLoginName("11")
//        bean1.setHeight(0)
//        bean1.setWeight(0)
//        val bean2 = TestBean()
//        bean2.setId(-2)
//        bean2.setLoginName("11")
//        bean2.setHeight(0)
//        bean2.setWeight(0)
//        val testBeans: List<TestBean> = ListTool.newArrayList(bean1, bean2)
//        val properties = arrayOf("id", "loginName", "weight")
//        var criteria: Criteria = Criteria.add("name", Operator.EQ, "unexists")
//        assertEquals(0, mapper.batchUpdateOnlyWhen(testBeans, criteria, properties))
//        criteria = Criteria.add("name", Operator.EQ, "Kevice")
//        assertEquals(1, mapper.batchUpdateOnlyWhen(testBeans, criteria, properties))
//        assertEquals(1, mapper.oneSearch("loginName", "11").size())
//    }
//
//    @Test
//    fun testBatchUpdateExcludeProperties() {
//        val bean1 = TestBean()
//        bean1.setId(-1)
//        bean1.setLoginName("11")
//        bean1.setHeight(0)
//        bean1.setWeight(0)
//        val bean2 = TestBean()
//        bean2.setId(-2)
//        bean2.setLoginName("11")
//        bean2.setHeight(0)
//        bean2.setWeight(0)
//        val testBeans: List<TestBean> = ListTool.newArrayList(bean1, bean2)
//        val excludeProperties = arrayOf("id", "height")
//        assertEquals(2, mapper.batchUpdateExcludeProperties(testBeans, excludeProperties))
//        assertEquals(2, mapper.oneSearch("loginName", "11").size())
//        assertEquals(2, mapper.oneSearch("weight", 0).size())
//        assertEquals(0, mapper.oneSearch("height", 0).size())
//    }
//
//    @Test
//    fun testBatchUpdateExcludePropertiesWhen() {
//        val bean1 = TestBean()
//        bean1.setId(-1)
//        bean1.setLoginName("11")
//        bean1.setHeight(0)
//        bean1.setWeight(0)
//        val bean2 = TestBean()
//        bean2.setId(-2)
//        bean2.setLoginName("11")
//        bean2.setHeight(0)
//        bean2.setWeight(0)
//        val testBeans: List<TestBean> = ListTool.newArrayList(bean1, bean2)
//        val excludeProperties = arrayOf("id", "height")
//        var criteria: Criteria = Criteria.add("name", Operator.EQ, "unexists")
//        assertEquals(0, mapper.batchUpdateExcludePropertiesWhen(testBeans, criteria, excludeProperties))
//        criteria = Criteria.add("name", Operator.EQ, "Kevice")
//        assertEquals(1, mapper.batchUpdateExcludePropertiesWhen(testBeans, criteria, excludeProperties))
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testDelete() {
//        val success: Boolean = mapper.delete(-1)
//        org.junit.Assert.assertTrue(success)
//        val results: List<Map<String, Any>> = jdbcTemplate.queryForList("SELECT * FROM test_soul WHERE id = -1")
//        org.junit.Assert.assertEquals(0, results.size.toLong())
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testBatchDelete() {
//        val ids = Arrays.asList(-1, -2)
//        val count: Int = mapper.batchDelete(ids)
//        org.junit.Assert.assertEquals(2, count.toLong())
//        val sqlPattern = "SELECT * FROM test_soul WHERE id = {0} or id = {1}"
//        val sql = MessageFormat.format(sqlPattern, -1, -2)
//        val results: List<Map<String, Any>> = jdbcTemplate.queryForList(sql)
//        org.junit.Assert.assertEquals(0, results.size.toLong())
//    }
//
//    @Test
//    fun testBatchDeleteCriteria() {
//        val criteria: Criteria = Criteria.add("loginName", Operator.LIKE, "name")
//        assertEquals(10, mapper.batchDeleteCriteria(criteria))
//        assertEquals(0, mapper.count(criteria))
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testGet() {
//        val result: TestBean = mapper.get(-1)
//        org.junit.Assert.assertNotNull(result)
//        assertEquals(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_KEVICE, result.getLoginName())
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testInSearch() {
//        val ids: List<Int> = ListTool.newArrayList(-3, -2, -1)
//        val results: List<TestBean> = mapper.inSearch("id", ids, Order.desc("id"))
//        org.junit.Assert.assertEquals(3, results.size.toLong())
//        assertEquals(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_KEVICE, results[0].getLoginName())
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testInSearchProperty() {
//        val ids: List<Int> = ListTool.newArrayList(-3, -2, -1)
//        val results: List<Any> = mapper.inSearchProperty("id", ids, "loginName", Order.desc("id"))
//        org.junit.Assert.assertEquals(3, results.size.toLong())
//        org.junit.Assert.assertEquals(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_KEVICE, results[0])
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testInSearchProperties() {
//        val ids: List<Int> = ListTool.newArrayList(-3, -2, -1)
//        val returnProperties: Set<String> = SetTool.newHashSet("loginName", "id", "activeStatus")
//        val results: List<Map<String, Any>> = mapper.inSearchProperties("id", ids, returnProperties, Order.desc("id"))
//        org.junit.Assert.assertEquals(3, results.size.toLong())
//        org.junit.Assert.assertEquals(
//            org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_KEVICE,
//            results[0]["loginName"]
//        )
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testInSearchById() {
//        val ids: List<Int> = ListTool.newArrayList(-3, -2, -1)
//        val results: List<TestBean> = mapper.inSearchById(ids)
//        org.junit.Assert.assertEquals(3, results.size.toLong())
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testInSearchPropertyById() {
//        val ids: List<Int> = ListTool.newArrayList(-3, -2, -1)
//        val results: List<Any> = mapper.inSearchPropertyById(ids, "loginName", Order.desc("id"))
//        org.junit.Assert.assertEquals(3, results.size.toLong())
//        org.junit.Assert.assertEquals(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_KEVICE, results[0])
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testInSearchPropertiesById() {
//        val ids: List<Int> = ListTool.newArrayList(-3, -2, -1)
//        val returnProperties: Set<String> = SetTool.newHashSet("loginName", "id")
//        val results: List<Map<String, Any>> = mapper.inSearchPropertiesById(ids, returnProperties, Order.desc("id"))
//        org.junit.Assert.assertEquals(3, results.size.toLong())
//        org.junit.Assert.assertEquals(
//            org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_KEVICE,
//            results[0]["loginName"]
//        )
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testAllSearch() {
//        val results: List<TestBean> = mapper.allSearch(Order.desc("id"))
//        org.junit.Assert.assertEquals(11, results.size.toLong())
//        assertEquals(Integer.valueOf(-1), results[0].getId())
//        assertEquals(Integer.valueOf(-11), results[10].getId())
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testAllSearchProperty() {
//        val results: List<Any> = mapper.allSearchProperty("id", Order.desc("id"))
//        org.junit.Assert.assertEquals(11, results.size.toLong())
//        org.junit.Assert.assertEquals(Integer.valueOf(-1), results[0])
//        org.junit.Assert.assertEquals(Integer.valueOf(-11), results[10])
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testAllSearchProperties() {
//        val returnProperties: Set<String> = SetTool.newHashSet("loginName", "id")
//        val results: List<Map<String, Any>> = mapper.allSearchProperties(returnProperties)
//        org.junit.Assert.assertEquals(11, results.size.toLong())
//        var eq = org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_KEVICE == results[0]["loginName"]
//        eq = eq || org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_KEVICE == results[1]["loginName"]
//        eq = eq || org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_KEVICE == results[2]["loginName"]
//        org.junit.Assert.assertTrue(eq)
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testOneSearch() {
//        var results: List<TestBean> =
//            mapper.oneSearch("loginName", org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_KEVICE)
//        org.junit.Assert.assertEquals(1, results.size.toLong())
//        assertEquals(Integer.valueOf(-1), results[0].getId())
//        results = mapper.oneSearch("weight", null)
//        org.junit.Assert.assertEquals(2, results.size.toLong())
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun tesOnetSearchProperty() {
//        var results: List<String?> =
//            mapper.oneSearchProperty("loginName", org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_KEVICE, "id")
//        org.junit.Assert.assertEquals(1, results.size.toLong())
//        org.junit.Assert.assertEquals(Integer.valueOf(-1), results[0])
//        results = mapper.oneSearchProperty("weight", null, "loginName")
//        org.junit.Assert.assertEquals(2, results.size.toLong())
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testOneSearchProperties() {
//        val returnProperties: Set<String> = SetTool.newHashSet("loginName", "id")
//        val results: List<Map<String, String>> = mapper.oneSearchProperties(
//            "loginName",
//            org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_KEVICE,
//            returnProperties
//        )
//        org.junit.Assert.assertEquals(1, results.size.toLong())
//        org.junit.Assert.assertEquals(
//            org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_KEVICE,
//            results[0]["loginName"]
//        )
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testAndSearch() {
//        val propertyMap: MutableMap<String, Any?> = java.util.HashMap(2)
//        propertyMap["loginName"] = "name5"
//        propertyMap["weight"] = null
//        val results: List<TestBean> = mapper.andSearch(propertyMap)
//        org.junit.Assert.assertEquals(1, results.size.toLong())
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testAndSearchProperty() {
//        val propertyMap: MutableMap<String, Any?> = java.util.HashMap(2)
//        propertyMap["loginName"] = "name5"
//        propertyMap["weight"] = null
//        val results: List<Any> = mapper.andSearchProperty(propertyMap, "loginName")
//        org.junit.Assert.assertEquals("name5", results[0])
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testAndSearchProperties() {
//        val propertyMap: MutableMap<String, Any> = java.util.HashMap(1)
//        propertyMap["loginName"] = org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_KEVICE
//        propertyMap["activeStatus"] = true
//        val returnProperties: Set<String> = SetTool.newHashSet("loginName", "id")
//        val results: List<Map<String, Any>> = mapper.andSearchProperties(propertyMap, returnProperties)
//        org.junit.Assert.assertEquals(1, results.size.toLong())
//        org.junit.Assert.assertEquals(
//            org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_KEVICE,
//            results[0]["loginName"]
//        )
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testOrSearch() {
//        val propertyMap: MutableMap<String, Any?> = java.util.HashMap(2)
//        propertyMap["loginName"] = "name5"
//        propertyMap["weight"] = null
//        val results: List<TestBean> = mapper.orSearch(propertyMap)
//        org.junit.Assert.assertEquals(2, results.size.toLong())
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testOrSearchProperty() {
//        val propertyMap: MutableMap<String, Any?> = java.util.HashMap(2)
//        propertyMap["loginName"] = "name5"
//        propertyMap["weight"] = null
//        val results: List<*> = mapper.orSearchProperty(propertyMap, "loginName", Order.desc("id"))
//        org.junit.Assert.assertEquals(2, results.size.toLong())
//        org.junit.Assert.assertEquals("name5", results[0])
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testOrSearchProperties() {
//        val propertyMap: MutableMap<String, Any?> = java.util.HashMap(2, 1f)
//        propertyMap["loginName"] = "name5"
//        propertyMap["weight"] = null
//        val returnProperties: Set<String> = SetTool.newHashSet("loginName", "id")
//        val results: List<Map<String, Any>> = mapper.orSearchProperties(propertyMap, returnProperties, Order.desc("id"))
//        org.junit.Assert.assertEquals(2, results.size.toLong())
//        org.junit.Assert.assertEquals("name5", results[0]["loginName"])
//    }
//
//    @Test
//    @Throws(Exception::class)
//    fun testPagingSearch() {
//        val criteria: Criteria = Criteria().addAnd(
//            Criterion("loginName", Operator.ILIKE_S, "name"),
//            Criterion("weight", Operator.IS_NOT_NULL, null)
//        )
//        val results: List<TestBean> = mapper.pagingSearch(criteria, 1, 5, Order.desc("weight"), Order.desc("id"))
//        org.junit.Assert.assertEquals(5, results.size.toLong())
//        assertEquals(Integer.valueOf(-3), results[0].getId())
//        assertEquals(Integer.valueOf(-4), results[4].getId())
//    }
//
//    @Test
//    fun testSearch() {
//        val inIds = Criterion("id", Operator.IN, ListTool.newArrayList(-2, -3, -4, -5, -6, -7, -8, -9, -10))
//        val eqActive = Criterion("activeStatus", Operator.EQ, true)
//        val andCriteria: Criteria = Criteria.and(inIds, eqActive)
//        val likeName = Criterion("loginName", Operator.ILIKE_S, "kE")
//        val orCriteria: Criteria = Criteria.or(likeName, andCriteria)
//        val noNull = Criterion("weight", Operator.IS_NOT_NULL, null)
//        val criteria: Criteria = Criteria.and(orCriteria, noNull)
//        val results: List<TestBean> = mapper.search(criteria, Order.desc("weight"), Order.desc("id"))
//        org.junit.Assert.assertEquals(6, results.size.toLong())
//        assertEquals(Integer.valueOf(-9), results[0].getId())
//    }
//
//    @Test
//    fun testSearchProperties() {
//        SpringTool.getBean(MyBatisMapperReloader::class.java)
//        val inIds = Criterion("id", Operator.IN, ListTool.newArrayList(-2, -3, -4, -5, -6, -7, -8, -9, -10))
//        val eqActive = Criterion("activeStatus", Operator.EQ, true)
//        val andCriteria: Criteria = Criteria.and(inIds, eqActive)
//        val likeName = Criterion("loginName", Operator.ILIKE_S, "kE")
//        val orCriteria: Criteria = Criteria.or(likeName, andCriteria)
//        val noNull = Criterion("weight", Operator.IS_NOT_NULL, null)
//        val criteria: Criteria = Criteria.and(orCriteria, noNull)
//        val returnProperties: List<String> = ListTool.newArrayList("activeStatus", "loginName")
//        val results: List<Map<String, Any>> =
//            mapper.searchProperties(criteria, returnProperties, Order.desc("weight"), Order.desc("id"))
//        org.junit.Assert.assertEquals(6, results.size.toLong())
//        org.junit.Assert.assertEquals("name9", results[0]["loginName"])
//        org.junit.Assert.assertEquals(true, results[0]["activeStatus"])
//    }
//
//    @Test
//    fun testAndSearchPropertyBy() {
//        val criteria: Criteria = Criteria().addAnd(
//            Criterion("loginName", Operator.ILIKE_S, "name"),
//            Criterion("weight", Operator.IS_NOT_NULL, null),
//            Criterion("height", Operator.GT, 160)
//        )
//        val results: List<Any> = mapper.searchProperty(criteria, "id", Order.desc("weight"), Order.desc("id"))
//        org.junit.Assert.assertEquals(6, results.size.toLong())
//        org.junit.Assert.assertEquals(Integer.valueOf(-3), results[0])
//    }
//
//    @Test
//    fun testAndSearchPropertiesBy() {
//        val criteria: Criteria = Criteria.and(
//            Criterion("loginName", Operator.ILIKE_S, "name"),
//            Criterion("weight", Operator.IS_NOT_NULL, null),
//            Criterion("height", Operator.GT, 160)
//        )
//        val returnProperties: Set<String> = SetTool.newHashSet("id", "loginName")
//        val results: List<Map<String, Any>> =
//            mapper.searchProperties(criteria, returnProperties, Order.desc("weight"), Order.desc("id"))
//        org.junit.Assert.assertEquals(6, results.size.toLong())
//        org.junit.Assert.assertEquals(Integer.valueOf(-3), results[0]["id"])
//        org.junit.Assert.assertEquals("name3", results[0]["loginName"])
//    }
//
//    @Test
//    fun testCount() {
//        val criteria: Criteria = Criteria.add("loginName", Operator.ILIKE_S, "name")
//        assertEquals(10L, mapper.count(criteria))
//    }
//
//    @Test
//    fun testSum() {
//        assertEquals(1382L, mapper.sum(null, "height"))
//        val criteria: Criteria = Criteria.add("loginName", Operator.ILIKE_S, "name")
//        assertEquals(389.24, mapper.sum(criteria, "weight"))
//        assertEquals(445.74, mapper.sum(null, "weight"))
//    }
//
//    @Test
//    fun testAvg() {
//        org.junit.Assert.assertEquals("49.53", java.lang.String.format("%.2f", mapper.avg(null, "weight")))
//    }
//
//    @Test
//    fun testMax() {
//        assertEquals(80.0, mapper.max(null, "weight"))
//    }
//
//    @Test
//    fun testMin() {
//        assertEquals(10.0, mapper.min(null, "weight"))
//    }
//
//    @Test
//    fun testDeleteAll() {
//        val count: Int = mapper.deleteAll()
//        org.junit.Assert.assertEquals(11, count.toLong())
//    }
//
//    @Test
//    fun test_cryptEntity_save() {
//        val source = "kevice@soul.com"
//        val phone = "13000000000"
//        val bean = TestBean()
//        bean.setId(org.soul.data.rdb.mybatis.TestBeanMapperTest.TEST_BEAN_ID)
//        bean.setLoginName(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST)
//        bean.setCipher(source)
//        bean.setPhone(phone)
//        org.junit.Assert.assertTrue(mapper.insert(bean))
//        val results: List<Map<String, Any>> =
//            jdbcTemplate.queryForList("SELECT * FROM test_soul WHERE id = " + org.soul.data.rdb.mybatis.TestBeanMapperTest.TEST_BEAN_ID)
//        org.junit.Assert.assertEquals(1, results.size.toLong())
//        assertCryptFieldEqualsSource(source, phone, results)
//    }
//
//    @Test
//    fun test_cryptEntity_batch_save() {
//        val source = "kevice@soul.com"
//        var bean: TestBean? = null
//        val entities: MutableList<TestBean?> = ArrayList<TestBean?>()
//        for (i in 0..1) {
//            bean = TestBean()
//            bean.setId(org.soul.data.rdb.mybatis.TestBeanMapperTest.TEST_BEAN_ID + i)
//            bean.setLoginName(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST)
//            bean.setCipher(source + i)
//            entities.add(bean)
//        }
//        mapper.batchInsert(entities)
//        val sql = "SELECT * FROM test_soul WHERE id in ({0})"
//        val results: List<Map<String, Any>> = jdbcTemplate.queryForList(
//            MessageFormat.format(
//                sql,
//                org.soul.data.rdb.mybatis.TestBeanMapperTest.TEST_BEAN_ID.toString() + "," + (org.soul.data.rdb.mybatis.TestBeanMapperTest.TEST_BEAN_ID + 1)
//            )
//        )
//        org.junit.Assert.assertEquals(2, results.size.toLong())
//    }
//
//
//    private fun assertCryptFieldEqualsSource(source: String, phone: String, results: List<Map<String, Any>>) {
//        var cipher = results[0][TestBean.PROP_CIPHER] as String?
//        val pwdKey: String = TestBean::class.java.getAnnotation(CryptEntity::class.java).password()
//        assertEquals(source, CryptoTool.aesDecrypt(cipher, pwdKey))
//        cipher = results[0][TestBean.PROP_PHONE] as String?
//        assertEquals(phone, CryptoTool.aesDecrypt(cipher, pwdKey))
//    }
//
//    @Test
//    fun test_cryptEntity_get() {
//        val source = "kevice@soul.com"
//        val bean = TestBean()
//        bean.setId(org.soul.data.rdb.mybatis.TestBeanMapperTest.TEST_BEAN_ID)
//        bean.setLoginName(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST)
//        bean.setCipher(source)
//        org.junit.Assert.assertTrue(mapper.insert(bean))
//        val rs: TestBean = mapper.get(org.soul.data.rdb.mybatis.TestBeanMapperTest.TEST_BEAN_ID)
//        assertEquals(rs.getCipher(), source)
//    }
//
//    @Test
//    fun test_cryptEntity_update() {
//        val id = -1
//        val source = "kevice@soul.com"
//        val phone = "13000000000"
//        val bean = TestBean()
//        bean.setId(id)
//        bean.setCipher(source)
//        bean.setSource(source)
//        bean.setPhone(phone)
//        mapper.updateOnly(bean, TestBean.PROP_SOURCE, TestBean.PROP_CIPHER, TestBean.PROP_PHONE)
//        val results: List<Map<String, Any>> = jdbcTemplate.queryForList(
//            "SELECT * FROM test_soul WHERE id = $id"
//        )
//        assertCryptFieldEqualsSource(source, phone, results)
//    }
//
//    @Test
//    fun test_cryptEntity_allSearch() {
//        val source = "kevice@soul.com"
//        val bean = TestBean()
//        bean.setId(org.soul.data.rdb.mybatis.TestBeanMapperTest.TEST_BEAN_ID)
//        bean.setLoginName(org.soul.data.rdb.mybatis.TestBeanMapperTest.LOGIN_NAME_TEST)
//        bean.setCipher(source)
//        bean.setSource(source)
//        org.junit.Assert.assertTrue(mapper.insert(bean))
//        val testBeans: List<TestBean> = mapper.allSearch()
//        for (testBean in testBeans) {
//            assertEquals(testBean.getCipher(), testBean.getSource())
//        }
//    }
//
//    @Test
//    fun test_cryptEntity_search_by_property() {
//        val ciphers: List<String> =
//            mapper.searchProperty(Criteria.add(TestBean.PROP_ID, Operator.EQ, -1), TestBean.PROP_CIPHER)
//        org.junit.Assert.assertEquals("Longer", ciphers[0])
//    }
//
//    /**
//     * Test case: 数组get
//     */
//    @Test
//    fun test_int_array_get() {
//        var testBean: TestBean? = null
//        testBean = mapper.get(-10)
//        org.junit.Assert.assertNull(testBean.getIntArray())
//        testBean = mapper.get(-11)
//        assertArrayEquals(testBean.getIntArray(), arrayOf(1, 2, 3, 4, 5))
//    }
//
//    /**
//     * Test case: 数组insert
//     */
//    @Test
//    fun test_int_array_insert() {
//        var testBean = TestBean()
//        testBean.setId(1)
//        testBean.setLoginName("water")
//        testBean.setIntArray(arrayOf(100, 200))
//        mapper.insertOnly(testBean, TestBean.PROP_ID, TestBean.PROP_LOGIN_NAME, TestBean.PROP_INT_ARRAY)
//        testBean = mapper.get(1)
//        assertArrayEquals(testBean.getIntArray(), arrayOf(100, 200))
//    }
//
//    /**
//     * Test case: 数组insert
//     */
//    @Test
//    fun test_int_array_update() {
//        var testBean: TestBean? = null
//        testBean = mapper.get(-11)
//        testBean.setIntArray(arrayOf(100, 200))
//        mapper.updateOnly(testBean, TestBean.PROP_ID, TestBean.PROP_LOGIN_NAME, TestBean.PROP_INT_ARRAY)
//        assertArrayEquals(testBean.getIntArray(), arrayOf(100, 200))
//    }


}