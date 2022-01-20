package io.kuark.service.sys.common.vo.dict

import io.kuark.base.support.result.IdJsonResult

class SysDictTreeNode: IdJsonResult<String>() {

    /** 字典类型或字典项编码 */
    var code: String? = null

}