package org.kuark.base.bean.validation.teminal.convert

import org.kuark.base.bean.validation.constraint.annotaions.AtLeast
import org.kuark.base.bean.validation.constraint.annotaions.Compare
import org.kuark.base.bean.validation.teminal.convert.converter.IConstraintConverter
import org.kuark.base.bean.validation.teminal.convert.converter.impl.AtLeastConstaintJsConverter
import org.kuark.base.bean.validation.teminal.convert.converter.impl.CompareConstraintJsConverter
import org.kuark.base.bean.validation.teminal.convert.converter.impl.DefaultConstaintJsConverter
import org.kuark.base.bean.validation.teminal.convert.converter.impl.PatternConstraintJsConverter
import javax.validation.constraints.Pattern

/**
 * 约束注解->终端约束转换器工厂
 *
 * @author K
 * @since 1.0.0
 */
object ConstraintConverterFactory {

    fun getInstance(annotation: Annotation): IConstraintConverter =
        when (annotation.annotationClass) {
            Pattern::class, Pattern.List::class -> PatternConstraintJsConverter(annotation)
            AtLeast::class, AtLeast.List::class -> AtLeastConstaintJsConverter(annotation)
            Compare::class, Compare.List::class -> CompareConstraintJsConverter(annotation)
            else -> DefaultConstaintJsConverter(annotation)
        }

}