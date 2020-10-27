package io.kuark.distributed.tx

import io.kuark.distributed.tx.tx.GlobalTx
import io.kuark.test.SpringTest
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

    @Test
    fun test() {
        globalTx.run()
    }

}

