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

    /**
     * 返回约束注解的终端约束转换器
     *
     * @param annotation 约束注解
     * @return 终端约束转换器
     * @author K
     * @since 1.0.0
     */
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