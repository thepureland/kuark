package io.kuark.distributed.tx.table

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IDbEntity

/**
 * 测试表数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
internal interface TestTable : IDbEntity<Int, TestTable> {
//endregion your codes 1

    companion object : DbEntityFactory<TestTable>()

    /** 余额 */
    var balance: Double


    //region your codes 2

    //endregion your codes 2

}