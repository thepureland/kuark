package io.kuark.distributed.tx.tx2

/**
 * 分支事务2接口
 *
 * @author K
 * @since 1.0.0
 */
interface IBranchTx2 {

    fun increase(id: Int, money: Double)

    fun increaseFail(id: Int, money: Double)

}