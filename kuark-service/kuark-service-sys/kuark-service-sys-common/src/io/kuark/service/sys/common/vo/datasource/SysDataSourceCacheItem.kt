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
        private const val serialVersionUID = 1756041562550276859L
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

    /** 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时 */
    var initialSize: Int? = null

    /** 最大连接池数量 */
    var maxActive: Int? = null

    /** 最小连接池数量 */
    var minIdle: Int? = null

    /** 获取连接时最大等待时间，单位毫秒 */
    var maxWait: Int? = null

    /** 备注，或其国际化key */
    var remark: String? = null

}