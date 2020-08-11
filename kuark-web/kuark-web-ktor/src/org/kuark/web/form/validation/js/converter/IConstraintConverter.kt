package org.kuark.web.form.validation.js.converter

/**
 * 约束转换器接口
 *
 * @author K
 * @since 1.0.0
 */
interface IConstraintConverter {

    /**
     * 将Bean约束转换为前端JS校验规则
     *
     * @param context 上下文
     * @return js校验规则
     */
    fun convert(context: RuleConvertContext): JsConstraintResult

}