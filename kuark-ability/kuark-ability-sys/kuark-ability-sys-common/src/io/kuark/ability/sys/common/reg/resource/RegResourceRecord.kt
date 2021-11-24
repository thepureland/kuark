package io.kuark.ability.sys.common.reg.resource

class RegResourceRecord {

    /** 主键 */
    var id: String? = null

    /** 名称，或其国际化key */
    var name: String? = null

    /** url */
    var url: String? = null

    /** 资源类型字典代码 */
    var resourceTypeDictCode: String? = null

    /** 资源类型名称 */
    var resourceTypeName: String? = null

    /** 所有父项ID */
    var parentIds: List<String>? = null

    /** 在同父节点下的排序号 */
    var seqNo: Int? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 子系统名称 */
    var subSysName: String? = null

    /** 图标 */
    var icon: String? = null

    /** 是否启用 */
    var active: Boolean? = null

    /** 备注 */
    var remark: String? = null

}