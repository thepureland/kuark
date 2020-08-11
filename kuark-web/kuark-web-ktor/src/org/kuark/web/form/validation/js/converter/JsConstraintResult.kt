package org.kuark.web.form.validation.js.converter

import org.soul.web.validation.form.export.ValidateRuleExporter
import org.soul.web.validation.form.js.JsRuleCreator
import org.soul.web.validation.form.js.converter.IRuleConverter
import org.soul.web.validation.form.js.converter.RuleConvertContext

/**
 * Create by (admin) on 2015/1/21.
 */
class JsConstraintResult {
    var rule: String? = null
    var msg: String? = null

    constructor() {}
    constructor(rule: String?, msg: String?) {
        this.rule = rule
        this.msg = msg
    }

}