package org.kuark.tools.codegen.dao

import me.liuwj.ktorm.schema.datetime
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.time
import me.liuwj.ktorm.schema.varchar
import org.kuark.data.jdbc.support.StringIdTable
import org.kuark.tools.codegen.po.CodeGenObject

object CodeGenObjects : StringIdTable<CodeGenObject>("code_gen_object") {

    val name = varchar("name").bindTo { it.name }
    val comment = varchar("comment").bindTo { it.comment }
    val createTime = datetime("create_time").bindTo { it.createTime }
    val createUser = varchar("create_user").bindTo { it.createUser }
    val updateTime = datetime("update_time").bindTo { it.updateTime }
    val updateUser = varchar("update_user").bindTo { it.updateUser }
    val genCount = int("gen_count").bindTo { it.genCount }

}