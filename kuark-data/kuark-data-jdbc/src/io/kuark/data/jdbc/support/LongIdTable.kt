package io.kuark.data.jdbc.support

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long

/**
 * 长整型主键的dao
 *
 * @author K
 * @since 1.0.0
 */
open class LongIdTable<E : IDbEntity<Long, E>>(tableName: String): Table<E>(tableName) {

    /** 主键 */
    val id = long("id").primaryKey().bindTo { it.id }

}