package io.kuark.service.sys.common.vo.datasource

import java.io.Serializable
import io.kuark.base.support.IIdEntity


/**
 * 数据源缓存项
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
open class SysDataSourceCacheItem : IIdEntity<String>, Serializable {
//endregion your codes 1

    //region your codes 2

    companion object {
        private const val serialVersionUID = 3822319498004465840L
    }

    //endregion your codes 2

    /** 主键 */
    override var id: String? = null

    /** 名称，或其国际化key */
    var name: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** url */
    var url: String? = null

    /** 用户名 */
    var username: String? = null

    /** 密码，强烈建议加密 */
    var password: String? = null

    /** 初始连接数。初始化发生在显示调用init方法，或者第一次getConnection时 */
    var initialSize: Int? = null

    /** 最大连接数 */
    var maxActive: Int? = null

    /** 最大空闲连接数 */
    var maxIdle: Int? = null

    /** 最小空闲连接数。至少维持多少个空闲连接 */
    var minIdle: Int? = null

    /** 出借最长期限(毫秒)。客户端从连接池获取（借出）一个连接后，超时没有归还（return），则连接池会抛出异常 */
    var maxWait: Int? = null

    /** 连接寿命(毫秒)。超时(相对于初始化时间)连接池将在出借或归还时删除这个连接 */
    var maxAge: Int? = null

    /** 备注，或其国际化key */
    var remark: String? = null

}