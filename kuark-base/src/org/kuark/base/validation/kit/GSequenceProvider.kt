package org.kuark.base.validation.kit

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider

/**
 *
 *
 * @author K
 * @since 1.0.0
 */
class GSequenceProvider<T>: DefaultGroupSequenceProvider<T> {
    override fun getValidationGroups(`object`: T): MutableList<Class<*>> {
        TODO("Not yet implemented")
    }


}