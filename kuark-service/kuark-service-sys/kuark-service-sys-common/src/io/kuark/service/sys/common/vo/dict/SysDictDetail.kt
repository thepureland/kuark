package io.kuark.service.sys.common.vo.dict

import io.kuark.base.support.result.IdJsonResult
import java.time.LocalDateTime

class SysDictDetail: IdJsonResult<String>() {

    /** 模块 */
    var module: String? = null

    /** 字典类型 */
    var dictType: String? = null

    /** 字典名称，或其国际化key */
    var dictName: String? = null

    /** 记录创建时间 */
    var createTime: LocalDateTime? = null

    /** 记录创建用户 */
    var createUser: String? = null

    /** 记录更新时间 */
    var updateTime: LocalDateTime? = null

    /** 记录更新用户 */
    var updateUser: String? = null

    /** 是否内置 */
    var builtIn: Boolean? = null

    /** 备注 */
    var remark: String? = null

}