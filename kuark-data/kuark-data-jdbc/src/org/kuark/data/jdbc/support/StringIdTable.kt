package org.kuark.data.jdbc.support

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.varchar

open class StringIdTable<E : IDbEntity<String, E>>(tableName: String): Table<E>(tableName) {

    val id by varchar("id").primaryKey().bindTo { it.id }

}