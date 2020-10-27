package io.kuark.distributed.tx.tx2

interface IBranchTx2 {

    fun increase(userId: Int, money: Double)

}