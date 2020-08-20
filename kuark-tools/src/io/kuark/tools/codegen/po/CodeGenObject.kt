package io.kuark.tools.codegen.po

import io.kuark.data.jdbc.support.DbEntityFactory
import io.kuark.data.jdbc.support.IDbEntity
import java.time.LocalDateTime

/**
 * 代码生成-对象信息数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface CodeGenObject : IDbEntity<String, CodeGenObject> {
//endregion your codes 1

    companion object : DbEntityFactory<CodeGenObject>()

    /** 对象名称 */
    var name: String

    /** 注释 */
    var comment: String

    /** 创建时间 */
    var createTime: LocalDateTime

    /** 创建用户 */
    var createUser: String

    /** 更新时间 */
    var updateTime: LocalDateTime

    /** 更新用户 */
    var updateUser: String

    /** 生成次数 */
    var genCount: Int


    //region your codes 2

    //endregion your codes 2

}