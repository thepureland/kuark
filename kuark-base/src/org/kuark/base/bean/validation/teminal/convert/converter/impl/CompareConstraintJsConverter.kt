package org.kuark.base.bean.validation.teminal.convert.converter.impl

import org.kuark.base.bean.validation.constraint.annotaions.Compare
import org.kuark.base.bean.validation.teminal.convert.converter.AbstractConstraintConverter
import java.io.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType

/**
 * Compare注解约束->终端约束的转换器
 *
 * @author K
 * @since 1.0.0
 */
class CompareConstraintJsConverter(annotation: Annotation) : Cloneable, AbstractConstraintConverter(annotation),Serializable {

    override fun getRule(constraintAnnotation: Annotation): MutableMap<String, Any> {
        val compare = constraintAnnotation as Compare
        val map = mutableMapOf<String, Any>()
        map["logic"] = compare.logic.toString()
        map["anotherProperty"] = compare.anotherProperty
        val beanClass = context.beanClass
        val returnType = beanClass.memberProperties.first { it.name == context.property }.returnType
        if (returnType.isSubtypeOf(Number::class.starProjectedType)) {
            map["isNumber"] = "true"
        }
//            val depends = compare.depends
//            if (depends.properties.isNotEmpty()) {
//                val dependsJsExp = DependsRuleConverter.Companion.createDependsJsExp(depends, context)
//                nestedMap["dependsOn"] = dependsJsExp
//            }
//            map["compare"] = nestedMap
        return map
    }

}