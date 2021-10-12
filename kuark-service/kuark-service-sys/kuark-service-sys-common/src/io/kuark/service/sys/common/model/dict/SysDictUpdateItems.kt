package io.kuark.service.sys.common.model.dict

import io.kuark.base.support.payload.UpdatePayload

class SysDictUpdateItems: UpdatePayload<String>() {

    /** 是否启用 */
    var isActive: Boolean? = null

}