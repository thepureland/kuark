package io.kuark.service.sys.common.vo.dict

import io.kuark.base.support.IIdEntity
import java.io.Serializable


/**
 * 字典的缓存项
 *
 * @author K
 * @since 1.0.0
 */
class SysDictCacheItem : IIdEntity<String>, Serializable {

    companion object {
        private const val serialVersionUID = 7990933882657037756L
    }


    /** 主键 */
    override var id: String? = null

    /** 模块 */
    var module: String? = null

    /** 字典类型 */
    var dictType: String? = null

    /** 字典名称，或其国际化key */
    var dictName: String? = null

    /** 是否内置 */
    var builtIn: Boolean? = null

    /** 备注 */
    var remark: String? = null

}