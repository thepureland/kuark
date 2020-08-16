package org.kuark.base.bean.validation.teminal.convert.converter

import org.kuark.base.bean.validation.teminal.TeminalConstraint
import org.kuark.base.bean.validation.teminal.convert.ConstraintConvertContext

/**
 * 约束转换器接口，负责将注解约束转换为终端约束
 *
 * @author K
 * @since 1.0.0
 */
interface IConstraintConvertor {

    /**
     * 将注解约束转换为终端约束
     *
     * @param context 上下文
     * @return 终端约束
     */
    fun convert(context: ConstraintConvertContext): TeminalConstraint

}