package io.kuark.service.sys.provider.model.table

import io.kuark.ability.data.rdb.support.MaintainableTable
import io.kuark.service.sys.provider.model.po.SysParam
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * 参数数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object SysParams: MaintainableTable<SysParam>("sys_param") {
//endregion your codes 1

    /** 模块 */
    var module = varchar("module").bindTo { it.module }

    /** 参数名称 */
    var paramName = varchar("param_name").bindTo { it.paramName }

    /** 参数值，或其国际化key */
    var paramValue = varchar("param_value").bindTo { it.paramValue }

    /** 默认参数值，或其国际化key */
    var defaultValue = varchar("default_value").bindTo { it.defaultValue }

    /** 序号 */
    var seqNo = int("seq_no").bindTo { it.seqNo }


    //region your codes 2

	//endregion your codes 2

}