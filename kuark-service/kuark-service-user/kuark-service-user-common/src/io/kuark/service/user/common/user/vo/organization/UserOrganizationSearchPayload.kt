package io.kuark.service.user.common.user.vo.organization

import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass

open class UserOrganizationSearchPayload: ListSearchPayload() {

    override var returnEntityClass: KClass<*>? = UserOrganizationRecord::class

    override var pageNo: Int? = null // 不分页

    /** 名称 */
    var name: String? = null

    /** 组织类型 */
    var orgTypeDictCode: String? = null

    /** 是否启用 */
    var active: Boolean? = null

}