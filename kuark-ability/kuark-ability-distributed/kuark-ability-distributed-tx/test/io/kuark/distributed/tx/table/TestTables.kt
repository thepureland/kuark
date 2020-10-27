package io.kuark.distributed.tx.table

import org.ktorm.schema.*
import io.kuark.ability.data.rdb.support.IntIdTable

/**
 * 测试表数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
internal object TestTables : IntIdTable<TestTable>("test_table") {
//endregion your codes 1

    /** 名字 */
    var name = varchar("name").bindTo { it.name }

    /** 生日 */
    var birthday = datetime("birthday").bindTo { it.birthday }

    /** 是否生效 */
    var isActive = boolean("is_active").bindTo { it.isActive }

    /** 体重 */
    var weight = double("weight").bindTo { it.weight }

    /** 身高 */
    var height = int("height").bindTo { it.height }


    //region your codes 2

    //endregion your codes 2

}