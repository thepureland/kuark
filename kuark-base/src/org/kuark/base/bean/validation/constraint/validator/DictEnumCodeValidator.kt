package org.kuark.base.bean.validation.constraint.validator

import org.kuark.base.lang.EnumKit
import org.kuark.base.bean.validation.constraint.annotaions.DictEnumCode
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * DictEnumCode约束验证器
 *
 * @author K
 * @since 1.0.0
 */
class DictEnumCodeValidator : ConstraintValidator<DictEnumCode, String?> {

    private lateinit var dictEnumCode: DictEnumCode

    override fun initialize(dictEnumCode: DictEnumCode) {
        this.dictEnumCode = dictEnumCode
    }

    override fun isValid(value: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
        val dictMap = EnumKit.getCodeMap(dictEnumCode.enumClass)
        return dictMap.containsKey(value)
    }

}