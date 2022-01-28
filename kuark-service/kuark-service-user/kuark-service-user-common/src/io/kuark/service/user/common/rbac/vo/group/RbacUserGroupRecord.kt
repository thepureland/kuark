package io.kuark.service.user.common.rbac.vo.group

import io.kuark.base.support.result.IdJsonResult
import java.time.LocalDateTime

class RbacUserGroupRecord: IdJsonResult<String>() {

    /** 组编码 */
    var groupCode: String? = null

    /** 组名 */
    var groupName: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** 记录创建时间 */
    var createTime: LocalDateTime? = null

    /** 是否启用 */
    var active: Boolean? = null

    /** 备注 */
    var remark: String? = null

}