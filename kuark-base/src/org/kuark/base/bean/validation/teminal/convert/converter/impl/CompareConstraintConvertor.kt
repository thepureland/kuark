package org.kuark.base.bean.validation.teminal.convert.converter.impl

import org.kuark.base.bean.validation.constraint.annotaions.Compare
import org.kuark.base.lang.getMemberProperty
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType

/**
 * Compare注解约束->终端约束的转换器
 *
 * @author K
 * @since 1.0.0
 */
class CompareConstraintConvertor(annotation: Annotation) : DefaultConstaintConvertor(annotation) {

    override fun getRule(constraintAnnotation: Annotation): MutableMap<String, Any> {
        val map = super.getRule(constraintAnnotation)
        val beanClass = context.beanClass
        val returnType = beanClass.getMemberProperty(context.property).returnType
        if (returnType.isSubtypeOf(Number::class.starProjectedType)) {
            map["isNumber"] = "true"
        }
        constraintAnnotation as Compare
        val depends = constraintAnnotation.depends
        if (depends.properties.isNotEmpty()) {
            map["depends"] = super.getRule(depends)
        } else {
            map.remove("depends")
        }
        return map
    }

}