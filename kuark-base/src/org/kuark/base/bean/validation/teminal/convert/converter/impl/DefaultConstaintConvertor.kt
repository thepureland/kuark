package org.kuark.base.bean.validation.teminal.convert.converter.impl

import org.kuark.base.bean.validation.teminal.convert.converter.AbstractConstraintConvertor
import kotlin.reflect.full.memberProperties

/**
 * 默认的约束注解->终端约束转换器
 *
 * @author K
 * @since 1.0.0
 */
open class DefaultConstaintConvertor(annotation: Annotation) : AbstractConstraintConvertor(annotation) {

    override fun getRule(constraintAnnotation: Annotation): LinkedHashMap<String, Any> {
        val rules = linkedMapOf<String, Any>()
        constraintAnnotation.annotationClass.memberProperties.forEach {
            if (it.name != "groups" && it.name != "payload") {
                rules[it.name] = it.call(constraintAnnotation)!!
            }
        }
        return rules
    }

}