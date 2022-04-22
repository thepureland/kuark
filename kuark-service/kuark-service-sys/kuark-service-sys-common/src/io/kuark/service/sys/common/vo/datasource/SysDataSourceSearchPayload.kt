package io.kuark.service.sys.common.vo.datasource

import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass


/**
 * 数据源查询条件载体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
open class SysDataSourceSearchPayload : ListSearchPayload() {
//endregion your codes 1

    //region your codes 2

    //endregion your codes 2

    override var returnEntityClass: KClass<*>? = SysDataSourceRecord::class

    /** 名称，或其国际化key */
    var name: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** 是否启用 */
    var active: Boolean? = null

}