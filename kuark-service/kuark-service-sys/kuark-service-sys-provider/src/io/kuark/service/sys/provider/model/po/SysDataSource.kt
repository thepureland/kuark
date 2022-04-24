package io.kuark.service.sys.provider.model.po

import io.kuark.ability.data.rdb.support.DbEntityFactory
import io.kuark.ability.data.rdb.support.IMaintainableDbEntity

/**
 * 数据源数据库实体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
interface SysDataSource : IMaintainableDbEntity<String, SysDataSource> {
//endregion your codes 1

    companion object : DbEntityFactory<SysDataSource>()

    /** 名称，或其国际化key */
    var name: String

    /** 子系统代码 */
    var subSysDictCode: String

    /** 租户id */
    var tenantId: String?

    /** url */
    var url: String

    /** 用户名 */
    var username: String

    /** 密码，强烈建议加密 */
    var password: String?

    /** 初始连接数。初始化发生在显示调用init方法，或者第一次getConnection时 */
    var initialSize: Int?

    /** 最大连接数 */
    var maxActive: Int?

    /** 最大空闲连接数 */
    var maxIdle: Int?

    /** 最小空闲连接数。至少维持多少个空闲连接 */
    var minIdle: Int?

    /** 出借最长期限(毫秒)。客户端从连接池获取（借出）一个连接后，超时没有归还（return），则连接池会抛出异常 */
    var maxWait: Int?

    /** 连接寿命(毫秒)。超时(相对于初始化时间)连接池将在出借或归还时删除这个连接 */
    var maxAge: Int?


    //region your codes 2

    //endregion your codes 2

}