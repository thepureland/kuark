package org.kuark.web.form.validation.js.converter

import org.kuark.web.form.validation.support.FormPropertyConverter
import java.lang.reflect.Method
import kotlin.reflect.full.memberProperties

/**
 * 抽象的Bean约束转换器
 *
 * @author K
 * @since 1.0.0
 */
abstract class AbstractConstraintConverter(protected var annotation: Annotation) : IConstraintConverter {

    protected var context: RuleConvertContext? = null
    protected abstract val rulePatternMap: Map<String, Any>

    private val errMsg: String
        protected get() {
            val annoClass = annotation.annotationClass
            val first = annoClass.memberProperties.first { it.name == "message" }



            return try {
                val m = annoClass.getMethod()
                m.invoke(annotation).toString() + ""
            } catch (e: Exception) {
                throw SystemException(e)
            }
        }

    protected open fun appendRule(ruleName: String, ruleValue: Any): String? {
        var ruleValue = ruleValue
        if (ruleValue is String) {
            // 不是数值和js对象
            if (!(ruleValue.toString() + "").matches("\\d+\\.?\\d*".toRegex()) && !(ruleValue.startsWith("{") && ruleValue.endsWith(
                    "}"
                ))
            ) {
                ruleValue = "'$ruleValue'"
            }
            if (ruleName == PROPERTY_KEY) {
                ruleValue = FormPropertyConverter.toPotQuote(ruleValue, context.getPropertyPrefix())
            }
        } else if (ruleValue is Map<*, *>) {
            return "$ruleName:{"
        }
        return "$ruleName:$ruleValue,"
    }

    protected fun resolverProperty(property: String): String {
        var property = property
        if (property.startsWith("_")) {
            property = property.substring(1)
        } else {
            val propertyPrefix = context.getPropertyPrefix()
            if (StringTool.isNotBlank(propertyPrefix) && !property.contains("_") && !property.startsWith("'$propertyPrefix.")) {
                property = "'$propertyPrefix.$property'"
            }
            // 有带"_"的为表单提交时属性名带"."的
            property = property.replace("_".toRegex(), ".")
            if (!property.startsWith("'") && property.contains(".")) {
                property = "'$property'"
            }
        }
        return property
    }

    override fun convert(context: RuleConvertContext?): JsConstraintResult {
        this.context = context
        val rulePatternMap = rulePatternMap
        val ruleSb = StringBuilder()
        val msgSb = StringBuilder()
        val errMsg = errMsg
        recursion(rulePatternMap, false, ruleSb, msgSb, errMsg)
        val rule = ruleSb.toString().replaceFirst(",$".toRegex(), "")
        val msg = msgSb.toString().replaceFirst(",$".toRegex(), "")
        return JsConstraintResult(rule, msg)
    }

    protected fun recursion(
        rulePatternMap: Map<String, Any>,
        nested: Boolean,
        ruleSb: StringBuilder,
        msgSb: StringBuilder,
        errMsg: String?
    ) {
        for (entry in rulePatternMap.entries) {
            val ruleName = entry.key
            //			ruleName = handlePropertyPrefix(ruleName);
            var ruleValue = entry.value
            if (ruleValue is String) {
                ruleValue = getRuleValue(ruleValue.toString() + "")
            }
            ruleSb.append(appendRule(ruleName, ruleValue))
            msgSb.append(ruleName).append(":'").append(errMsg).append("',")
            if (ruleValue is Map<*, *>) {
                recursion(ruleValue as Map<String, Any>, true, ruleSb, msgSb, errMsg)
            }
        }
        if (nested) {
            ruleSb.deleteCharAt(ruleSb.length - 1)
            ruleSb.append("}")
        }
    }

    protected open fun getRuleValue(methodName: String?): Any {
        val method: Method = MethodTool.getAccessibleMethod(annotation.annotationType(), methodName)
        return try {
            var value = method.invoke(annotation)
            if (value is Enum<*>) {
                value = value.toString()
            }
            value
        } catch (e: Exception) {
            throw SystemException(e)
        }
    }

    companion object {
        const val PROPERTY_KEY = "property"
    }

}