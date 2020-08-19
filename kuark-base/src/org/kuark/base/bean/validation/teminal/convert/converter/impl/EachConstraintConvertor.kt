package org.kuark.base.bean.validation.teminal.convert.converter.impl

import org.kuark.base.bean.validation.constraint.annotaions.Each
import org.kuark.base.bean.validation.constraint.validator.ConstraintsValidator

/**
 * Each注解约束->终端约束的转换器
 *
 * @author K
 * @since 1.0.0
 */
class EachConstraintConvertor(annotation: Annotation) : DefaultConstaintConvertor(annotation) {

    override fun getRule(constraintAnnotation: Annotation): LinkedHashMap<String, Any> {
        val map = linkedMapOf<String, Any>()
        constraintAnnotation as Each
        val annotations = ConstraintsValidator.getAnnotations(constraintAnnotation.value)
        annotations.forEach {
            map[it.annotationClass.simpleName!!] = super.getRule(it)
        }
        return map
    }

}