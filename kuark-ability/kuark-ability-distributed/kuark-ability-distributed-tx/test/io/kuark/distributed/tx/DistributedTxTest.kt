package io.kuark.distributed.tx

import io.kuark.distributed.tx.table.TestTableKit
import io.kuark.distributed.tx.tx.GlobalTx
import io.kuark.test.SpringTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
class DistributedTxTest : SpringTest() {

    @Autowired
    private lateinit var globalTx: GlobalTx

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
        // 原来余额100，先扣50，后加100，结果150
        assertEquals(150.0, globalTx.normal())

        // 原来余额100，先扣50，后加100(此时出错，应全部回滚)，结果100(没变化)
        assertEquals(100.0, globalTx.onError())
    }

}

