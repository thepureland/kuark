package io.kuark.ability.web.springmvc

import io.kuark.ability.web.common.WebResult
import io.kuark.base.bean.validation.teminal.TeminalConstraintsCreator
import io.kuark.base.lang.GenericKit
import io.kuark.base.support.payload.ListSearchPayload
import org.springframework.web.bind.annotation.GetMapping
import kotlin.reflect.KClass

open class BaseReadOnlyController<S: ListSearchPayload> {

    @GetMapping("/getValidationRule")
    fun getValidationRule(): WebResult<Map<String, LinkedHashMap<String, Array<Map<String, Any>>>>> {
        val formModelClass = GenericKit.getSuperClassGenricClass(this::class, 0)
        val rule = TeminalConstraintsCreator.create(formModelClass)
        return WebResult(rule)
    }




}