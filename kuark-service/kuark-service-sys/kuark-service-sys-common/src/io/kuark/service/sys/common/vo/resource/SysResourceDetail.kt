package io.kuark.service.sys.common.vo.resource

import java.io.Serializable
import java.time.LocalDateTime

class SysResourceDetail: Serializable {

    /** 主键 */
    var id: String? = null

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