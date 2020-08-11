package org.kuark.web.form.validation.js.converter

/**
 * 约束转换器接口，负责将注解约束转换为Javascript约束
 *
 * @author K
 * @since 1.0.0
 */
interface IConstraintConverter {

    /**
     * 将注解约束转换为Javascript约束
     *
     * @param context 上下文
     * @return javascript约束
     */
    fun convert(context: ConstraintConvertContext): JsConstraint

}