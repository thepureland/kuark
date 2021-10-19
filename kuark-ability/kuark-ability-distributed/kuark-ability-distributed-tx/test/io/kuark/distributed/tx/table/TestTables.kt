package io.kuark.distributed.tx.table

import io.kuark.ability.data.rdb.support.IntIdTable
import org.ktorm.schema.double

/**
 * 测试表数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
internal object TestTables : IntIdTable<TestTable>("test_table") {
//endregion your codes 1

    /** 余额 */
    var balance = double("balance").bindTo { it.balance }


    //region your codes 2

    //endregion your codes 2

}