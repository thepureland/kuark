package io.kuark.ability.data.rdb.support

import org.ktorm.schema.Table
import org.ktorm.schema.int

/**
 * 整型主键的dao
 *
 * @param E 实体类型
 * @author K
 * @since 1.0.0
 */
open class IntIdTable<E : IDbEntity<Int, E>>(tableName: String): Table<E>(tableName) {

    /** 主键 */
    val id = int("id").primaryKey().bindTo { it.id }

}