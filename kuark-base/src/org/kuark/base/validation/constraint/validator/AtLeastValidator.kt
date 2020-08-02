package org.kuark.base.validation.constraint.validator

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl
import org.hibernate.validator.internal.engine.path.PathImpl
import org.kuark.base.lang.reflect.MethodKit
import org.kuark.base.validation.constraint.annotaions.AtLeast
import org.kuark.base.validation.support.FormPropertyConverter
import java.lang.reflect.Method
import java.util.*
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.groups.Default
import kotlin.reflect.KClass

/**
 * @author admin
 * @time 8/14/15 4:45 PM
 */
class AtLeastValidator : ConstraintValidator<AtLeast, Any?> {

    private lateinit var atLeast: AtLeast

    override fun initialize(atLeast: AtLeast) {
        this.atLeast = atLeast
    }

    override fun isValid(o: Any?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        val pathImpl =
            (constraintValidatorContext as ConstraintValidatorContextImpl).constraintViolationCreationContexts[0].path
        return atLeast.count <= countNotNullValue(pathImpl)
    }

    private fun countNotNullValue(pathImpl: PathImpl): Int {
        val properties = fetchSameGroupProperties()
//        val body = KuarkContext.get().clientInfo.requestContentString
        val form: JSONObject = JSON.parseObject("body")
        var count = 0
        for (property in properties) {
            val paramName: String = FormPropertyConverter.toPot(property)
            if (paramName.contains("[]")) {
                //处理AtLeast应用在Form里面对单个属性是数组的验证场景,只支持一维数组，二维（含）以上不支持。
                for (j in 0 until form.getJSONArray(property).size) {
                    val parameter: String = form.getJSONArray(property).getString(j) ?: break
                    if (parameter.isNotBlank()) {
                        count++
                    }
                }
            } else {
                val parameter: String = form.getString(property)
                if (parameter.isNotBlank()) {
                    count++
                }
            }
        }
        return count
    }

    private fun fetchSameGroupProperties(): Set<String> {
        val properties: MutableSet<String> = HashSet()
        val formClass = parseFormClassByGroup()
        val readMethods: List<Method> = MethodKit.getReadMethods(formClass.kotlin)
        for (readMethod in readMethods) {
            if (readMethod.isAnnotationPresent(AtLeast::class.java)) {
                val annotation = readMethod.getAnnotation(AtLeast::class.java)
                if (annotation.groups.any { it in atLeast.groups }) {
                    properties.add(MethodKit.getReadProperty(readMethod)!!)
                }
            }
        }
        return properties
    }

    private fun parseFormClassByGroup(): Class<*> {
        val groups = atLeast.groups
        var groupClass: KClass<*>? = null
        for (group in groups) {
            if (group != Default::class.java) {
                groupClass = group
                break
            }
        }
        if (groupClass != null) {
            val name = groupClass.java.name
            val formClassName: String = name.substringBefore("$")
            return try {
                Class.forName(formClassName)
            } catch (e: ClassNotFoundException) {
                throw Exception(e)
            }
        }
        throw Exception("AtLeast注解的groups必须指定非Default的分组类！")
    }
}