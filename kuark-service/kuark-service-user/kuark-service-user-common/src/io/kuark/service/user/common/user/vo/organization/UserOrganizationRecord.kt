package io.kuark.service.user.common.user.vo.organization

import io.kuark.base.support.result.IdJsonResult
import java.time.LocalDateTime

open class UserOrganizationRecord: IdJsonResult<String>() {

    /** 名称 */
    var name: String? = null

    /** 简称 */
    var abbrName: String? = null

    /** 组织类型 */
    var orgTypeDictCode: String? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String? = null

    /** 是否启用 */
    var active: Boolean? = null

    /** 创建时间 */
    var createTime: LocalDateTime? = null

}