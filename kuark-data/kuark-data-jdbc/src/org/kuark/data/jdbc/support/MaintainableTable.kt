package org.kuark.data.jdbc.support

import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.time
import me.liuwj.ktorm.schema.varchar

open class MaintainableTable<E : IMaintainableDbEntity<String, E>>(tableName: String): StringIdTable<E>(tableName) {

    val createTime by time("create_time").bindTo { it.createTime }
    val createUser by varchar("create_user").bindTo { it.createUser }
    val updateTime by time("update_time").bindTo { it.updateTime }
    val updateUser by varchar("update_user").bindTo { it.updateUser }
    val isActive by boolean("is_active").bindTo { it.isActive }
    val isBuiltIn by boolean("is_builtIn").bindTo { it.isBuiltIn }
    val remark by varchar("remark").bindTo { it.remark }

}