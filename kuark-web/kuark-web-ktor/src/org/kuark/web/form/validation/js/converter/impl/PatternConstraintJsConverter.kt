package org.kuark.web.form.validation.js.converter.impl

import org.kuark.web.form.validation.js.converter.AbstractConstraintConverter

/**
 * Pattern注解约束(正则表达式)->js约束的转换器
 *
 * @author K
 * @since 1.0.0
 */
class PatternConstraintJsConverter(annotation: Annotation) : AbstractConstraintConverter(annotation) {

    override val rulePatternMap: Map<String, Any>
        get() = mapOf("pattern" to "regexp")

    override fun appendRule(ruleName: String, ruleValue: Any): String {
        return "$ruleName:/$ruleValue/"
    }
}