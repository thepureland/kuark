package org.kuark.web.form.validation.js.converter.impl

import org.kuark.web.form.validation.js.converter.AbstractConstraintConverter
import org.kuark.web.form.validation.js.converter.ConstraintConvertContext
import org.soul.commons.lang.string.StringTool
import org.soul.commons.query.enums.Operator
import org.soul.commons.validation.form.support.AndOr
import org.soul.commons.validation.form.support.FormPropertyConverter
import org.soul.model.common.constraints.Depends
import org.soul.web.validation.form.export.ValidateRuleExporter
import org.soul.web.validation.form.js.JsRuleCreator
import org.soul.web.validation.form.js.converter.AbstractRuleConverter
import org.soul.web.validation.form.js.converter.IRuleConverter
import org.soul.web.validation.form.js.converter.RuleConvertContext
import java.text.MessageFormat
import java.util.*

/**
 * @author admin
 * @time 8/13/15 9:05 PM
 */
class DependsRuleConverter(annotation: Annotation) : AbstractConstraintConverter(annotation) {
    private val depends: Depends
    protected override val rulePatternMap: Map<String, Any>
        protected get() {
            val map: MutableMap<String, Any> = HashMap(1, 1f)
            val jsExps = createDependsJsExp(depends, context!!)
            map["required"] = MessageFormat.format(RULE_PATTERN, jsExps)
            return map
        }

    protected fun getRuleValue(methodName: String): Any {
        return methodName // require content
    }

    companion object {
        private const val EXP_PATTERN = "$(\"[name={0}]\").val()"
        private const val RULE_PATTERN = "'{'" +
                "   depends: function(elem) '{'" +
                "       return {0};" +
                "   }" +
                "}"

        fun createDependsJsExp(depends: Depends, context: ConstraintConvertContext): String {
            var jsExps = "" // 绝大多数情况下只有一个表达式
            val properties: Array<String> = depends.property()
            val jsValueExps: Array<String> = depends.jsValueExp()
            for (i in properties.indices) {
                val property = properties[i]
                var jsValueExp: String? = null
                if (jsValueExps.size > i) {
                    jsValueExp = jsValueExps[i]
                }
                val jsExp = getJsExp(
                    property,
                    depends.operator().get(i),
                    depends.value().get(i),
                    jsValueExp,
                    context
                )
                if (jsExps.isEmpty()) {
                    jsExps = jsExp
                } else {
                    val andOr = if (depends.andOr() === AndOr.AND) " && " else " || "
                    jsExps += andOr + jsExp
                }
            }
            return jsExps
        }

        private fun getJsExp(
            property: String,
            operator: Operator,
            value: String,
            jsValueExp: String?,
            context: ConstraintConvertContext
        ): String {
            var value: String? = value
            var propValExp =
                if (StringTool.isBlank(jsValueExp)) EXP_PATTERN else jsValueExp!!.replace(
                    "'".toRegex(),
                    "''"
                )
            var prop: String = FormPropertyConverter.toPotQuote(property, context.propertyPrefix)
            if (prop.startsWith("'")) {
                prop = "\\" + prop.substring(0, prop.length - 1) + "\\'" // 加反斜杠转义单引号，防止多重嵌套
            }
            propValExp = MessageFormat.format(propValExp, prop)
            if (value != null && "true" != value && "false" != value && !value.startsWith("[") && !value.endsWith(
                    "]"
                )
            ) {
                value = "\"" + value + "\""
            }
            when (operator) {
                EQ -> propValExp = "$propValExp == $value"
                IEQ -> propValExp = "$propValExp.toLowerCase() == $value.toLowerCase()"
                NE, LG -> propValExp = "$propValExp != $value"
                GE -> propValExp = "$propValExp >= $value"
                LE -> propValExp = "$propValExp <= $value"
                GT -> propValExp = "$propValExp > $value"
                LT -> propValExp = "$propValExp < $value"
                LIKE -> propValExp = "$propValExp.indexOf($value) != -1"
                LIKE_S -> propValExp = "$propValExp.trim().indexOf($value) == 0"
                LIKE_E -> propValExp = "$propValExp.trim().endsWith($value)"
                ILIKE -> propValExp = "$propValExp.toLowerCase().indexOf($value.toLowerCase()) != -1"
                ILIKE_S -> propValExp = "$propValExp.toLowerCase().indexOf($value.toLowerCase()) == 0"
                ILIKE_E -> propValExp = "$propValExp.toLowerCase().endsWith($value.toLowerCase())"
                IN -> propValExp = if (value!!.startsWith("[") && value.endsWith("]")) {
                    "$.inArray($propValExp, $value) != -1"
                } else {
                    "$propValExp.indexOf($value) != -1"
                }
                IS_NULL -> propValExp = "!$propValExp"
                IS_EMPTY -> propValExp = "$propValExp == ''"
                else -> {
                }
            }
            return propValExp
        }
    }

    init {
        depends = annotation as Depends
    }
}