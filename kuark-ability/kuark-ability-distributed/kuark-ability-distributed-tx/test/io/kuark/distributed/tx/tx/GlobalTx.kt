package io.kuark.distributed.tx.tx

import io.seata.spring.annotation.GlobalTransactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * 模拟全局事务
 *
 * @author K
 * @since 1.0.0
 */
@Service
open class GlobalTx {

    @Autowired
    private lateinit var branchTx1: IBranchTx1

    @Autowired
    private lateinit var branchTx2: IBranchTx2

    @GlobalTransactional(name = "change-balance-normal", rollbackFor = [Exception::class])
    open fun normal() {

        branchTx1.decrease(1, 50.0) // 扣款

        branchTx2.increase(1, 100.0) // 加款
    }

    @GlobalTransactional(name = "change-balance-error", rollbackFor = [Exception::class])
    open fun onError() {

        branchTx1.decrease(1, 100.0) // 扣款

        branchTx2.increaseFail(1, 100.0) // 模拟加款失败

    }

}