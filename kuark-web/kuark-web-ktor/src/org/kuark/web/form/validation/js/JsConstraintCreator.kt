package org.kuark.web.form.validation.js

import org.kuark.base.lang.SystemKit
import org.kuark.base.lang.string.uncapitalize
import org.kuark.base.log.LogFactory
import org.kuark.web.form.validation.js.converter.JsConstraint
import org.kuark.web.form.validation.js.converter.ConstraintConvertContext
import org.kuark.web.form.validation.support.FormPropertyConverter
import org.springframework.util.ClassUtils
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.text.MessageFormat
import java.util.*
import javax.validation.Constraint
import javax.validation.Valid
import kotlin.reflect.KClass

/**
 * js约束创建者
 *
 * @author K
 * @since 1.0.0
 */
object JsConstraintCreator {

    private val LOG = LogFactory.getLog(JsConstraintCreator::class)
    private val jsRuleMap = mutableMapOf<String, String>()
    private const val RESULT_PATTERN = "rules:'{'{0}},messages:'{'{1}'},"

    /**
     * 生成表单类对应的表单验证规则
     *
     * @param formClass 表单类
     * @param propertyPrefix 属性名前缀
     * @return 验证规则文本，包含错误提示信息，最后以逗号结尾
     */
    fun create(formClass: KClass<*>, propertyPrefix: String? = null): String? {
        var propertyPrefix = propertyPrefix
        if (propertyPrefix == null) {
            propertyPrefix = ""
        }
        val className = formClass.qualifiedName
        val key = "$className-$propertyPrefix"
        var jsRule = jsRuleMap[key]
        if (jsRule == null || SystemKit.isDebug()) {
            val annotations = mutableMapOf<String, MutableList<Annotation>>() // Map<属性名, List<getter上的注解对象>>
            parseAnnotations(annotations, formClass, null)
            jsRule = genRule(annotations, propertyPrefix, formClass)
            jsRuleMap[key] = jsRule
        }
        return jsRule
    }

    fun parseAnnotations(
        annotations: MutableMap<String, MutableList<Annotation>>, clazz: KClass<*>, parentProperty: String?
    ) {
        var clazz = clazz
        var parentProperty = parentProperty
        val classAnnotations = getAnnotations(clazz)
        annotations.putAll(classAnnotations)

        val readMethods: List<Method> = MethodTool.getReadMethods(clazz)
        for (getter in readMethods) {
            if (getter.declaringClass != Any::class.java && getter.parameterCount == 0) {
                val getterName = getter.name
                var property = getterName.replaceFirst("^is|^get".toRegex(), "").uncapitalize()
                if (getter.isAnnotationPresent(Valid::class.java)) { // 嵌套表单
                    parentProperty = property
                    val parentClazz = clazz
                    clazz = getter.returnType
                    if (clazz.isArray) {
                        clazz = clazz.componentType
                    }
                    if (clazz.isAssignableFrom(MutableList::class.java)) {
                        val paramType =
                            getter.genericReturnType as ParameterizedType
                        clazz = paramType.actualTypeArguments[0] as Class<*>
                    }
                    if (clazz.isAssignableFrom(MutableMap::class.java)) {
                        val paramType =
                            getter.genericReturnType as ParameterizedType
                        clazz = paramType.actualTypeArguments[1] as Class<*>
                    }
                    parseAnnotations(annotations, clazz, parentProperty)
                    clazz = parentClazz
                    parentProperty = null
                } else {
                    property = if (parentProperty == null) property else "'$parentProperty.$property'"
                    if (property.split("\\.").toTypedArray().size > 2) {
                        error("属性嵌套层次超过1层：$property")
                    }
                    val annoList =
                        getAnnotations(clazz, getterName)
                    val classAnnoList: List<Annotation>? = annotations[property]
                    if (classAnnoList != null) {
                        annoList.addAll(classAnnoList)
                    }
                    if (annoList.isNotEmpty()) {
                        annotations[property] = annoList
                    }
                }
            }
        }
    }

    private fun genRule(
        annotations: Map<String, MutableList<Annotation>>, propertyPrefix: String, formClass: KClass<*>
    ): String {
        if (annotations.isEmpty()) {
            return ""
        }
        val rules = StringBuilder()
        val messages = StringBuilder()
        for ((originalProperty, value) in annotations) {
            val property = FormPropertyConverter.toPotQuote(originalProperty, propertyPrefix)
            rules.append(property).append(":{")
            messages.append(property).append(":{")
            for (anno in value) {
                val converter = RuleConverterFactory.getInstance(anno)
                val context = ConstraintConvertContext(originalProperty, property, propertyPrefix, formClass)
                val ruleResult: JsConstraint = converter.convert(context)
                rules.append(ruleResult.rule).append(",")
                messages.append(ruleResult.msg).append(",")
            }
            try {
                val fieldCLass = formClass.getDeclaredField(property).type
                if (fieldCLass.isArray || fieldCLass.isAssignableFrom(MutableList::class.java)) {
                    rules.append("type:'array'").append(",")
                }
            } catch (e: NoSuchFieldException) {
            }
            rules.deleteCharAt(rules.length - 1).append("},")
            messages.deleteCharAt(messages.length - 1).append("},")
        }
        return MessageFormat.format(
            RESULT_PATTERN,
            rules.deleteCharAt(rules.length - 1),
            messages.deleteCharAt(messages.length - 1)
        )
    }

    private fun getAnnotations(clazz: Class<*>): Map<String, MutableList<Annotation>> {
        val annoMap: MutableMap<String, MutableList<Annotation>> =
            HashMap()
        val annotations = clazz.annotations
        for (annotation in annotations) {
            val propMethod: Method =
                MethodTool.getAccessibleMethod(annotation.annotationType(), "property")
            if (propMethod != null) {
                try {
                    val property = propMethod.invoke(annotation) as String
                    var annoList = annoMap[property]
                    if (annoList == null) {
                        annoList = ArrayList(1)
                        annoMap[property] = annoList
                    }
                    val annos: Array<Annotation> = annotation.annotationType().getAnnotations()
                    for (anno in annos) {
                        if (anno.annotationType() == Constraint::class.java) {
                            annoList.add(annotation)
                            break
                        }
                    }
                } catch (e: Exception) {
                    LOG.error(e, e.message)
                }
            }
        }
        return annoMap
    }

    private fun getAnnotations(
        clazz: Class<*>,
        getter: String
    ): MutableList<Annotation> {
        val annoList: MutableList<Annotation> = ArrayList(1)
        if (clazz != Any::class.java && !clazz.isInterface) {
            /**
             * 将apache的MethodUtil调整为spring的ClassUtils
             * MethodUtil在类不是public的时候取不到相关method方法。
             * @author admin
             */
            //Method method = MethodUtils.getAccessibleMethod(clazz, getter);
            val method = ClassUtils.getMethod(clazz, getter)
            val superclass = clazz.superclass
            if (method == null) {
                return getAnnotations(superclass, getter)
            } else {
                val annotations = method.annotations
                for (annotation in annotations) {
                    val annoClazz: Class<out Annotation> = annotation.annotationType()
                    val present = annoClazz.isAnnotationPresent(Constraint::class.java)
                    if (present) {
                        annoList.add(annotation)
                    }
                }
            }
        }
        return annoList
    }
}