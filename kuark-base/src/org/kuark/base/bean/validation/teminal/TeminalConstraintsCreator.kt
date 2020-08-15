package org.kuark.base.bean.validation.teminal

import org.kuark.base.bean.validation.teminal.convert.ConstraintConvertContext
import org.kuark.base.bean.validation.teminal.convert.ConstraintConverterFactory
import org.kuark.base.bean.validation.teminal.convert.converter.impl.CompareConstraintJsConverter
import org.kuark.base.lang.SystemKit
import org.kuark.base.lang.getDirectSuperClass
import org.kuark.base.lang.getMemberProperty
import org.kuark.base.lang.isAnnotationPresent
import org.kuark.base.log.LogFactory
import java.text.MessageFormat
import javax.validation.Constraint
import javax.validation.Valid
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.*

/**
 * 终端约束创建者
 *
 * @author K
 * @since 1.0.0
 */
object TeminalConstraintsCreator {

    private val LOG = LogFactory.getLog(TeminalConstraintsCreator::class)
    private val constrainCacheMap = mutableMapOf<String, String>()
    private const val RESULT_PATTERN = "rules:'{'{0}},messages:'{'{1}'},"

    /**
     * 生成Bean类对应的终端验证规则
     *
     * @param beanClass 待校验的bean类
     * @param propertyPrefix 属性名前缀
     * @return 验证规则文本，包含错误提示信息，最后以逗号结尾
     */
    fun create(beanClass: KClass<*>, propertyPrefix: String = ""): String {
        val cacheKey = "${beanClass.qualifiedName}-$propertyPrefix"
        var teminalConstraints = constrainCacheMap[cacheKey]
        if (teminalConstraints == null || SystemKit.isDebug()) {
            val annotations = mutableMapOf<String, MutableList<Annotation>>() // Map<属性名, List<getter上的注解对象>>
            parseAnnotations(annotations, beanClass, null)
            println(annotations)
//            teminalConstraints = genRule(annotations, propertyPrefix, beanClass)
//            constrainCacheMap[cacheKey] = teminalConstraints
        }
        return ""
    }

    fun parseAnnotations(
        annotations: MutableMap<String, MutableList<Annotation>>, clazz: KClass<*>, parentProperty: KProperty<*>?
    ) {
        var clazz = clazz
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

//    private fun genRule(
//        annotations: Map<String, MutableList<Annotation>>, propertyPrefix: String, beanClass: KClass<*>
//    ): String {
//        if (annotations.isEmpty()) {
//            return ""
//        }
//        val rules = StringBuilder()
//        val messages = StringBuilder()
//        for ((originalProperty, value) in annotations) {
//            val property = PropertyResolver.toPotQuote(originalProperty, propertyPrefix)
//            rules.append(property).append(":{")
//            messages.append(property).append(":{")
//            for (anno in value) {
//                val converter = ConstraintConverterFactory.getInstance(anno)
//                val context = ConstraintConvertContext(originalProperty, property, propertyPrefix, beanClass)
//                val ruleResult = converter.convert(context)
//                rules.append(ruleResult.rule).append(",")
////                messages.append(ruleResult.msg).append(",")
//            }
//            val prop = beanClass.getMemberProperty(property)
//            if (prop.returnType == Array<Any>::class.starProjectedType || prop.returnType.isSubtypeOf(List::class.starProjectedType)) {
//                rules.append("type:'array'").append(",")
//            }
//            rules.deleteCharAt(rules.length - 1).append("},")
//            messages.deleteCharAt(messages.length - 1).append("},")
//        }
//        return MessageFormat.format(
//            RESULT_PATTERN,
//            rules.deleteCharAt(rules.length - 1),
//            messages.deleteCharAt(messages.length - 1)
//        )
//    }

    /**
     * 获取类级别的约束注解
     *
     * @param clazz Bean类
     * @return Map<属性名，List<约束注解>>
     */
    private fun getAnnotationsOnClass(clazz: KClass<*>): Map<String, MutableList<Annotation>> {
        val annotationMap = mutableMapOf<String, MutableList<Annotation>>()
        for (annotation in clazz.annotations) {
            val prop = annotation::class.getMemberProperty("properties")
            if (prop != null) {
                val propertyNames = prop.call(annotation) as Array<String>
                propertyNames.forEach { propertyName ->
                    var annoList = annotationMap[propertyName] ?: mutableListOf()
                    annotationMap[propertyName] = annoList
                    if (annotation.annotationClass.isAnnotationPresent(Constraint::class)) { // 是约束注解
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
                    if (annotation.annotationClass.isAnnotationPresent(Constraint::class)) { // 是约束注解
                        annotationList.add(annotation)
                    }
                }
            }
        }
        return annotationList
    }
}

fun main() {
    val superclasses = CompareConstraintJsConverter::class.superclasses
    superclasses.forEach { println(it.constructors.size) }
    print(superclasses)
}