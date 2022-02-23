package io.kuark.ability.web.springmvc

import io.kuark.base.bean.validation.teminal.TeminalConstraintsCreator
import io.kuark.base.lang.GenericKit
import io.kuark.base.support.Consts
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
    open fun getValidationRule(): Map<String, LinkedHashMap<String, Array<Map<String, Any>>>> {
        if (formModelClass == null) {
            formModelClass = getFormModelClass()
        }
        return TeminalConstraintsCreator.create(formModelClass!!)
    }

    /**
     * 返回表单模型类
     *
     * @param F 表单模型类
     * @return KClass
     * @author K
     * @since 1.0.0
     */
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    open fun getFormModelClass(): KClass<F> {
        return GenericKit.getSuperClassGenricClass(this::class, 5) as KClass<F>
    }

}