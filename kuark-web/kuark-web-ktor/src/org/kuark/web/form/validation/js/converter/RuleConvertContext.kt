package org.kuark.web.form.validation.js.converter

import org.soul.web.validation.form.export.ValidateRuleExporter
import org.soul.web.validation.form.js.JsRuleCreator
import org.soul.web.validation.form.js.converter.IRuleConverter
import org.soul.web.validation.form.js.converter.RuleConvertContext

/**
 * @author admin
 * @time 8/15/15 10:49 AM
 */
class RuleConvertContext {
    var property: String
    var propertyPrefix: String?
    var formClass: Class<*>
    var originalProperty: String? = null

    constructor(property: String, propertyPrefix: String?, formClass: Class<*>) {
        this.property = property
        this.propertyPrefix = propertyPrefix
        this.formClass = formClass
    }

    constructor(
        originalProperty: String?,
        property: String,
        propertyPrefix: String?,
        formClass: Class<*>
    ) {
        this.originalProperty = originalProperty
        this.property = property
        this.propertyPrefix = propertyPrefix
        this.formClass = formClass
    }

}