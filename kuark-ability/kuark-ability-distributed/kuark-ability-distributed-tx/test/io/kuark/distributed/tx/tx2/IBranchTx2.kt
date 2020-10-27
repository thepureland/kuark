package io.kuark.distributed.tx.tx2

interface IBranchTx2 {

    fun increase(id: Int, money: Double)

    fun increaseFail(id: Int, money: Double)

}