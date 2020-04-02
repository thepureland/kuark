package org.kuark.biz.dao.user

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.date
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import org.kuark.biz.model.user.UserInfo

object UserInfos: Table<UserInfo>("USER_INFO") {
    val id by int("id").primaryKey().bindTo { it.id }
    val name by varchar("name").bindTo { it.name }
    val birthday by date("birthday").bindTo { it.birthday }
}

