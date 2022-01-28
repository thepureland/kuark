package io.kuark.service.sys.provider.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IMaintainableDbEntity

/**
 * 租户数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface SysTenant : IMaintainableDbEntity<String, SysTenant> {
//endregion your codes 1

    companion object : DbEntityFactory<SysTenant>()

    /** 子系统代码 */
    var subSysDictCode: String

    /** 名称 */
    var name: String


    //region your codes 2

    //endregion your codes 2

}