package org.kuark.web.form.validation.js.converter

import kotlin.reflect.KClass

/**
 * @author admin
 * @time 8/15/15 10:49 AM
 */
class ConstraintConvertContext {

    var property: String
    var propertyPrefix: String?
    var formClass: KClass<*>
    var originalProperty: String? = null

    constructor(property: String, propertyPrefix: String?, formClass: KClass<*>) {
        this.property = property
        this.propertyPrefix = propertyPrefix
        this.formClass = formClass
    }

    constructor(
        originalProperty: String?,
        property: String,
        propertyPrefix: String?,
        formClass: KClass<*>
    ) {
        this.originalProperty = originalProperty
        this.property = property
        this.propertyPrefix = propertyPrefix
        this.formClass = formClass
    }

}