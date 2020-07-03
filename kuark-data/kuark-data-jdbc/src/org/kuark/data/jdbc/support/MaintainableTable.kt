package org.kuark.data.jdbc.support

import me.liuwj.ktorm.schema.*

open class MaintainableTable<E : IMaintainableDbEntity<String, E>>(tableName: String): StringIdTable<E>(tableName) {

    val createTime = datetime("create_time").bindTo { it.createTime }
    val createUser = varchar("create_user").bindTo { it.createUser }
    val updateTime = datetime("update_time").bindTo { it.updateTime }
    val updateUser = varchar("update_user").bindTo { it.updateUser }
    val isActive = boolean("is_active").bindTo { it.isActive }
    val isBuiltIn = boolean("is_builtIn").bindTo { it.isBuiltIn }
    val remark = varchar("remark").bindTo { it.remark }

}