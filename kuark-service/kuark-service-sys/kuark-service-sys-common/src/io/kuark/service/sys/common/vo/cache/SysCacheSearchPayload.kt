package io.kuark.service.sys.common.vo.cache

import io.kuark.base.query.enums.Operator
import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass


/**
 * 缓存查询条件载体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
open class SysCacheSearchPayload : ListSearchPayload() {
//endregion your codes 1

    //region your codes 2

    override var returnEntityClass: KClass<*>? = SysCacheRecord::class

    /** 名称 */
    var name: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 是否启用 */
    var active: Boolean? = null

    override var operators: Map<String, Operator>? = mapOf(SysCacheSearchPayload::name.name to Operator.ILIKE)

    //endregion your codes 2

}