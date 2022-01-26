package io.kuark.service.user.common.user.vo.organization

import io.kuark.base.support.payload.FormPayload

open class UserOrganizationPayload: FormPayload<String>() {

    /** 名称 */
    var name: String? = null

    /** 简称 */
    var abbrName: String? = null

    /** 组织类型 */
    var orgTypeDictCode: String? = null


    /** 父id */
    var parentId: String? = null

    /** 在同父节点下的排序号 */
    var seqNo: Int? = null

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 所有者id，依业务可以是店铺id、站点id、商户id等 */
    var ownerId: String? = null

}