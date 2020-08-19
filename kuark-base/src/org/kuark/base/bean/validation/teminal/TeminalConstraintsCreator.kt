package org.kuark.base.bean.validation.teminal

import org.kuark.base.bean.validation.teminal.convert.ConstraintConvertContext
import org.kuark.base.bean.validation.teminal.convert.ConstraintConvertorFactory
import org.kuark.base.data.json.JsonKit
import org.kuark.base.lang.SystemKit
import org.kuark.base.lang.reflect.getDirectSuperClass
import org.kuark.base.lang.reflect.getMemberProperty
import org.kuark.base.lang.reflect.isAnnotationPresent
import javax.validation.Constraint
import javax.validation.Valid
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType

/**
 * 终端约束创建者
 *
 * @author K
 * @since 1.0.0
 */
object TeminalConstraintsCreator {

    private val constrainCacheMap = mutableMapOf<String, String>()

    /**
     * 生成Bean类对应的终端验证规则
     *
     * @param beanClass 待校验的bean类
     * @param propertyPrefix 属性名前缀
     * @return 验证规则json文本
     */
    fun create(beanClass: KClass<*>, propertyPrefix: String = ""): String {
        val cacheKey = "${beanClass.qualifiedName}-$propertyPrefix"
        var rules = constrainCacheMap[cacheKey]
        if (rules == null || SystemKit.isDebug()) {
            val annotations = mutableMapOf<String, MutableList<Annotation>>()
            parseAnnotations(annotations, beanClass, null)
            rules = genRule(annotations, propertyPrefix, beanClass)
            constrainCacheMap[cacheKey] = rules
        }
        return rules
    }

    /**
     * 解析注解
     *
     * @param annotations MutableMap<属性名, MutableList<注解对象>>
     * @param beanClass 待校验的bean类
     * @param parentProperty 父属性对象
     */
    private fun parseAnnotations(
        annotations: MutableMap<String, MutableList<Annotation>>, beanClass: KClass<*>, parentProperty: KProperty<*>?
    ) {
        var clazz = beanClass
        var parentProperty = parentProperty
        annotations.putAll(getAnnotationsOnClass(clazz))

        for (prop in clazz.memberProperties) {
            if (prop.returnType != Any::class.starProjectedType) {
                if (prop.getter.isAnnotationPresent(Valid::class)) { // 级联验证
                    parentProperty = prop
                    val parentClazz = clazz
                    clazz = prop.returnType.classifier as KClass<*>
                    if (clazz == Array<Any>::class) {
                        clazz = clazz.companionObject!!
                    }
                    if (clazz.isSubclassOf(List::class)) {
                        val paramType = prop.typeParameters
                        clazz = paramType[0].starProjectedType.classifier as KClass<*>
                    }
                    if (clazz.isSubclassOf(Map::class)) {
                        val paramType = prop.typeParameters
                        clazz = paramType[1].starProjectedType.classifier as KClass<*>
                    }
                    parseAnnotations(annotations, clazz, parentProperty) // 递归解析所有类中的注解
                    clazz = parentClazz
                    parentProperty = null
                } else {
                    val propName = if (parentProperty == null) {
                        prop.name
                    } else {
                        "'${parentProperty.name}.${prop.name}'"
                    }
                    if (propName.split("\\.").toTypedArray().size > 2) {
                        error("属性嵌套层次超过1层：$propName")
                    }
                    val annoList = getAnnotationsOnGetter(clazz, prop.name)
                    val classAnnoList = annotations[propName]
                    if (classAnnoList != null) {
                        annoList.addAll(classAnnoList)
                    }
                    if (annoList.isNotEmpty()) {
                        annotations[propName] = annoList
                    }
                }
            }
        }
    }

    /**
     * 获取校验规则，按属性合并，并将结果转为json
     *
     * @param annotationsMap MutableMap<属性名, MutableList<注解对象>>
     * @param propertyPrefix 属性名前缀
     * @param beanClass 待校验的bean类
     * @return 验证规则json文本
     */
    private fun genRule(
        annotationsMap: Map<String, MutableList<Annotation>>, propertyPrefix: String, beanClass: KClass<*>
    ): String {
        if (annotationsMap.isEmpty()) {
            return ""
        }
        val ruleMap = mutableMapOf<String, LinkedHashMap<String, Array<Map<String, Any>>>>()
        for ((originalProperty, annotations) in annotationsMap) {
            val property = PropertyResolver.toPotQuote(originalProperty, propertyPrefix)
            val context = ConstraintConvertContext(originalProperty, property, propertyPrefix, beanClass)
            annotations.forEach {
                val converter = ConstraintConvertorFactory.getInstance(it)
                val teminalConstraint = converter.convert(context)
                val map = ruleMap[property] ?: linkedMapOf()
                map[teminalConstraint.constraint] = teminalConstraint.rule
                ruleMap[property] = map
            }
        }
        return JsonKit.toJson(ruleMap)
    }

    /**
     * 获取类级别的约束注解
     *
     * @param clazz Bean类
     * @return Map<属性名，List<约束注解>>
     */
    private fun getAnnotationsOnClass(clazz: KClass<*>): Map<String, MutableList<Annotation>> {
        val annotationMap = mutableMapOf<String, MutableList<Annotation>>()
        for (annotation in clazz.annotations) {
            val prop = annotation::class.getMemberProperty("properties") // 自定义的类级别约束注解中, 代表类属性数组的属性名定义统一用properties
            if (prop != null) {
                val propertyNames = prop.call(annotation) as Array<String>
                propertyNames.forEach { propertyName ->
                    var annoList = annotationMap[propertyName] ?: mutableListOf()
                    annotationMap[propertyName] = annoList
                    if (annotation.annotationClass.isAnnotationPresent(Constraint::class)
                        || annotation.annotationClass.qualifiedName!!.endsWith(".List")
                    ) { // 是约束注解
                        annoList.add(annotation)
                    }
                }
            }
        }
        return annotationMap
    }

    /**
     * 获取Getter上的约束注解
     *
     * @param clazz Bean类
     * @return Map<属性名，List<约束注解>>
     */
    private fun getAnnotationsOnGetter(clazz: KClass<*>, property: String): MutableList<Annotation> {
        val annotationList = mutableListOf<Annotation>()
        if (clazz != Any::class && !clazz.isAbstract) {
            val prop = clazz.getMemberProperty(property)
            if (prop == null) {
                return getAnnotationsOnGetter(clazz.getDirectSuperClass(), property)
            } else {
                val annotations = prop.getter.annotations
                for (annotation in annotations) {
                    if (annotation.annotationClass.isAnnotationPresent(Constraint::class)
                        || annotation.annotationClass.qualifiedName!!.endsWith(".List")
                    ) { // 是约束注解
                        annotationList.add(annotation)
                    }
                }
            }
        }
        return annotationList
    }
}