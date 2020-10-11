package io.kuark.tools.codegen.dao

import io.kuark.ability.data.rdb.support.StringIdTable
import io.kuark.tools.codegen.po.CodeGenObject
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * 代码生成-对象信息数据库实体DAO
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object CodeGenObjects : StringIdTable<CodeGenObject>("code_gen_object") {
//endregion your codes 1

    /** 对象名称 */
    var name = varchar("name").bindTo { it.name }

    /** 注释 */
    var comment = varchar("comment").bindTo { it.comment }

    /** 创建时间 */
    var createTime = datetime("create_time").bindTo { it.createTime }

    /** 创建用户 */
    var createUser = varchar("create_user").bindTo { it.createUser }

    /** 更新时间 */
    var updateTime = datetime("update_time").bindTo { it.updateTime }

    /** 更新用户 */
    var updateUser = varchar("update_user").bindTo { it.updateUser }

    /** 生成次数 */
    var genCount = int("gen_count").bindTo { it.genCount }


    //region your codes 2

    //endregion your codes 2

}