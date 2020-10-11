package io.kuark.ability.data.rdb.support

import org.ktorm.schema.Table
import org.ktorm.schema.varchar

/**
 * 字符串型主键的dao
 *
 * @param E 实体类型
 * @author K
 * @since 1.0.0
 */
open class StringIdTable<E : IDbEntity<String, E>>(tableName: String): Table<E>(tableName) {

    /** 主键 */
    val id = varchar("id").primaryKey().bindTo { it.id }

}