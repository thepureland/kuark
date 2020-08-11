package org.kuark.web.form.validation.js.converter.impl

import org.kuark.web.form.validation.js.converter.AbstractConstraintConverter
import org.soul.commons.lang.ArrayTool
import org.soul.commons.lang.reflect.MethodTool
import org.soul.commons.lang.string.StringTool
import org.soul.model.common.constraints.Compare
import org.soul.model.common.constraints.Depends
import org.soul.web.validation.form.export.ValidateRuleExporter
import org.soul.web.validation.form.js.JsRuleCreator
import org.soul.web.validation.form.js.converter.AbstractRuleConverter
import org.soul.web.validation.form.js.converter.IRuleConverter
import org.soul.web.validation.form.js.converter.RuleConvertContext
import java.lang.reflect.Method
import java.util.*

/**
 * @author admin
 * @time 9/16/15 11:52 AM
 */
class CompareRuleConverter(compare: Annotation) : AbstractConstraintConverter(compare) {
    private val compare: Compare
    protected override val rulePatternMap: Map<String, Any>
        protected get() {
            val map: MutableMap<String, Any> = HashMap(2, 1f)
            val nestedMap: MutableMap<String, Any> =
                HashMap(2, 1f)
            nestedMap["logic"] = compare.logic().toString()
            nestedMap["property"] = compare.anotherProperty()
            val formClass: Class<*> = context.getFormClass()
            val depends: Depends = compare.depends()
            if (StringTool.isNotBlank(compare.isNumber())) {
                nestedMap["isNumber"] = compare.isNumber()
            } else {
                val property: String = context.getOriginalProperty()
                val readMethod: Method = MethodTool.getReadMethod(formClass, property)
                val returnType = readMethod.returnType
                if (returnType == String::class.java) {
                    nestedMap["isNumber"] = "false"
                } else {
                    nestedMap["isNumber"] = "true"
                }
            }
            if (ArrayTool.isNotEmpty(depends.property())) {
                val dependsJsExp: String = DependsRuleConverter.Companion.createDependsJsExp(depends, context!!)
                nestedMap["dependsOn"] = dependsJsExp
            }
            map["compare"] = nestedMap
            return map
        }

    protected fun getRuleValue(methodName: String): Any {
        return methodName // require content
    }

    init {
        this.compare = compare as Compare
    }
}