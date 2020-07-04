package org.kuark.tools.codegen.po

import org.kuark.data.jdbc.support.IDbEntity

/**
 * 生成的文件历史信息数据库实体
 *
 * @author K
 * @since 1.0.0
 */
interface CodeGenFile : IDbEntity<String, CodeGenFile> {

    var filename: String
    var objectName: String

}