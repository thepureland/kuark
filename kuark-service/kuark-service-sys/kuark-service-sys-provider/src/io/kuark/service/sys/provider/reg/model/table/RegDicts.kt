package io.kuark.service.sys.provider.reg.model.table

import io.kuark.ability.data.rdb.support.UpdatableTable
import io.kuark.service.sys.provider.reg.model.po.RegDict
import org.ktorm.schema.varchar

/**
 * 字典主表数据库表-实体关联对象
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
object RegDicts: UpdatableTable<RegDict>("reg_dict") {
//endregion your codes 1

    /** 模块 */
    var module = varchar("module").bindTo { it.module }

    /** 字典类型 */
    var dictType = varchar("dict_type").bindTo { it.dictType }

    /** 字典名称，或其国际化key */
    var dictName = varchar("dict_name").bindTo { it.dictName }


    //region your codes 2

	//endregion your codes 2

}