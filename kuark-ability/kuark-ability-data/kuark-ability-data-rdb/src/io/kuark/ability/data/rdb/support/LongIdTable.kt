package io.kuark.ability.data.rdb.support

import org.ktorm.schema.Table
import org.ktorm.schema.long

/**
 * 长整型主键的dao
 *
 * @param E 实体类型
 * @author K
 * @since 1.0.0
 */
open class LongIdTable<E : IDbEntity<Long, E>>(tableName: String): Table<E>(tableName) {

    /** 主键 */
    val id = long("id").primaryKey().bindTo { it.id }

}