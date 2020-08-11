package org.kuark.web.form.validation.js.converter

import org.kuark.web.form.validation.js.converter.impl.AtLeastConverter
import org.kuark.web.form.validation.js.converter.impl.DependsRuleConverter
import org.kuark.web.form.validation.js.converter.impl.RemoteRuleConverter
import org.soul.model.common.constraints.AtLeast
import org.soul.model.common.constraints.Compare
import org.soul.model.common.constraints.Depends
import org.soul.model.common.constraints.Remote
import org.soul.web.validation.form.export.ValidateRuleExporter
import org.soul.web.validation.form.js.JsRuleCreator
import org.soul.web.validation.form.js.converter.IRuleConverter
import org.soul.web.validation.form.js.converter.RuleConvertContext
import org.soul.web.validation.form.js.converter.impl.*
import javax.validation.constraints.Pattern

/**
 * Create by (admin) on 2015/2/15.
 */
object RuleConverterFactory {
    fun getInstance(annotation: Annotation): IConstraintConverter {
        val annoClass: Class<out Annotation> = annotation.annotationType()
        if (annoClass == Pattern::class.java) {
            return RegExpRuleConverter(annotation)
        } else if (annoClass == Remote::class.java) {
            return RemoteRuleConverter(annotation)
        } else if (annoClass == Depends::class.java) {
            return DependsRuleConverter(annotation)
        } else if (annoClass == AtLeast::class.java) {
            return AtLeastConverter(annotation)
        } else if (annoClass == Compare::class.java) {
            return CompareRuleConverter(annotation)
        }
        return DefaultRuleConverter(annotation)
    }
}