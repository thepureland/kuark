package io.kuark.service.user.common.user.vo.account

import com.fasterxml.jackson.annotation.JsonIgnore
import io.kuark.base.bean.validation.constraint.annotaions.DictCode
import io.kuark.base.support.payload.FormPayload
import javax.validation.constraints.NotBlank

open class UserAccountPayload: FormPayload<String>() {

    /** 用户名 */
    @get:NotBlank(message = "用户名不能为空！")
    var username: String? = null

    /** 子系统代码 */
    @get:NotBlank(message = "子系统不能为空！")
    @get:DictCode(message = "子系统不在取值范围内！", module = "kuark:sys", dictType = "sub_sys")
    var subSysDictCode: String? = null

    /** 用户类型代码 */
    @get:DictCode(message = "用户类型不在取值范围内！", module = "kuark:user", dictType = "user_type")
    var userTypeDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

    /** 备注 */
    var remark: String? = null

    /** 密码 */
    @JsonIgnore //不可由前端指定
    var password: String? = null

}