package io.kuark.service.sys.provider.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IDbEntity
import java.time.LocalDateTime

/**
 * 缓存数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface SysCache : IDbEntity<String, SysCache> {
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

    /** 缓存管理Bean的名称 */
    var managementBeanName: String

    /** 缓存生存时间(秒) */
    var ttl: Int?

    /** 备注，或其国际化key */
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