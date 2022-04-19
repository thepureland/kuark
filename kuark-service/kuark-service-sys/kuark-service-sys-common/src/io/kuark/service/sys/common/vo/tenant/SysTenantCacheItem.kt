package io.kuark.service.sys.common.vo.dict

import io.kuark.base.support.IIdEntity
import java.io.Serializable


/**
 * 租户的缓存项
 *
 * @author K
 * @since 1.0.0
 */
class SysTenantCacheItem: IIdEntity<String>, Serializable {

    companion object {
        private const val serialVersionUID = 3292934385656787739L
    }

    /** 主键 */
    override var id: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 名称 */
    var name: String? = null

    /** 备注，或其国际化key */
    var remark: String? = null

    /** 是否内置 */
    var builtIn: Boolean? = null

}