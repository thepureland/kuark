package io.kuark.service.sys.common.vo.datasource

import io.kuark.base.support.result.IdJsonResult


/**
 * 数据源查询记录
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
open class SysDataSourceRecord : IdJsonResult<String>() {
//endregion your codes 1

    //region your codes 2

    /** 租户id */
    @Transient
    var tenantId: String? = null

    /** 租户名称 */
    var tenantName: String? = null

    //endregion your codes 2

    /** 名称，或其国际化key */
    var name: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** url */
    var url: String? = null

    /** 用户名 */
    var username: String? = null

    /** 是否启用 */
    var active: Boolean? = null

}