package org.kuark.base.bean.validation.teminal.convert

import org.kuark.base.bean.validation.constraint.annotaions.Compare
import org.kuark.base.bean.validation.constraint.annotaions.DictEnumCode
import org.kuark.base.bean.validation.teminal.convert.converter.IConstraintConvertor
import org.kuark.base.bean.validation.teminal.convert.converter.impl.CompareConstraintConvertor
import org.kuark.base.bean.validation.teminal.convert.converter.impl.DefaultConstaintConvertor
import org.kuark.base.bean.validation.teminal.convert.converter.impl.DictEnumCodeConstraintConvertor

/**
 * 约束注解->终端约束转换器工厂
 *
 * @author K
 * @since 1.0.0
 */
object ConstraintConvertorFactory {

    fun getInstance(annotation: Annotation): IConstraintConvertor =
        when (annotation.annotationClass) {
            DictEnumCode::class -> DictEnumCodeConstraintConvertor(annotation)
            Compare::class, Compare.List::class -> CompareConstraintConvertor(annotation)
            else -> DefaultConstaintConvertor(annotation)
        }

}