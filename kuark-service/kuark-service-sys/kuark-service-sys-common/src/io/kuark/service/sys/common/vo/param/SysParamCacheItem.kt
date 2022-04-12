package io.kuark.service.sys.common.vo.dict

import java.io.Serializable


/**
 * 参数的缓存项
 *
 * @author K
 * @since 1.0.0
 */
class SysParamCacheItem: Serializable {

    companion object {
        private const val serialVersionUID = 5290933384657787736L
    }


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

    /** 是否内置 */
    var builtIn: Boolean? = null

    /** 备注 */
    var remark: String? = null

}