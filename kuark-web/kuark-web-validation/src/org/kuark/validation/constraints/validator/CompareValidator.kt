package org.kuark.validation.constraints.validator

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import org.kuark.base.bean.BeanKit
import org.kuark.base.lang.reflect.TypeKit
import org.kuark.base.log.LogFactory
import org.kuark.context.core.KuarkContext
import org.kuark.validation.constraints.Compare
import org.kuark.validation.constraints.Depends
import org.kuark.validation.constraints.support.CompareLogic
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * Create by (admin) on 2015/1/23.
 */
class CompareValidator : ConstraintValidator<Compare, Any?> {

    private lateinit var compare: Compare

    override fun initialize(compare: Compare) {
        this.compare = compare
    }

    override fun isValid(
        value: Any?,
        constraintValidatorContext: ConstraintValidatorContext
    ): Boolean {
        val depends: Depends = compare.depends
        if (depends != null && depends.property.isNotEmpty()) {
            if (!DependsValidator.Companion.pass(depends)) {
                return true
            }
        }
        val anotherProperty: String = compare.anotherProperty
        val body = KuarkContext.get().clientInfo.requestContentString
        var bodyParams: JSONObject = JSON.parseObject(body)
        var anotherValue: Any? = null
        if (anotherProperty.contains(".")) {
            val propertyPath = anotherProperty.split("\\.").toTypedArray()
            if (value != null) {
                for (i in propertyPath.indices) {
                    if (i == propertyPath.size - 1) {
                        val anotherValueStr: String = bodyParams.getString(propertyPath[i])
                        anotherValue = TypeKit.valueOf(anotherValueStr, value.javaClass)
                    } else {
                        bodyParams = bodyParams.getJSONObject(propertyPath[i])
                    }
                }
            }
        } else {
            val anotherValueStr: String = bodyParams.getString(anotherProperty)
            if (value != null) {
                anotherValue = TypeKit.valueOf(anotherValueStr, value.javaClass)
            }
        }
        return compare(value, anotherValue, compare.logic)
    }

    companion object {
        private val LOG = LogFactory.getLog(CompareValidator::class)

        // 以下代码为当compare有多个时的校验逻辑，现在没有多个compare加在同一属性上的场景，等有了再开启，开启时Compare，CompareRuleConverter，及前端脚本都还要完善
        //	@Override
        //	public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        //		String[] anotherOroperties = compare.anotherProperty();
        //		HttpServletRequest request = SpringTool.getRequest();
        //		for (int i = 0; i < anotherOroperties.length; i++) {
        //			String anotherProperty = anotherOroperties[i];
        //			Depends depends = compare.depends()[i];
        //			if (depends != null && ArrayTool.isNotEmpty(depends.property())) {
        //				boolean shouldCompare = DependsValidator.pass(depends); // depends不成立时，不需要进行compare，当作当前pass
        //				boolean comparePass = true;
        //				if (shouldCompare) {
        //					String parameter = request.getParameter(anotherProperty);
        //					Object anotherValue = null;
        //					if (value != null) {
        //						anotherValue = TypeTool.valueOfStr(parameter, value.getClass());
        //					}
        //					comparePass = compare(value, anotherValue, this.compare.logic()[i]);
        //				}
        //				AndOr andOr = this.compare.andOr();
        //				if (andOr == AndOr.AND) {
        //					if (!comparePass) { // 只要某个表达式不成立，就是比较失败
        //						return false;
        //					}
        //				} else {
        //					if (comparePass) { // 只要某个表达式成立，就是成功
        //						return true;
        //					}
        //				}
        //			}
        //		}
        //		return true;
        //	}

        fun getPropertyValue(`object`: Any?, property: String?): Any? {
            try {
                return BeanKit.getProperty(`object`, property)
            } catch (e: Exception) {
                LOG.error(e)
            }
            return null
        }

        fun compare(value: Any?, anotherValue: Any?, logic: CompareLogic?): Boolean {
            if (value == null && anotherValue == null) {
                return true
            }
            if (value == null || anotherValue == null) {
                return false
            }
            if (value.javaClass != anotherValue.javaClass) {
                return false
            }
            if (value is String) {
                when (logic) {
                    CompareLogic.EQ -> return value == anotherValue
                    CompareLogic.NE -> return value != anotherValue
                    CompareLogic.IEQ -> return value.equals(anotherValue as String?, ignoreCase = true)
                    CompareLogic.GT -> return value > (anotherValue as String?)!!
                    CompareLogic.GE -> return value >= (anotherValue as String?)!!
                    CompareLogic.LT -> return value < (anotherValue as String?)!!
                    CompareLogic.LE -> return value <= (anotherValue as String?)!!
                    else -> {
                    }
                }
            } else if (value is Number) {
                val v1: Double = value.toDouble()
                val v2: Double = (anotherValue as Number).toDouble()
                when (logic) {
                    CompareLogic.EQ -> return v1 == v2
                    CompareLogic.NE -> return v1 != v2
                    CompareLogic.GT -> return v1 > v2
                    CompareLogic.GE -> return v1 >= v2
                    CompareLogic.LT -> return v1 < v2
                    CompareLogic.LE -> return v1 <= v2
                    else -> {
                    }
                }
            }
            return false
        }
    }
}