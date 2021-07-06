package io.kuark.ability.data.rdb.support

import io.kuark.ability.data.rdb.table.TestTableDao
import io.kuark.ability.data.rdb.table.TestTableKit
import io.kuark.base.query.Criteria
import io.kuark.base.query.sort.Order
import io.kuark.base.support.GroupExecutor
import io.kuark.base.support.logic.AndOr
import io.kuark.test.common.SpringTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.ktorm.entity.*
import org.ktorm.schema.Column
import org.springframework.beans.factory.annotation.Autowired
import java.text.MessageFormat
import java.util.*

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





}