package io.kuark.service.sys.common.vo.dict

import javax.validation.constraints.NotBlank

class DictModuleAndTypePayload {

    /** 模块 */
    var module: String? = null

    /** 字典类型 */
    @get:NotBlank(message = "字典类型不能为空！")
    var dictType: String? = null

    constructor()

    constructor(module: String?, dictType: String) {
        this.module = module
        this.dictType = dictType
    }

}