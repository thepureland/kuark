package io.kuark.base.bean.validation.teminal

import io.kuark.base.bean.validation.teminal.convert.ConstraintConvertContext
import io.kuark.base.bean.validation.teminal.convert.ConstraintConvertorFactory
import io.kuark.base.data.json.JsonKit
import io.kuark.base.lang.SystemKit
import io.kuark.base.lang.reflect.getMemberProperty
import io.kuark.base.lang.reflect.getSuperClass
import io.kuark.base.lang.reflect.isAnnotationPresent
import io.kuark.base.support.Consts
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

    /**
     * Map(Bean类名-属性名前缀，Map(属性名， LinkedHashMap(约束名，Array(Map(约束注解的属性名，约束注解的属性值)))))
     */
    private val constrainCacheMap = mutableMapOf<String, Map<String, LinkedHashMap<String, Array<Map<String, Any>>>>>()

    /**
     * 生成Bean类对应的终端验证规则
     *
     * @param beanClass 待校验的bean类
     * @param propertyPrefix 属性名前缀
     * @return Map(属性名， LinkedHashMap(约束名，Array(Map(约束注解的属性名，约束注解的属性值))))
     * @author K
     * @since 1.0.0
     */
    fun create(beanClass: KClass<*>, propertyPrefix: String = ""): Map<String, LinkedHashMap<String, Array<Map<String, Any>>>> {
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
     * @author K
     * @since 1.0.0
     */
    private fun parseAnnotations(
        annotations: MutableMap<String, MutableList<Annotation>>, beanClass: KClass<*>, parentProperty: KProperty<*>?
    ) {
        var clazz = beanClass
        var parentProp = parentProperty
        annotations.putAll(getAnnotationsOnClass(clazz))

        for (prop in clazz.memberProperties) {
            if (prop.returnType != Any::class.starProjectedType) {
                if (prop.getter.isAnnotationPresent(Valid::class)) { // 级联验证
                    parentProp = prop
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
                    parseAnnotations(annotations, clazz, parentProp) // 递归解析所有类中的注解
                    clazz = parentClazz
                    parentProp = null
                } else {
                    val propName = if (parentProp == null) {
                        prop.name
                    } else {
                        "'${parentProp.name}.${prop.name}'"
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
     * @author K
     * @since 1.0.0
     */
    private fun genRule(
        annotationsMap: Map<String, MutableList<Annotation>>, propertyPrefix: String, beanClass: KClass<*>
    ): Map<String, LinkedHashMap<String, Array<Map<String, Any>>>> {
        if (annotationsMap.isEmpty()) {
            return emptyMap()
        }
        val ruleMap = mutableMapOf<String, LinkedHashMap<String, Array<Map<String, Any>>>>()
        for ((originalProperty, annotations) in annotationsMap) {
            val property = PropertyResolver.toPotQuote(originalProperty, propertyPrefix)
            val context = ConstraintConvertContext(originalProperty, property, propertyPrefix, beanClass)
            annotations.forEach {
                val converter = ConstraintConvertorFactory.getInstance(it)
                if (converter != null) { // 为null不需要返回给终端
                    val teminalConstraint = converter.convert(context)
                    val map = ruleMap[property] ?: linkedMapOf()
                    map[teminalConstraint.constraint] = teminalConstraint.rule
                    ruleMap[property] = map
                }
            }
        }
        return ruleMap
    }

    /**
     * 获取类级别的约束注解
     *
     * @param clazz Bean类
     * @return Map(属性名，List(约束注解))
     * @author K
     * @since 1.0.0
     */
    @Suppress(Consts.Suppress.UNCHECKED_CAST)
    private fun getAnnotationsOnClass(clazz: KClass<*>): Map<String, MutableList<Annotation>> {
        val annotationMap = mutableMapOf<String, MutableList<Annotation>>()
        for (annotation in clazz.annotations) {
            val prop = annotation::class.getMemberProperty("properties") // 自定义的类级别约束注解中, 代表类属性数组的属性名定义统一用properties
            val propertyNames = prop.call(annotation) as Array<String>
            propertyNames.forEach { propertyName ->
                val annoList = annotationMap[propertyName] ?: mutableListOf()
                annotationMap[propertyName] = annoList
                if (annotation.annotationClass.isAnnotationPresent(Constraint::class)
                    || annotation.annotationClass.qualifiedName!!.endsWith(".List")
                ) { // 是约束注解
                    annoList.add(annotation)
                }
            }
        }
        return annotationMap
    }

    /**
     * 获取Getter上的约束注解
     *
     * @param clazz Bean类
     * @return Map(属性名，List(约束注解))
     * @author K
     * @since 1.0.0
     */
    private fun getAnnotationsOnGetter(clazz: KClass<*>, property: String): MutableList<Annotation> {
        val annotationList = mutableListOf<Annotation>()
        if (clazz != Any::class && !clazz.isAbstract) {
            try {
                val prop = clazz.getMemberProperty(property)
                val annotations = prop.getter.annotations
                for (annotation in annotations) {
                    if (annotation.annotationClass.isAnnotationPresent(Constraint::class)
                        || annotation.annotationClass.qualifiedName!!.endsWith(".List")
                    ) { // 是约束注解
                        annotationList.add(annotation)
                    }
                }
            } catch (e: Exception) {
                return getAnnotationsOnGetter(clazz.getSuperClass()!!, property)
            }
        }
        return annotationList
    }
}