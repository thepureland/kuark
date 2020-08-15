package org.kuark.base.bean.validation.teminal.convert

import kotlin.reflect.KClass

/**
 * 约束转换上下文
 *
 * @author admin
 * @since 1.0.0
 */
class ConstraintConvertContext {

    var property: String
    var propertyPrefix: String?
    var beanClass: KClass<*>
    var originalProperty: String? = null

    constructor(property: String, propertyPrefix: String?, beanClass: KClass<*>) {
        this.property = property
        this.propertyPrefix = propertyPrefix
        this.beanClass = beanClass
    }

    constructor(
        originalProperty: String?,
        property: String,
        propertyPrefix: String?,
        beanClass: KClass<*>
    ) {
        this.originalProperty = originalProperty
        this.property = property
        this.propertyPrefix = propertyPrefix
        this.beanClass = beanClass
    }

}