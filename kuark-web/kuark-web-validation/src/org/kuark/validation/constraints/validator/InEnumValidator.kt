package org.kuark.validation.constraints.validator

import org.kuark.base.log.LogFactory
import org.kuark.validation.constraints.InEnum
import java.lang.reflect.Method
import java.util.*
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

/**
 * 实现参数枚举校验方法
 *
 * @author Simon
 */
class InEnumValidator : ConstraintValidator<InEnum, String?> {

    private var codeList: MutableList<String>? = null
    private var notNull: Boolean? = null

    override fun initialize(annotation: InEnum) {
        val cls: KClass<*> = annotation.value
        notNull = annotation.notNull
        if (cls is Enum<*>) {
            try {
                codeList = mutableListOf()
                val objects: Array<Any> = cls.java.enumConstants as Array<Any>
                val method =
                    Arrays.stream(cls.java.methods).filter { m: Method -> "getCode" == m.name }.findFirst().get()
                for (obj in objects) {
                    val code = method.invoke(obj)
                    codeList!!.add(code.toString())
                }
            } catch (e: Exception) {
                LOG.error("InEnumValidator ->  {0} not getCode Method ", cls)
            }
        }
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        /* 允许为为空 */
        if (!notNull!! && (value == null || value.isBlank())) {
            return true
        }
        if (value!!.isNotBlank()) {
            if (codeList != null && codeList!!.contains(value)) {
                return true
            }
        }

        // 校验不通过，自定义提示语句（因为，注解上的 value 是枚举类，无法获得枚举类的实际值）
        context.disableDefaultConstraintViolation() // 禁用默认的 message 的值
        // 重新添加错误提示语句
        context.buildConstraintViolationWithTemplate(
            context.defaultConstraintMessageTemplate
                .replace("\\{value}".toRegex(), codeList.toString())
        ).addConstraintViolation()
        return false
    }

    companion object {
        private val LOG = LogFactory.getLog(InEnumValidator::class)
    }
}