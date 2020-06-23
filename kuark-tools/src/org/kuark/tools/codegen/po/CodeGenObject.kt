package org.kuark.tools.codegen.po

import org.kuark.data.jdbc.support.IDbEntity
import java.time.LocalTime

interface CodeGenObject : IDbEntity<String, CodeGenObject> {

    var name: String
    var comment: String?
    var createTime: LocalTime
    var createUser: String
    var updateTime: LocalTime?
    var updateUser: String?
    var genCount: Int

}