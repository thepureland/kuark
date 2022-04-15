package io.kuark.service.sys.provider.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IMaintainableDbEntity

/**
 * 缓存数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface SysCache : IMaintainableDbEntity<String, SysCache> {
//endregion your codes 1

    companion object : DbEntityFactory<SysCache>()

    /** 名称 */
    var name: String

    /** 子系统代码 */
    var subSysDictCode: String

    /** 缓存策略代码 */
    var strategyDictCode: String

    /** 是否启动时写缓存 */
    var writeOnBoot: Boolean

    /** 是否及时回写缓存 */
    var writeInTime: Boolean

    /** 缓存生存时间(秒) */
    var ttl: Int?


    //region your codes 2

    //endregion your codes 2

}