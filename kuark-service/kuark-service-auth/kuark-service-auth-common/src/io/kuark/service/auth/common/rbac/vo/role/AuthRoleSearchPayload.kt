package io.kuark.service.auth.common.rbac.vo.role

import io.kuark.base.support.payload.ListSearchPayload
import kotlin.reflect.KClass

class AuthRoleSearchPayload: ListSearchPayload() {

    override var returnEntityClass: KClass<*>? = AuthRoleRecord::class

}