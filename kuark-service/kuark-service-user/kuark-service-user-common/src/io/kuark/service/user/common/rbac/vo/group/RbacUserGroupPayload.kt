package io.kuark.service.user.common.rbac.vo.group

import io.kuark.base.support.IIdEntity
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

class RbacUserGroupPayload: IIdEntity<String> {

    /** 主键 */
    override var id: String? = null

    /** 组编码 */
    @get:NotBlank(message = "组编码不能为空！")
    var groupCode: String? = null

    /** 组名 */
    @get:NotBlank(message = "组名不能为空！")
    var groupName: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String? = null

    /** 备注 */
    var remark: String? = null

}