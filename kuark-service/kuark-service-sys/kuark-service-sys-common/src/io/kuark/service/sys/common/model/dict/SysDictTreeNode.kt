package io.kuark.service.sys.common.model.dict

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class SysDictTreeNode {

    /** 主键 */
    var id: String? = null

    /** 字典类型或字典项编码 */
    var code: String? = null

}