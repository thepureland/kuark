package io.kuark.service.sys.common.vo.dict

import io.kuark.base.support.IIdEntity
import java.io.Serializable


/**
 * 资源的缓存项
 *
 * @author K
 * @since 1.0.0
 */
class SysResourceCacheItem: IIdEntity<String>, Serializable {

    companion object {
        private const val serialVersionUID = 2292933385654787737L
    }


    /** 主键 */
    override var id: String? = null

    /** 名称，或其国际化key */
    var name: String? = null

    /** url */
    var url: String? = null

    /** 资源类型字典代码 */
    var resourceTypeDictCode: String? = null

    /** 父id */
    var parentId: String? = null

    /** 在同父节点下的排序号 */
    var seqNo: Int? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 图标 */
    var icon: String? = null

    /** 是否内置 */
    var builtIn: Boolean? = null

    /** 备注 */
    var remark: String? = null

}