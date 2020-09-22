package io.kuark.service.provider.sys.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IMaintainableDbEntity

/**
 * 数据源数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface SysDataSource: IMaintainableDbEntity<String, SysDataSource> {
//endregion your codes 1

    companion object : DbEntityFactory<SysDataSource>()

    /** 名称，或其国际化key */
    var name: String

    /** url */
    var url: String

    /** 用户名 */
    var username: String

    /** 密码，强烈建议加密 */
    var password: String

    /** 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时 */
    var initialSize: Int?

    /** 最大连接池数量 */
    var maxActive: Int?

    /** 最小连接池数量 */
    var minIdle: Int?

    /** 获取连接时最大等待时间，单位毫秒 */
    var maxWait: Int?


    //region your codes 2

	//endregion your codes 2

}