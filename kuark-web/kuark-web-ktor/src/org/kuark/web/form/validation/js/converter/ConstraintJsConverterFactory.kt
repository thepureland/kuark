package org.kuark.web.form.validation.js.converter

import org.kuark.base.bean.validation.constraint.annotaions.AtLeast
import org.kuark.base.bean.validation.constraint.annotaions.Compare
import org.kuark.web.form.validation.js.converter.impl.AtLeastConstaintJsConverter
import org.kuark.web.form.validation.js.converter.impl.CompareConstraintJsConverter
import org.kuark.web.form.validation.js.converter.impl.DefaultConstaintJsConverter
import org.kuark.web.form.validation.js.converter.impl.PatternConstraintJsConverter
import javax.validation.constraints.Pattern

/**
 * 约束注解->js校验规则转换器工厂
 *
 * @author K
 * @since 1.0.0
 */
object ConstraintJsConverterFactory {

    fun getInstance(annotation: Annotation): IConstraintConverter =
        when (annotation.annotationClass) {
            Pattern::class -> PatternConstraintJsConverter(annotation)
            AtLeast::class -> AtLeastConstaintJsConverter(annotation)
            Compare::class -> CompareConstraintJsConverter(annotation)
            else -> DefaultConstaintJsConverter(annotation)
        }

}