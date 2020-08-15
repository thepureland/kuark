package org.kuark.base.bean.validation.teminal.convert.converter.impl

import org.kuark.base.bean.validation.constraint.annotaions.AtLeast
import org.kuark.base.bean.validation.teminal.TeminalConstraint
import org.kuark.base.bean.validation.teminal.convert.ConstraintConvertContext
import org.kuark.base.bean.validation.teminal.convert.converter.AbstractConstraintConverter
import kotlin.reflect.KClass

/**
 * AtLeast注解约束->js约束的转换器
 *
 * @author K
 * @since 1.0.0
 */
class AtLeastConstaintJsConverter(annotation: Annotation) : AbstractConstraintConverter(annotation) {

    override fun getRule(constraintAnnotation: Annotation): MutableMap<String, Any> {
        return mutableMapOf()
    }

}