package org.kuark.tools.codegen.po

import org.kuark.data.jdbc.support.IDbEntity
import java.time.LocalDateTime

/**
 * 生成的表对象历史信息数据库实体
 *
 * @author K
 * @since 1.0.0
 */
interface CodeGenObject : IDbEntity<String, CodeGenObject> {

    var name: String
    var comment: String?
    var createTime: LocalDateTime
    var createUser: String
    var updateTime: LocalDateTime?
    var updateUser: String?
    var genCount: Int

}