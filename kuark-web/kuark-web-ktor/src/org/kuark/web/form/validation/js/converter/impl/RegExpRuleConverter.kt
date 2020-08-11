package org.kuark.web.form.validation.js.converter.impl

import org.kuark.web.form.validation.js.converter.AbstractConstraintConverter
import org.soul.web.validation.form.export.ValidateRuleExporter
import org.soul.web.validation.form.js.JsRuleCreator
import org.soul.web.validation.form.js.converter.AbstractRuleConverter
import org.soul.web.validation.form.js.converter.IRuleConverter
import org.soul.web.validation.form.js.converter.RuleConvertContext
import java.util.*

/**
 * Create by (admin) on 2015/2/15.
 */
class RegExpRuleConverter(annotation: Annotation) : AbstractConstraintConverter(annotation) {
    protected override val rulePatternMap: Map<String, Any>
        protected get() {
            val map: MutableMap<String, Any> = HashMap(1, 1f)
            map["pattern"] = "regexp"
            return map
        }

    override fun appendRule(ruleName: String, ruleValue: Any): String {
        return "$ruleName:/$ruleValue/"
    }
}