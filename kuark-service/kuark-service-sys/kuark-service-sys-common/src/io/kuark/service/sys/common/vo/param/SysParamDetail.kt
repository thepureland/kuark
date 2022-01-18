package io.kuark.service.sys.common.vo.param

import java.io.Serializable
import java.time.LocalDateTime


class SysParamDetail: Serializable {

    /** 主键 */
    var id: String? = null

    /** 模块 */
    var module: String? = null

    /** 参数名称 */
    var paramName: String? = null

    /** 参数值，或其国际化key */
    var paramValue: String? = null

    /** 默认参数值，或其国际化key */
    var defaultValue: String? = null

    /** 序号 */
    var seqNo: Int? = null

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

    /** 是否启用 */
    var active: Boolean? = null

}