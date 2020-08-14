package org.kuark.web.form.validation.js.converter.impl

import org.kuark.base.bean.validation.constraint.annotaions.Compare
import org.kuark.web.form.validation.js.converter.AbstractConstraintConverter
import java.lang.reflect.Method

/**
 * Compare注解约束->js约束的转换器
 *
 * @author K
 * @since 1.0.0
 */
class CompareConstraintJsConverter(compare: Annotation) : AbstractConstraintConverter(compare) {

    private val compare = compare as Compare

    override val rulePatternMap: Map<String, Any>
        get() {
            val map = mutableMapOf<String, Any>()
            val nestedMap = mutableMapOf<String, Any>()
            nestedMap["logic"] = compare.logic.toString()
            nestedMap["property"] = compare.anotherProperty
            val formClass = context.formClass
            val depends = compare.depends
            if (StringTool.isNotBlank(compare.isNumber())) {
                nestedMap["isNumber"] = compare.isNumber()
            } else {
                val property = context.originalProperty
                val readMethod: Method = MethodTool.getReadMethod(formClass, property)
                val returnType = readMethod.returnType
                if (returnType == String::class.java) {
                    nestedMap["isNumber"] = "false"
                } else {
                    nestedMap["isNumber"] = "true"
                }
            }
            if (depends.properties.isNotEmpty()) {
                val dependsJsExp = DependsRuleConverter.Companion.createDependsJsExp(depends, context)
                nestedMap["dependsOn"] = dependsJsExp
            }
            map["compare"] = nestedMap
            return map
        }

    override fun getRuleValue(methodName: String): Any {
        return methodName // require content
    }

}