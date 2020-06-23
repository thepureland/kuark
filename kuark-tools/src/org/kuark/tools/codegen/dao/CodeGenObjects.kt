package org.kuark.tools.codegen.dao

import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.time
import me.liuwj.ktorm.schema.varchar
import org.kuark.data.jdbc.support.StringIdTable
import org.kuark.tools.codegen.po.CodeGenObject

object CodeGenObjects : StringIdTable<CodeGenObject>("code_gen_object") {

    val name by varchar("name").bindTo { it.name }
    val comment by varchar("comment").bindTo { it.comment }
    val createTime by time("create_time").bindTo { it.createTime }
    val createUser by varchar("create_user").bindTo { it.createUser }
    val updateTime by time("update_time").bindTo { it.updateTime }
    val updateUser by varchar("update_user").bindTo { it.updateUser }
    val genCount by int("gen_count").bindTo { it.genCount }

}