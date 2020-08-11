package org.kuark.web.form.validation.js.converter.impl

import org.kuark.web.form.validation.js.converter.AbstractConstraintConverter
import org.soul.commons.exception.SystemException
import org.soul.commons.lang.ArrayTool
import org.soul.commons.lang.string.StringTool
import org.soul.commons.spring.ServletTool
import org.soul.commons.spring.SpringTool
import org.soul.commons.validation.form.support.FormPropertyConverter
import org.soul.model.common.constraints.Remote
import org.soul.web.validation.form.export.ValidateRuleExporter
import org.soul.web.validation.form.js.JsRuleCreator
import org.soul.web.validation.form.js.converter.AbstractRuleConverter
import org.soul.web.validation.form.js.converter.IRuleConverter
import org.soul.web.validation.form.js.converter.RuleConvertContext
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.lang.reflect.Method
import java.text.MessageFormat
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * Create by (admin) on 2015/2/16.
 */
class RemoteRuleConverter(annotation: Annotation) : AbstractConstraintConverter(annotation) {
    private val remote: Remote
    private var selecor = ""
    protected override val rulePatternMap: Map<String, Any>
        protected get() {
            val map: MutableMap<String, Any> = HashMap(1, 1f)
            map["remote"] = action
            return map
        }

    protected fun getRuleValue(methodName: String): Any {
        return methodName // action
    }

    override fun appendRule(ruleName: String, ruleValue: Any): String {
        var ruleValue = ruleValue
        if ((ruleValue as String).startsWith("{") == false) {
            ruleValue = "'$ruleValue'"
        }
        return "$ruleName:$ruleValue"
    }

    /**
     * 表单选择器.
     *
     * @return
     */
    private fun selector(): String {
        var formId: String = remote.formId()
        val type: String = remote.type()
        if (StringTool.isNotBlank(formId)) {
            formId = "#$formId"
        }
        return if (type != null && type.length > 0) {
            MessageFormat.format(EXP_SELECTOR, formId, " ", type)
        } else MessageFormat.format(EXP_SELECTOR, formId, "", type)
    }

    protected fun getRule(contextPath: String): String {
        var clazz: Class<*> = remote.checkClass()
        if (clazz.isInterface) {
            clazz = try {
                SpringTool.getBean(clazz).getClass().getSuperclass()
            } catch (e: Exception) {
                throw SystemException("没有找到【{0}】的实现类!!!", clazz)
            }
        }
        val methodName: String = remote.checkMethod()
        val method = getMethod(clazz, methodName)
        return if (method == null) {
            throw SystemException(
                "在类【{0}】中找不到名称为【{1}】，返回值类型为String，且参数为以下情况的方法：" + "带有HttpServletRequest类型或用RequestParam注解标注参数名！",
                clazz,
                methodName
            )
        } else {
            val classAction = getClassAction(clazz)
            val methodAction = getMethodAction(clazz, method)
            val action = "$contextPath$classAction$methodAction.html"
            val additionalProperties: Array<String> = remote.additionalProperties()
            val jsValueExps: Array<String> = remote.jsValueExp()
            val properties: Array<String> = ArrayTool.add(additionalProperties, 0, context.getProperty())
            multiPropertiesRule(action, properties, jsValueExps)

//            if (additionalProperties.length == 0) {
//                return action;
//            } else {
//                String[] properties = ArrayTool.add(additionalProperties, 0, context.getProperty());
//                return multiPropertiesRule(action, properties);
//            }
        }
    }

    private fun multiPropertiesRule(
        action: String,
        additionalProperties: Array<String>,
        jsValueExps: Array<String>
    ): String {
        val propertyPattern = "          {0}: function() '{'" +
                "            return {1};" +
                "          }"
        val propData = arrayOfNulls<String>(additionalProperties.size)
        for (i in additionalProperties.indices) {
            val property: String =
                FormPropertyConverter.toPotQuote(additionalProperties[i], context.getPropertyPrefix())
            if (i != 0 && jsValueExps.size >= i && StringTool.isNotBlank(jsValueExps[i - 1])) {
                propData[i] = MessageFormat.format(propertyPattern, property, jsValueExps[i - 1])
            } else {
                propData[i] = MessageFormat.format(
                    propertyPattern,
                    property,
                    MessageFormat.format(EXP_PATTERN, selecor, property)
                )
            }
            //            String property = resolverProperty(additionalProperties[i]);
        }
        val pattern = "      '{'" +
                "        url: ''{0}''," +
                "        cache: false," +
                "        type: ''POST''," +
                "        data: '{'" +
                "           {1}" +
                "        }" +
                "      }"
        return MessageFormat.format(pattern, action, StringTool.join(",", propData)).trim()
    }

    //        String contextPath = ServletTool.getRootPath(SpringBeanTool.getRequest());
    //页面缓存刷新时,新开线程获取上下文为空,默认赋值未''
    private val action: String
        private get() {
//        String contextPath = ServletTool.getRootPath(SpringBeanTool.getRequest());
            var contextPath = ""
            //页面缓存刷新时,新开线程获取上下文为空,默认赋值未''
            try {
                contextPath = ServletTool.getRequest().getContextPath()
            } catch (e: Exception) {
            }
            return getRule(contextPath)
        }

    private fun getMethod(clazz: Class<*>, methodName: String): Method? {
        val methods = clazz.methods
        for (m in methods) {
            if (m.name == methodName && (m.returnType == String::class.java || m.returnType == Boolean::class.javaPrimitiveType || m.returnType == Boolean::class.java)) {
                if (m.isAnnotationPresent(RequestMapping::class.java)) {
                    val parameterAnnotations =
                        m.parameterAnnotations
                    for (i in parameterAnnotations.indices) {
                        val paramAnno = parameterAnnotations[i]
                        if (paramAnno.size > 0) {
                            for (j in paramAnno.indices) {
                                if (paramAnno[j] is RequestParam) {
                                    return m
                                }
                            }
                        }
                    }
                    val parameterTypes = m.parameterTypes
                    val contains: Boolean = ArrayTool.contains(parameterTypes, HttpServletRequest::class.java)
                    if (contains) {
                        return m
                    }
                }
            }
        }
        return null
    }

    private fun getClassAction(clazz: Class<*>): String {
        val anno: Annotation = clazz.getAnnotation(RequestMapping::class.java)
        if (anno != null) {
            val mapping: RequestMapping = anno as RequestMapping
            val value: Array<String> = mapping.value()
            return if (value.size == 0 || StringTool.isBlank(value[0])) {
                throw SystemException("类【{0}】的RequestMapping注解value为空！", clazz)
            } else {
                value[0]
            }
        }
        return ""
    }

    private fun getMethodAction(clazz: Class<*>, method: Method): String? {
        val annotations = method.declaredAnnotations
        for (anno in annotations) {
            if (anno is RequestMapping) {
                val mapping: RequestMapping = anno as RequestMapping
                val value: Array<String> = mapping.value()
                return if (value.size == 0 || StringTool.isBlank(value[0])) {
                    throw SystemException("类【{0}】中方法【{1}】的RequestMapping注解value为空！", clazz, method.name)
                } else {
                    value[0]
                }
            }
        }
        return null
    }

    companion object {
        private const val EXP_PATTERN = "$(\"{0}[name={1}]\").val()"
        private const val EXP_SELECTOR = "{0}{1}{2}"
    }

    init {
        remote = annotation as Remote
        selecor = selector()
    }
}