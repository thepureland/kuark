package org.kuark.tools.codegen.po

import org.kuark.data.jdbc.support.DbEntityFactory
import org.kuark.data.jdbc.support.IDbEntity

/**
 * 代码生成-文件信息数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface CodeGenFile : IDbEntity<String, CodeGenFile> {
//endregion your codes 1

    companion object : DbEntityFactory<CodeGenFile>()

    /** 文件名 */
    var filename: String

    /** 对象名 */
    var objectName: String


    //region your codes 2

    //endregion your codes 2

}