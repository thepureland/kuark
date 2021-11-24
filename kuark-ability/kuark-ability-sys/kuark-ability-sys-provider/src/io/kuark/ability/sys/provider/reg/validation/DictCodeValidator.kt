package io.kuark.ability.sys.provider.reg.validation

import io.kuark.base.bean.validation.constraint.annotaions.DictCode
import io.kuark.context.kit.SpringKit
import io.kuark.ability.sys.provider.reg.ibiz.IRegDictItemBiz
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * DictCode约束验证器
 *
 * @author K
 * @since 1.0.0
 */
class DictCodeValidator: ConstraintValidator<DictCode, CharSequence?> {

    private lateinit var dictCode: DictCode
    private lateinit var regDictItemBiz: IRegDictItemBiz

    override fun initialize(dictCode: DictCode) {
        this.dictCode = dictCode
        regDictItemBiz = SpringKit.getBean(IRegDictItemBiz::class)
    }

    override fun isValid(value: CharSequence?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
        val dictItems = regDictItemBiz.getItemsByModuleAndType(dictCode.module, dictCode.dictType)
        val itemCodes = dictItems.map { it.itemCode }
        return itemCodes.contains(value)
    }

}