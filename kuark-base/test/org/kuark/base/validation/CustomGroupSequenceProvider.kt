package org.kuark.base.validation

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
class CustomGroupSequenceProvider : DefaultGroupSequenceProvider<ValidationBean> {

    override fun getValidationGroups(obj: ValidationBean?): MutableList<Class<*>> {
        val result = mutableListOf<Class<*>>(ValidationBean::class.java)
        if (obj != null) result.add(obj.javaClass)
        return result
    }

}