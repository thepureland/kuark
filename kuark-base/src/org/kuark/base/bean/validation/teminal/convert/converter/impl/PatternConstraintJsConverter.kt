package org.kuark.base.bean.validation.teminal.convert.converter.impl

import org.kuark.base.bean.validation.teminal.convert.converter.AbstractConstraintConverter

/**
 * Pattern注解约束(正则表达式)->终端约束的转换器
 *
 * @author K
 * @since 1.0.0
 */
class PatternConstraintJsConverter(annotation: Annotation) : AbstractConstraintConverter(annotation) {

    override fun getRule(constraintAnnotation: Annotation): MutableMap<String, Any> {
        return mutableMapOf("pattern" to "regexp")
    }

}