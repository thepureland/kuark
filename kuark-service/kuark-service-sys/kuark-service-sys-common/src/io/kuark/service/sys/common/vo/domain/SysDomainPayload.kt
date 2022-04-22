package io.kuark.service.sys.common.vo.domain

import io.kuark.base.bean.validation.constraint.annotaions.DictCode
import io.kuark.base.support.payload.FormPayload
import javax.validation.constraints.NotBlank


/**
 * 域名表单载体
 *
 * @author K
 * @since 1.0.0
 */
//region your codes 1
open class SysDomainPayload : FormPayload<String>() {
//endregion your codes 1

    //region your codes 2

    /** 域名 */
    @get:NotBlank(message = "域名不能为空！")
    var domain: String? = null

    /** 子系统代码 */
    @get:NotBlank(message = "子系统不能为空！")
    @get:DictCode(message = "子系统不在取值范围内！", module = "kuark:sys", dictType = "sub_sys")
    var subSysDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** 备注，或其国际化key */
    var remark: String? = null

    //endregion your codes 2

}