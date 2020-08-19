package org.kuark.base.bean.validation.teminal.convert.converter.impl

import org.kuark.base.bean.validation.constraint.annotaions.NotNullOn

/**
 * NotNullOn注解约束->终端约束的转换器
 *
 * @author K
 * @since 1.0.0
 */
class NotNullOnConstraintConvertor(annotation: Annotation) : DefaultConstaintConvertor(annotation) {

    override fun getRule(constraintAnnotation: Annotation): LinkedHashMap<String, Any> {
        val map = super.getRule(constraintAnnotation)
        constraintAnnotation as NotNullOn
        val depends = constraintAnnotation.depends
        map["depends"] = super.getRule(depends)
        return map
    }

}