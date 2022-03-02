package io.kuark.service.sys.common.vo.tenant

import io.kuark.base.query.enums.Operator
import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass


/**
 * 租户查询条件载体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
open class SysTenantSearchPayload : ListSearchPayload() {
//endregion your codes 1

    //region your codes 2

    override var returnEntityClass: KClass<*>? = SysTenantRecord::class

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 名称 */
    var name: String? = null

    /** 是否启用 */
    var active: Boolean? = null

    override var operators: Map<String, Operator>? = mapOf(SysTenantSearchPayload::name.name to Operator.ILIKE)

    //endregion your codes 2

}