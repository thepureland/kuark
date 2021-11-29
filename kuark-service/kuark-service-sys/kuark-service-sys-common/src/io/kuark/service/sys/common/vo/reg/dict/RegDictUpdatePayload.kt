package io.kuark.service.sys.common.vo.reg.dict

import io.kuark.base.support.payload.UpdatePayload

class RegDictUpdatePayload: UpdatePayload<RegDictSearchPayload>() {

    /** 是否启用 */
    var active: Boolean? = null

}