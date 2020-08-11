package org.kuark.web.form.validation.js.converter.impl

import org.kuark.base.bean.validation.constraint.annotaions.AtLeast
import org.kuark.web.form.validation.js.converter.AbstractConstraintConverter
import java.lang.reflect.Method
import java.text.MessageFormat
import java.util.*

/**
 *
 *
 * @author admin
 * @time 8/15/15 10:45 AM
 */
class AtLeastConverter(annotation: Annotation) : AbstractConstraintConverter(annotation) {

    private val atLeast: AtLeast

    // Map<属性名,获取javascript值的表达式>
    protected override val rulePatternMap: Map<String, Any>
        protected get() {
            // Map<属性名,获取javascript值的表达式>
            val properties = fetchSameGroupProperties()
            val sb = StringBuilder()
            for (entry in properties.entries) {
                var property = entry.key
                property = FormPropertyConverter.toPotQuote(property, context.getPropertyPrefix())
                val jsValueExp = entry.value
                var pattern = EXP_PATTERN
                if (properties.size == 1) {
                    pattern = EXP_PATTERN_ONE_ATTR
                    val propValExp = if (StringTool.isBlank(jsValueExp)) pattern else jsValueExp
                    val propertyStart = property.substring(0, property.indexOf("]"))
                    val propertyEnd = property.substring(property.indexOf("]"), property.length)
                    sb.append(MessageFormat.format(propValExp, "$propertyStart'", "'$propertyEnd")).append(",")
                } else {
                    val propValExp = if (StringTool.isBlank(jsValueExp)) pattern else jsValueExp
                    sb.append(MessageFormat.format(propValExp, property)).append(",")
                }
            }
            sb.deleteCharAt(sb.length - 1)
            val map: MutableMap<String, Any> = HashMap(1, 1f)
            map["required"] = MessageFormat.format(RULE_PATTERN, sb.toString(), atLeast.count())
            return map
        }

    protected fun getRuleValue(methodName: String): Any {
        // require content
        return methodName
    }

    private fun fetchSameGroupProperties(): Map<String, String> {
        val properties: MutableMap<String, String> =
            HashMap()
        val readMethods: List<Method> =
            MethodTool.getReadMethods(context.getFormClass())
        for (readMethod in readMethods) {
            if (readMethod.isAnnotationPresent(AtLeast::class.java)) {
                val annotation: AtLeast = readMethod.getAnnotation(AtLeast::class.java)
                if (ArrayTool.containsAny(atLeast.groups(), annotation.groups())) {
                    properties[MethodTool.getReadProperty(readMethod)] = annotation.jsValueExp()
                }
            }
        }
        return properties
    }

    companion object {
        private const val EXP_PATTERN = "$(\"[name={0}]\").val()"
        private const val EXP_PATTERN_ONE_ATTR = "$(\"[name ^= {0} ][name $= {1} ]\")"
        private const val RULE_PATTERN = "'{'" +
                "   depends: function(elem) '{'" +
                "       return page.testNotBlankCount([{0}], {1});" +
                "   }" +
                "}"
    }

    init {
        atLeast = annotation as AtLeast
    }
}