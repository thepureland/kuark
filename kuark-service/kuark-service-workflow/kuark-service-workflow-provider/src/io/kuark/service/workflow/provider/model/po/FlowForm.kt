package io.kuark.service.workflow.provider.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IDbEntity
import java.sql.Clob
import java.time.LocalDateTime

/**
 * 工作流表单数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface FlowForm : IDbEntity<String, FlowForm> {
//endregion your codes 1

    companion object : DbEntityFactory<FlowForm>()

    /** 表单key */
    var key: String

    /** 版本 */
    var version: Int

    /** 分类字典代码 */
    var categoryDictCode: String

    /** 名称 */
    var name: String

    /** 路径 */
    var path: String

    /** 内容 */
    var content: Clob

    /** 备注 */
    var remark: String?

    /** 是否内置 */
    var builtIn: Boolean

    /** 创建用户 */
    var createUser: String?

    /** 创建时间 */
    var createTime: LocalDateTime?

    /** 更新用户 */
    var updateUser: String?

    /** 更新时间 */
    var updateTime: LocalDateTime?


    //region your codes 2

    //endregion your codes 2

}