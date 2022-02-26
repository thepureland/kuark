package io.kuark.service.sys.common.vo.tenant

import io.kuark.base.bean.validation.constraint.annotaions.DictCode
import io.kuark.base.support.payload.FormPayload
import javax.validation.constraints.NotBlank


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
    @get:NotBlank(message = "子系统不能为空！")
    @get:DictCode(message = "子系统不在取值范围内！", module = "kuark:sys", dictType = "sub_sys")
    var subSysDictCode: String? = null

    /** 名称 */
    @get:NotBlank(message = "名称不能为空！")
    var name: String? = null

    /** 备注，或其国际化key */
    var remark: String? = null

    //endregion your codes 2

}