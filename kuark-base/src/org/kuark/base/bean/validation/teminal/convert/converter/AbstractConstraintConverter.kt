package org.kuark.base.bean.validation.teminal.convert.converter

import org.kuark.base.bean.validation.teminal.TeminalConstraint
import org.kuark.base.bean.validation.teminal.convert.ConstraintConvertContext
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

/**
 * 抽象的注解约束->终端约束的转换器
 *
 * @author K
 * @since 1.0.0
 */
abstract class AbstractConstraintConverter(
    /** 约束注解，或者其List注解，比如：Compare.List */
    protected var annotation: Annotation
) : IConstraintConverter {

    protected lateinit var context: ConstraintConvertContext
    private lateinit var constraintAnnotation: Annotation

    override fun convert(context: ConstraintConvertContext): TeminalConstraint {
        this.context = context
        val rules = handleRules()
        val constraint = constraintAnnotation.annotationClass.simpleName!!
        return TeminalConstraint(context.property, constraint, rules)
    }

    protected abstract fun getRule(constraintAnnotation: Annotation): MutableMap<String, Any>

    private fun handleRules(): Array<Map<String, Any>> {
        val rules = mutableListOf<Map<String, Any>>()
        val annotationClass = annotation.annotationClass.qualifiedName!! // 可能是约束注解类，也可能是约束的List注解类
        if (annotationClass.endsWith(".List")) {
            // 为List注解包装具体约束注解的形式，遍历处理每一个具体约束注解
            val annotationsProp = annotation.annotationClass.declaredMemberProperties.first()
            val constraintAnnotations = annotationsProp.call(annotation) as Array<*>
            constraintAnnotations.forEach {
                rules.add(handleRule(it as Annotation))
            }
        } else {
            // 为具体约束注解，没有其List注解包装
            rules.add(handleRule(annotation))
        }
        return rules.toTypedArray()
    }

    private fun handleRule(constraintAnnotation: Annotation): Map<String, Any> {
        this.constraintAnnotation = constraintAnnotation
        val rule = getRule(constraintAnnotation)
        rule["errMsg"] = getErrMsg(constraintAnnotation)
        return rule
    }

    private fun getErrMsg(constraintAnnotation: Annotation): String {
        val annoClass = constraintAnnotation.annotationClass
        val property = annoClass.memberProperties.first { it.name == "message" }
        return property.call(constraintAnnotation) as String
    }

//    private fun recursion(
//        rulePatternMap: Map<String, Any>, nested: Boolean, ruleSb: StringBuilder, msgSb: StringBuilder, errMsg: String?
//    ) {
//        for (entry in rulePatternMap.entries) {
//            val ruleName = entry.key
//            //			ruleName = handlePropertyPrefix(ruleName);
//            var ruleValue = entry.value
//            if (ruleValue is String) {
//                ruleValue = getRuleValue(ruleValue.toString() + "")
//            }
//            ruleSb.append(appendRule(ruleName, ruleValue))
//            msgSb.append(ruleName).append(":'").append(errMsg).append("',")
//            if (ruleValue is Map<*, *>) {
//                recursion(ruleValue as Map<String, Any>, true, ruleSb, msgSb, errMsg)
//            }
//        }
//        if (nested) {
//            ruleSb.deleteCharAt(ruleSb.length - 1)
//            ruleSb.append("}")
//        }
//    }
//
//    protected open fun getRuleValue(methodName: String): Any {
//        val annoClass = annotation.annotationClass
//        val function = annoClass.memberFunctions.first { it.name == methodName }
//        var value = function.call(annotation)
//        if (value is Enum<*>) {
//            value = value.toString()
//        }
//        return value!!
//    }
//
//    companion object {
//        const val PROPERTY_KEY = "property"
//    }
//
//    protected open fun appendRule(ruleName: String, ruleValue: Any): String? {
//        var ruleValue = ruleValue
//        if (ruleValue is String) {
//            // 不是数值和js对象
//            if (!ruleValue.matches("\\d+\\.?\\d*".toRegex()) && !(ruleValue.startsWith("{") && ruleValue.endsWith("}"))) {
//                ruleValue = "'$ruleValue'"
//            }
//            if (ruleName == PROPERTY_KEY) {
//                ruleValue = PropertyResolver.toPotQuote(ruleValue, context.propertyPrefix)
//            }
//        } else if (ruleValue is Map<*, *>) {
//            return "$ruleName:{"
//        }
//        return "$ruleName:$ruleValue,"
//    }
//
//    protected fun resolveProperty(property: String): String {
//        var prop = property
//        if (prop.startsWith("_")) {
//            prop = prop.substring(1)
//        } else {
//            val propertyPrefix = context.propertyPrefix
//            if (propertyPrefix != null && propertyPrefix.isNotBlank() && !prop.contains("_") && !prop.startsWith(
//                    "'$propertyPrefix."
//                )
//            ) {
//                prop = "'$propertyPrefix.$prop'"
//            }
//            // 有带"_"的为表单提交时属性名带"."的
//            prop = prop.replace("_".toRegex(), ".")
//            if (!prop.startsWith("'") && prop.contains(".")) {
//                prop = "'$prop'"
//            }
//        }
//        return prop
//    }

}