package org.kuark.tools.codegen.dao

import me.liuwj.ktorm.schema.varchar
import org.kuark.data.jdbc.support.StringIdTable
import org.kuark.tools.codegen.po.CodeGenFile

object CodeGenFiles : StringIdTable<CodeGenFile>("code_gen_file") {

    val filename = varchar("filename").bindTo { it.filename }
    val objectName = varchar("object_name").bindTo { it.objectName }

}