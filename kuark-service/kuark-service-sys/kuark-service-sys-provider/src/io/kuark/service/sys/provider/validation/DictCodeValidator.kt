package io.kuark.service.sys.provider.validation

import io.kuark.base.bean.validation.constraint.annotaions.DictCode
import io.kuark.context.kit.SpringKit
import io.kuark.service.sys.provider.ibiz.ISysDictItemBiz
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
    private lateinit var sysDictItemBiz: ISysDictItemBiz

    override fun initialize(dictCode: DictCode) {
        this.dictCode = dictCode
        sysDictItemBiz = SpringKit.getBean(ISysDictItemBiz::class)
    }

    override fun isValid(value: CharSequence?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
        val dictItems = sysDictItemBiz.getItemsByModuleAndType(dictCode.module, dictCode.dictType)
        val itemCodes = dictItems.map { it.itemCode }
        return itemCodes.contains(value)
    }

}