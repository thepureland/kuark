package io.kuark.ability.web.springmvc

import io.kuark.ability.web.common.WebResult
import io.kuark.base.bean.validation.teminal.TeminalConstraintsCreator
import org.springframework.web.bind.annotation.GetMapping
import kotlin.reflect.KClass

abstract class BaseController {

    @GetMapping("/getValidationRule")
    fun getValidationRule(): WebResult<Map<String, LinkedHashMap<String, Array<Map<String, Any>>>>> {
        val formModelClass = getFormModelClass()
        val rule = TeminalConstraintsCreator.create(formModelClass)
        return WebResult(rule)
    }

    abstract fun getFormModelClass(): KClass<*>

}