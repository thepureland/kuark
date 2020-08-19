package org.kuark.base.bean.validation.teminal.convert.converter.impl

import org.kuark.base.bean.validation.constraint.annotaions.Constraints
import org.kuark.base.bean.validation.constraint.validator.ConstraintsValidator
import org.kuark.base.support.logic.AndOr

/**
 * Constraints约束注解->终端约束转换器
 *
 * @author K
 * @since 1.0.0
 */
class ConstraintsConstraintConvertor(annotation: Annotation) : DefaultConstaintConvertor(annotation) {

    override fun getRule(constraintAnnotation: Annotation): LinkedHashMap<String, Any> {
        val map = linkedMapOf<String, Any>()
        constraintAnnotation as Constraints
        val annotations = ConstraintsValidator.getAnnotations(constraintAnnotation)
        annotations.forEach {
            val constraintName = it.annotationClass.simpleName!!
            val ruleMap = super.getRule(it)
            if (constraintAnnotation.andOr == AndOr.OR) {
                ruleMap.remove("message") // 当子约束间的校验逻辑为OR时，子约束的message无意义，提示信息取Constraints约束的message
            }
            map[constraintName] = ruleMap
        }
        if (constraintAnnotation.andOr == AndOr.OR) {
            map["andOr"] = AndOr.OR // 缺省为AND
            // 当子约束间的校验逻辑为OR时，Constraints的message才有意义(为AND时提示信息取子约束的message)
            map["message"] = constraintAnnotation.message
        }
        return map
    }

}