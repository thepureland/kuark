package io.kuark.service.sys.common.model.dict

import io.kuark.base.support.payload.UpdatePayload

class SysDictUpdateItems: UpdatePayload<SysDictSearchPayload>() {

    /** 是否启用 */
    var active: Boolean? = null

}