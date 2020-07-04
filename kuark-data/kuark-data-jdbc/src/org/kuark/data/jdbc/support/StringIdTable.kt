package org.kuark.data.jdbc.support

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.varchar

/**
 * 字符串型主键的dao
 *
 * @author K
 * @since 1.0.0
 */
open class StringIdTable<E : IDbEntity<String, E>>(tableName: String): Table<E>(tableName) {

    /** 主键 */
    val id = varchar("id").primaryKey().bindTo { it.id }

}