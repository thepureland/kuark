package io.kuark.distributed.tx.tx1

interface IBranchTx1 {

    fun decrease(userId: Int, money: Double)

}