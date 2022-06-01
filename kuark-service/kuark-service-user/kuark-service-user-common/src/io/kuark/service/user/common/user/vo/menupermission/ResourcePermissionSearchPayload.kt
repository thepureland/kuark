package io.kuark.service.user.common.user.vo.menupermission

import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass

open class ResourcePermissionSearchPayload: ListSearchPayload() {

    override var pageNo: Int? = null // 不分页

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 租户id */
    var tenantId: String? = null

}