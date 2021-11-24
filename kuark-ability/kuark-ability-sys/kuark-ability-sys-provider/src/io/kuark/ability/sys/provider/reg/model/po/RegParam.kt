package io.kuark.ability.sys.provider.reg.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IMaintainableDbEntity

/**
 * 参数数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface RegParam: IMaintainableDbEntity<String, RegParam> {
//endregion your codes 1

    companion object : DbEntityFactory<RegParam>()

    /** 模块 */
    var module: String?

    /** 参数名称 */
    var paramName: String

    /** 参数值，或其国际化key */
    var paramValue: String

    /** 默认参数值，或其国际化key */
    var defaultValue: String?

    /** 序号 */
    var seqNo: Int?


    //region your codes 2

	//endregion your codes 2

}