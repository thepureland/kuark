package io.kuark.service.provider.sys.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IMaintainableDbEntity

/**
 * 字典主表数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface SysDict : IMaintainableDbEntity<String, SysDict> {
//endregion your codes 1

    companion object : DbEntityFactory<SysDict>()

    /** 模块 */
    var module: String?

    /** 字典类型 */
    var dictType: String

    /** 字典名称，或其国际化key */
    var dictName: String?


    //region your codes 2

    //endregion your codes 2

}