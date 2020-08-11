package org.kuark.web.form.validation.js

import org.kuark.web.form.validation.js.converter.JsConstraintResult
import org.kuark.web.form.validation.js.converter.RuleConvertContext
import org.soul.commons.collections.MapTool
import org.soul.commons.exception.SystemException
import org.soul.commons.lang.SystemTool
import org.soul.commons.lang.reflect.MethodTool
import org.soul.commons.lang.string.StringTool
import org.soul.commons.log.Log
import org.soul.commons.log.LogFactory
import org.soul.commons.validation.form.support.FormPropertyConverter
import org.soul.web.validation.form.export.ValidateRuleExporter
import org.soul.web.validation.form.js.JsRuleCreator
import org.soul.web.validation.form.js.converter.IRuleConverter
import org.soul.web.validation.form.js.converter.RuleConvertContext
import org.springframework.util.ClassUtils
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.text.MessageFormat
import java.util.*
import javax.validation.Constraint
import javax.validation.Valid

/**
 * Create by (admin) on 2015/1/20.
 */
object JsRuleCreator {
    private val LOG: Log = LogFactory.getLog(JsRuleCreator::class.java)
    private val jsRuleMap: MutableMap<String, String?> =
        HashMap()
    private const val RESULT_PATTERN = "rules:'{'{0}},messages:'{'{1}'},"
    /**
     * 生成表单类对应的表单验证规则
     *
     * @param formClass 表单类
     * @param propertyPrefix 属性名前缀
     * @return 验证规则文本，包含错误提示信息，最后以逗号结尾
     */
    /**
     * 生成表单类对应的表单验证规则
     *
     * @param formClass 表单类
     * @return 验证规则文本，包含错误提示信息，最后以逗号结尾
     */
    @JvmOverloads
    fun create(formClass: Class<*>, propertyPrefix: String? = null): String? {
        var propertyPrefix = propertyPrefix
        if (propertyPrefix == null) {
            propertyPrefix = ""
        }
        val className = formClass.name
        val key = "$className-$propertyPrefix"
        var jsRule = jsRuleMap[key]
        if (jsRule == null || SystemTool.isDebug()) {
            val annotations: MutableMap<String, MutableList<Annotation>> =
                HashMap() // Map<属性名, List<getter上的注解对象>>
            parseAnnotations(annotations, formClass, null)
            jsRule = genRule(annotations, propertyPrefix, formClass)
            jsRuleMap[key] = jsRule
        }
        return jsRule
    }

    fun parseAnnotations(
        annotations: MutableMap<String, MutableList<Annotation>>,
        clazz: Class<*>,
        parentProperty: String?
    ) {
        var clazz = clazz
        var parentProperty = parentProperty
        val classAnnotations =
            getAnnotations(clazz)
        annotations.putAll(classAnnotations)
        val readMethods: List<Method> = MethodTool.getReadMethods(clazz)
        for (getter in readMethods) {
            if (getter.declaringClass != Any::class.java && getter.parameterCount == 0) {
                val getterName = getter.name
                var property: String = StringTool.uncapitalize(getterName.replaceFirst("^is|^get".toRegex(), ""))
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
                        throw SystemException("属性嵌套层次超过1层：$property")
                    }
                    val annoList =
                        getAnnotations(clazz, getterName)
                    val classAnnoList: List<Annotation>? = annotations[property]
                    if (classAnnoList != null) {
                        annoList.addAll(classAnnoList)
                    }
                    if (!annoList.isEmpty()) {
                        annotations[property] = annoList
                    }
                }
            }
        }
    }

    private fun genRule(
        annotations: Map<String, MutableList<Annotation>>,
        propertyPrefix: String,
        formClass: Class<*>
    ): String {
        if (MapTool.isEmpty(annotations)) {
            return ""
        }
        val rules = StringBuilder()
        val messages = StringBuilder()
        for ((originalProperty, value) in annotations) {
            val property: String = FormPropertyConverter.toPotQuote(originalProperty, propertyPrefix)
            rules.append(property).append(":{")
            messages.append(property).append(":{")
            for (anno in value) {
                val converter: IRuleConverter = RuleConverterFactory.getInstance(anno)
                val context =
                    RuleConvertContext(originalProperty, property, propertyPrefix, formClass)
                val ruleResult: JsConstraintResult = converter.convert(context)
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