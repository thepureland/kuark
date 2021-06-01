package io.kuark.distributed.tx

import io.kuark.distributed.tx.table.TestTableDao
import io.kuark.distributed.tx.table.TestTableKit
import io.kuark.distributed.tx.tx.GlobalTx
import io.kuark.distributed.tx.tx.GlobalTxApplication
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * 分布式事务测试用例
 *
 * 执行步骤：
 * 1.启动SeataServer和EurekaServer
 * 2.启动BranchTx1Application和BranchTx2Application
 * 3.运行本类DistributedTxTest
 *
 * @author K
 * @since 1.0.0
 */
@SpringBootTest(classes = [GlobalTxApplication::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class DistributedTxTest {

    @Autowired
    private lateinit var globalTx: GlobalTx

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
    fun test() {
        // 当前余额100，先扣50，后加100，结果150
        globalTx.normal()
        assertEquals(150.0, testTableDao.getById(1).balance)

        // 当前余额150，先扣100，后加100(此时出错，应全部回滚)，结果150(没变化)
        assertThrows<Exception> { globalTx.onError() }
        assertEquals(150.0, testTableDao.getById(1).balance)
    }

}
