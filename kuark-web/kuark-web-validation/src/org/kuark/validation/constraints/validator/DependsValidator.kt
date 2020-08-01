package org.kuark.validation.constraints.validator

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import org.kuark.context.core.KuarkContext
import org.kuark.validation.constraints.Depends
import org.kuark.validation.constraints.support.AndOr
import org.kuark.validation.constraints.support.FormPropertyConverter
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * 表单Depends规则验证器
 *
 * @author admin
 * @time 8/12/15 5:29 PM
 */
class DependsValidator : ConstraintValidator<Depends, Any?> {

    private lateinit var depends: Depends

    override fun initialize(depends: Depends) {
        this.depends = depends
    }

    override fun isValid(o: Any?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        return if (!pass(depends)) { //当pass返回结果为false时表示不需要验证，直接返回true
            true
        } else o != null
    }

    companion object {
        /**
         * 此方法返回值为depends里面的表达式是否成立，true为成立，false为不成立，
         * 当depends里面的表达式成立时为必填，不成立时为非必填。
         * @param depends
         * @return
         */
        fun pass(depends: Depends): Boolean {
            val properties: Array<String> = depends.property
            val body = KuarkContext.get().clientInfo.requestContentString
            val form: JSONObject = JSON.parseObject(body)
            var result = true //默认为需要验证
            for (i in properties.indices) {
                var property = properties[i]
                if (!property.contains(".")) {
                    property = FormPropertyConverter.toPot(property)
                }
                var v1 = ""
                if (form.containsKey(property)) {
                    v1 = form.getString(property)
                }
                var v2: String = depends.value.get(i)
                if (v2.startsWith("[") && v2.endsWith("]")) {
                    v2 = v2.replaceFirst("\\['".toRegex(), "").replaceFirst("'\\]".toRegex(), "")
                        .replace("',\\s*'".toRegex(), ",")
                }
                val compare: Boolean = depends.operator[i].compare(v1, v2)
                if (depends.andOr === AndOr.AND) {
                    if (compare) {
                        result = true
                    } else {
                        result = false
                        break
                    }
                } else {
                    if (compare) {
                        result = true
                        break
                    } else {
                        result = false
                    }
                }
            }
            return result
        }
    }
}