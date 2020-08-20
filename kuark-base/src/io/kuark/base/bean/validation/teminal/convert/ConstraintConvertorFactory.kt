package io.kuark.base.bean.validation.teminal.convert

import io.kuark.base.bean.validation.constraint.annotaions.*
import io.kuark.base.bean.validation.teminal.convert.converter.IConstraintConvertor
import io.kuark.base.bean.validation.teminal.convert.converter.impl.*

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
            NotNullOn::class -> NotNullOnConstraintConvertor(annotation)
            Each::class -> EachConstraintConvertor(annotation)
            Exist::class -> ExistConstraintConvertor(annotation)
            Constraints::class -> ConstraintsConstraintConvertor(annotation)
            Remote::class -> RemoteConstraintConvertor(annotation)
            else -> DefaultConstaintConvertor(annotation)
        }

}