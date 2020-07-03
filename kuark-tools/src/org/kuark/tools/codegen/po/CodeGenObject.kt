package org.kuark.tools.codegen.po

import org.kuark.data.jdbc.support.IDbEntity
import java.time.LocalDateTime

interface CodeGenObject : IDbEntity<String, CodeGenObject> {

    var name: String
    var comment: String?
    var createTime: LocalDateTime
    var createUser: String
    var updateTime: LocalDateTime?
    var updateUser: String?
    var genCount: Int

}