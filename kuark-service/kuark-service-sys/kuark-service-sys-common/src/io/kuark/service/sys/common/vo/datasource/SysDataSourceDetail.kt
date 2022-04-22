package io.kuark.service.sys.common.vo.datasource

import io.kuark.base.support.result.IdJsonResult
import java.time.LocalDateTime


/**
 * 数据源查询记录
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
open class SysDataSourceDetail : IdJsonResult<String>() {
//endregion your codes 1

    //region your codes 2

    /** 租户名称 */
    var tenantName: String? = null

    //endregion your codes 2


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

    /** 是否启用 */
    var active: Boolean? = null

    /** 是否内置 */
    var builtIn: Boolean? = null

    /** 创建用户 */
    var createUser: String? = null

    /** 创建时间 */
    var createTime: LocalDateTime? = null

    /** 更新用户 */
    var updateUser: String? = null

    /** 更新时间 */
    var updateTime: LocalDateTime? = null

}