package io.kuark.service.sys.common.vo.reg.dict

import javax.validation.constraints.NotBlank

class DictModuleAndTypePayload {

    /** 模块 */
    var module: String? = null

    /** 字典类型 */
    @get:NotBlank(message = "字典类型不能为空！")
    var dictType: String? = null

}