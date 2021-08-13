package io.kuark.ability.workflow.form

import org.ktorm.schema.*
import io.kuark.ability.data.rdb.support.StringIdTable

/**
 * 工作流表单数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object FlowForms : StringIdTable<FlowForm>("flow_form") {
//endregion your codes 1

    /** 表单key */
    var key = varchar("key").bindTo { it.key }

    /** 版本 */
    var version = int("version_").bindTo { it.version }

    /** 分类字典代码 */
    var categoryDictCode = varchar("category_dict_code").bindTo { it.categoryDictCode }

    /** 名称 */
    var name = varchar("name").bindTo { it.name }

    /** 路径 */
    var path = varchar("path").bindTo { it.path }

    /** 内容 */
    var content = text("content").bindTo { it.content.toString() }

    /** 备注 */
    var remark = varchar("remark").bindTo { it.remark }

    /** 是否内置 */
    var isBuiltIn = boolean("is_built_in").bindTo { it.isBuiltIn }

    /** 创建用户 */
    var createUser = varchar("create_user").bindTo { it.createUser }

    /** 创建时间 */
    var createTime = datetime("create_time").bindTo { it.createTime }

    /** 更新用户 */
    var updateUser = varchar("update_user").bindTo { it.updateUser }

    /** 更新时间 */
    var updateTime = datetime("update_time").bindTo { it.updateTime }


    //region your codes 2

    //endregion your codes 2

}