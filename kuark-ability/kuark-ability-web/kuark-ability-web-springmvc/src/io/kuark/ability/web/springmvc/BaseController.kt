package io.kuark.ability.web.springmvc

import io.kuark.base.support.payload.ListSearchPayload
import java.io.Serializable

open class BaseController<B: IBaseR S : ListSearchPayload, R : Serializable, F : Any>
    : BaseReadOnlyController<S>() {


}