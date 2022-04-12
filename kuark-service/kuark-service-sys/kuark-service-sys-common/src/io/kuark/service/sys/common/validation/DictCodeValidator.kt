package io.kuark.service.sys.common.validation

import io.kuark.base.bean.validation.constraint.annotaions.DictCode
import io.kuark.context.kit.SpringKit
import io.kuark.service.sys.common.api.ISysDictApi
import io.kuark.service.sys.common.vo.dict.DictModuleAndTypePayload
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * DictCode约束验证器
 *
 * @author K
 * @since 1.0.0
 */
class DictCodeValidator : ConstraintValidator<DictCode, CharSequence?> {

    private lateinit var dictCode: DictCode
    private lateinit var dictApi: ISysDictApi

    override fun initialize(dictCode: DictCode) {
        this.dictCode = dictCode
        dictApi = SpringKit.getBean(ISysDictApi::class)
    }

    override fun isValid(value: CharSequence?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
        val dictItems = dictApi.getDictItems(DictModuleAndTypePayload(dictCode.module, dictCode.dictType))
        val itemCodes = dictItems.map { it.itemCode!! }
        return itemCodes.contains(value)
    }

}