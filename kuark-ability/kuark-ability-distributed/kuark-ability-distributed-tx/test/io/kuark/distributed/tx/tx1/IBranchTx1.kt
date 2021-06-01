package io.kuark.distributed.tx.tx1

/**
 * 分支事务1接口
 *
 * @author K
 * @since 1.0.0
 */
interface IBranchTx1 {

    fun decrease(id: Int, money: Double)

}