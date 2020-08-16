package org.kuark.base.bean.validation.teminal.convert.converter.impl

import org.kuark.base.bean.validation.constraint.annotaions.DictEnumCode
import org.kuark.base.lang.EnumKit

/**
 * DictEnumCode约束注解->终端约束转换器
 *
 * @author K
 * @since 1.0.0
 */
class DictEnumCodeConstraintConvertor(annotation: Annotation) : DefaultConstaintConvertor(annotation) {

    override fun getRule(constraintAnnotation: Annotation): MutableMap<String, Any> {
        val map = super.getRule(constraintAnnotation)
        constraintAnnotation as DictEnumCode
        map.remove("enumClass")
        val codeMap = EnumKit.getCodeMap(constraintAnnotation.enumClass)
        map["values"] = codeMap.keys
        return map
    }

}