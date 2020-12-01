package io.kuark.context.annotation

import com.alibaba.nacos.client.config.utils.MD5
import com.alibaba.nacos.spring.context.event.config.NacosConfigReceivedEvent
import com.alibaba.spring.beans.factory.annotation.AnnotationInjectedBeanPostProcessor
import io.kuark.base.log.LogFactory
import org.springframework.beans.BeansException
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.annotation.InjectionMetadata.InjectedElement
import org.springframework.context.ApplicationListener
import org.springframework.context.EnvironmentAware
import org.springframework.core.MethodParameter
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.*

/
 *
 *
 * 配置项注解处理逻辑
 *
 * @author @author <a href="mailto:huangxiaoyu1018@gmail.com">hxy1991</a>
 * @author K
 * @since 1.0.0
 * @see com.alibaba.nacos.spring.context.annotation.config.NacosValueAnnotationBeanPostProcessor
 */
@Component
class ConfigValueAnnotationBeanPostProcessor :
    AnnotationInjectedBeanPostProcessor<ConfigValue>(),
    BeanFactoryAware, EnvironmentAware, ApplicationListener<NacosConfigReceivedEvent> {

    private val logger = LogFactory.getLog(this::class)
    private val PLACEHOLDER_PREFIX = "\${"
    private val PLACEHOLDER_SUFFIX = "}"
    private val VALUE_SEPARATOR = ":"
    private val placeholderNacosValueTargetMap: MutableMap<String, MutableList<NacosValueTarget>> = HashMap()

    override fun doGetInjectedBean(
        annotation: ConfigValue, bean: Any?, beanName: String?,
        injectedType: Class<*>?, injectedElement: InjectedElement
    ): Any? {
        val annotationValue = annotation.value
        val value = beanFactory.resolveEmbeddedValue(annotationValue)
        val member = injectedElement.member
        if (member is Field) {
            return convertIfNecessary(member, value)
        }
        return if (member is Method) {
            convertIfNecessary(member, value)
        } else null
    }

    override fun buildInjectedObjectCacheKey(
        annotation: ConfigValue, bean: Any, beanName: String?,
        injectedType: Class<*>?, injectedElement: InjectedElement?
    ): String? = bean.javaClass.name + annotation

    @Throws(BeansException::class)
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        doWithFields(bean, beanName)
        doWithMethods(bean, beanName)
        return super.postProcessBeforeInitialization(bean, beanName)
    }

    override fun onApplicationEvent(event: NacosConfigReceivedEvent) {
        for ((key1, beanPropertyList) in placeholderNacosValueTargetMap) {
            val key = environment!!.resolvePlaceholders(key1)
            val newValue = environment!!.getProperty(key) ?: continue
            for (target in beanPropertyList) {
                val md5String = MD5.getInstance().getMD5String(newValue)
                val isUpdate = target.lastMD5 != md5String
                if (isUpdate) {
                    target.updateLastMD5(md5String)
                    if (target.method == null) {
                        setField(target, newValue)
                    } else {
                        setMethod(target, newValue)
                    }
                }
            }
        }
    }

    private fun convertIfNecessary(field: Field?, value: Any?): Any? {
        val converter = beanFactory.typeConverter
        return converter.convertIfNecessary(value, field!!.type, field)
    }

    private fun convertIfNecessary(method: Method, value: Any?): Any? {
        val paramTypes = method.parameterTypes
        val arguments = arrayOfNulls<Any>(paramTypes.size)
        val converter = beanFactory.typeConverter
        if (arguments.size == 1) {
            return converter.convertIfNecessary(value, paramTypes[0], MethodParameter(method, 0))
        }
        for (i in arguments.indices) {
            arguments[i] = converter.convertIfNecessary(value, paramTypes[i], MethodParameter(method, i))
        }
        return arguments
    }

    private fun doWithFields(bean: Any, beanName: String) {
        ReflectionUtils.doWithFields(bean.javaClass) { field ->
            val annotation = AnnotationUtils.getAnnotation(field, ConfigValue::class.java)
            doWithAnnotation(beanName, bean, annotation, field.modifiers, null, field)
        }
    }

    private fun doWithMethods(bean: Any, beanName: String) {
        ReflectionUtils.doWithMethods(bean.javaClass) { method ->
            val annotation = AnnotationUtils.getAnnotation(method, ConfigValue::class.java)
            doWithAnnotation(beanName, bean, annotation, method.modifiers, method, null)
        }
    }

    private fun doWithAnnotation(
        beanName: String, bean: Any, annotation: ConfigValue?,
        modifiers: Int, method: Method?, field: Field?
    ) {
        if (annotation != null) {
            if (Modifier.isStatic(modifiers)) {
                return
            }
            if (annotation.autoRefreshed) {
                val placeholder = resolvePlaceholder(annotation.value) ?: return
                val nacosValueTarget = NacosValueTarget(bean, beanName, method, field)
                put2ListMap(placeholderNacosValueTargetMap, placeholder, nacosValueTarget)
            }
        }
    }

    private fun resolvePlaceholder(placeholder: String): String? {
        var placeholderStr = placeholder
        if (!placeholderStr.startsWith(PLACEHOLDER_PREFIX)) {
            return null
        }
        if (!placeholderStr.endsWith(PLACEHOLDER_SUFFIX)) {
            return null
        }
        if (placeholderStr.length <= PLACEHOLDER_PREFIX.length + PLACEHOLDER_SUFFIX.length) {
            return null
        }
        val beginIndex = PLACEHOLDER_PREFIX.length
        val endIndex = placeholderStr.length - PLACEHOLDER_PREFIX.length + 1
        placeholderStr = placeholderStr.substring(beginIndex, endIndex)
        val separatorIndex = placeholderStr.indexOf(VALUE_SEPARATOR)
        return if (separatorIndex != -1) {
            placeholderStr.substring(0, separatorIndex)
        } else placeholderStr
    }

    private fun <K, V> put2ListMap(map: MutableMap<K, MutableList<V>>, key: K, value: V) {
        var valueList = map[key]
        if (valueList == null) {
            valueList = ArrayList()
        }
        valueList.add(value)
        map[key] = valueList
    }

    private fun setMethod(nacosValueTarget: NacosValueTarget, propertyValue: String) {
        val method = nacosValueTarget.method
        ReflectionUtils.makeAccessible(method!!)
        try {
            method.invoke(nacosValueTarget.bean, convertIfNecessary(method, propertyValue))
            logger.debug("Update value with ${method.name} (method) in ${nacosValueTarget.beanName} (bean) with $propertyValue")
        } catch (e: Throwable) {
            logger.error(e, "Can't update value with ${method.name} (method) in ${nacosValueTarget.beanName} (bean)")
        }
    }

    private fun setField(nacosValueTarget: NacosValueTarget, propertyValue: String) {
        val bean = nacosValueTarget.bean
        val field = nacosValueTarget.field
        val fieldName = field!!.name
        try {
            ReflectionUtils.makeAccessible(field)
            field[bean] = convertIfNecessary(field, propertyValue)
            logger.debug("Update value of the $fieldName" + " (field) in ${nacosValueTarget.beanName} (bean) with $propertyValue")
        } catch (e: Throwable) {
            logger.error(e, "Can't update value of the $fieldName (field) in ${nacosValueTarget.beanName} (bean)")
        }
    }

    private class NacosValueTarget internal constructor(
        val bean: Any, val beanName: String, val method: Method?, val field: Field?
    ) {
        var lastMD5 = ""
        fun updateLastMD5(newMD5: String) {
            lastMD5 = newMD5
        }
    }

}