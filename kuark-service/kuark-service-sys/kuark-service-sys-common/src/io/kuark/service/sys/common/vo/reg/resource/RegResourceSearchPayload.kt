package io.kuark.service.sys.common.vo.reg.resource

import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass

class RegResourceSearchPayload: ListSearchPayload() {

    /** 子系统代码 */
    var subSysDictCode: String? = null

    /** 资源类型字典代码 */
    var resourceTypeDictCode: String? = null

    /** 名称，或其国际化key */
    var name: String? = null

    /** 父资源id */
    var parentId: String? = null

    /** 树层级 */
    var level: Int? = null

    /** 是否启用 */
    var active: Boolean? = null

    override var returnEntityClass: KClass<*>? = RegResourceRecord::class

}