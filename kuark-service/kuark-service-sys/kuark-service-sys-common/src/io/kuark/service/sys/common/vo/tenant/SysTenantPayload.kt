package io.kuark.service.sys.common.vo.tenant

import io.kuark.base.support.payload.FormPayload


/**
 * 租户表单载体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
open class SysTenantPayload : FormPayload<String>() {
//endregion your codes 1

    //region your codes 2

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 名称 */
    var name: String? = null

    /** 备注，或其国际化key */
    var remark: String? = null

    /** 是否启用 */
    var active: Boolean? = null

    //endregion your codes 2

}