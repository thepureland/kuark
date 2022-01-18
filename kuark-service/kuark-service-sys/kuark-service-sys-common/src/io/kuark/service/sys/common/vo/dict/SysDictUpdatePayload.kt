package io.kuark.service.sys.common.vo.dict

import io.kuark.base.support.payload.UpdatePayload

class SysDictUpdatePayload: UpdatePayload<SysDictSearchPayload>() {

    /** 是否启用 */
    var active: Boolean? = null

}