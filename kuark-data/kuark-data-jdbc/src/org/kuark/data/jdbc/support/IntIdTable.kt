package org.kuark.data.jdbc.support

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int

open class IntIdTable<E : IDbEntity<Int, E>>(tableName: String): Table<E>(tableName) {

    val id = int("id").primaryKey().bindTo { it.id }

}