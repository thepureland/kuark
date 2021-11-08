package io.kuark.ability.web.common

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * 返回给前端的统一结果
 *
 * @author K
 * @since 1.0.0
 */
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL) // 序列化json的时候,如果是null的对象,key也会消失
data class WebResult<T>(
    var data: T?,
    var msg: String?,
    var code: Int = 200
) {

    constructor() : this(null, null, 200)

    constructor(data: T?, msg: String? = null) : this(data, msg, 200)

    constructor(msg: String?, code: Int) : this(null, msg, code)

}