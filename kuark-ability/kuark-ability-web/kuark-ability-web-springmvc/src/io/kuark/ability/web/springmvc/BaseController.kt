package io.kuark.ability.web.springmvc

import io.kuark.ability.web.common.WebResult
import io.kuark.base.bean.validation.teminal.TeminalConstraintsCreator
import io.kuark.base.lang.GenericKit
import io.kuark.base.support.Consts
import io.kuark.base.support.IIdEntity
import org.springframework.web.bind.annotation.GetMapping
import kotlin.reflect.KClass

/**
 * 基础的Controller
 *
 * @param F 表单实体类
 * @author K
 * @since 1.0.0
 */
open class BaseController<F: Any> {

    private var formModelClass: KClass<F>? = null

    /**
     * 获取表单校验规则
     *
     * @return WebResult(Map(属性名， LinkedHashMap(约束名，Array(Map(约束注解的属性名，约束注解的属性值)))))
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/getValidationRule")
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    open fun getValidationRule(): WebResult<Map<String, LinkedHashMap<String, Array<Map<String, Any>>>>> {
        if (formModelClass == null) {
            formModelClass = GenericKit.getSuperClassGenricClass(this::class, 0) as KClass<F>
        }
        return WebResult(TeminalConstraintsCreator.create(formModelClass!!))
    }

}