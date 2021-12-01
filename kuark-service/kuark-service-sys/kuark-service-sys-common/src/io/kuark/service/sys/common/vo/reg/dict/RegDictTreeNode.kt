package io.kuark.service.sys.common.vo.reg.dict

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class RegDictTreeNode: Serializable {

    /** 主键 */
    var id: String? = null

    /** 字典类型或字典项编码 */
    var code: String? = null

}