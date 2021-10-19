package io.kuark.base.bean.validation.teminal.convert.converter

import io.kuark.base.bean.validation.teminal.TeminalConstraint
import io.kuark.base.bean.validation.teminal.convert.ConstraintConvertContext
import kotlin.reflect.full.declaredMemberProperties

/**
 * 抽象的注解约束->终端约束的转换器
 *
 * @author K
 * @since 1.0.0
 */
abstract class AbstractConstraintConvertor(
    /** 约束注解，或者其List注解，比如：Compare.List */
    protected var annotation: Annotation
) : IConstraintConvertor {

    protected lateinit var context: ConstraintConvertContext
    private lateinit var constraintAnnotation: Annotation

    override fun convert(context: ConstraintConvertContext): TeminalConstraint {
        this.context = context
        val rules = handleRules()
        val constraint = constraintAnnotation.annotationClass.simpleName!!
        return TeminalConstraint(context.property, constraint, rules)
    }

    /**
     * 返回具体约束注解的规则
     *
     * @param constraintAnnotation 具体约束注解(不会是其内部注解List)
     * @return LinkedHashMap<注解属性名，注解属性值>
     */
    protected abstract fun getRule(constraintAnnotation: Annotation): LinkedHashMap<String, Any>

    /**
     * 处理约束注解（可能是约束注解，也可能是约束的内部注解List）
     *
     * @return Array<Map<注解属性名，注解属性值>>
     */
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

    /**
     * 处理具体约束注解的规则
     *
     * @param constraintAnnotation 具体约束注解(不会是其内部注解List)
     * @return Map<注解属性名，注解属性值>
     */
    private fun handleRule(constraintAnnotation: Annotation): Map<String, Any> {
        this.constraintAnnotation = constraintAnnotation
        val rule = getRule(constraintAnnotation)
        handleMessageI18n(rule)
        return rule
    }

    /**
     * 处理错误消息的国际化
     *
     * @param rule Map<注解属性名，注解属性值>
     */
    private fun handleMessageI18n(rule: Map<String, Any>) {
        println(rule) //TODO i18n
    }

}