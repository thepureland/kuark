package io.kuark.distributed.tx.tx

import io.kuark.distributed.tx.tx2.IBranchTx2
import io.seata.spring.annotation.GlobalTransactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
@Service
class GlobalTx {

    @Autowired
    private lateinit var branchTx1: IBranchTx1

    @Autowired
    private lateinit var branchTx2: IBranchTx2

    @GlobalTransactional(name = "fsp-create-order", rollbackFor = [Exception::class])
    fun run() {

    }

}