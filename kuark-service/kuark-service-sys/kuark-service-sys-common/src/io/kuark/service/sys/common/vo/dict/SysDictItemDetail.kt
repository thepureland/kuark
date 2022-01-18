package io.kuark.service.sys.common.vo.dict

import java.io.Serializable
import java.time.LocalDateTime

class SysDictItemDetail: Serializable {

    /** 主键 */
    var id: String? = null

    /** 外键，sys_dict表的主键 */
    var dictId: String? = null

    /** 字典项编号 */
    var itemCode: String? = null

    /** 字典项名称，或其国际化key */
    var itemName: String? = null

    /** 父项主键 */
    var parentId: String? = null

    /** 该字典编号在同父节点下的排序号 */
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