package org.kuark.base.validation.support

import org.kuark.base.bean.BeanKit

/**
 * Depends约束验证器，由其他一级约束Validator调用
 *
 * @author K
 * @since 1.0.0
 */
object DependsValidator {

    /**
     * 此方法返回值为depends里面的表达式是否成立，true为成立，false为不成立，
     * 当depends里面的表达式成立时为必填，不成立时为非必填。
     *
     * @param depends 依赖注解
     * @param bean 待校验的Bean
     * @return 是否校验通过
     */
    fun validate(depends: Depends, bean: Any): Boolean {
        val properties = depends.property
        var result = true //默认为需要验证
        properties.forEachIndexed { index, property ->
            var v1 = BeanKit.getProperty(bean, property)
            var v2 = depends.value[index]
            if (v2.startsWith("[") && v2.endsWith("]")) {
                v2 = v2.replaceFirst("\\['".toRegex(), "")
                    .replaceFirst("'\\]".toRegex(), "")
                    .replace("',\\s*'".toRegex(), ",")
            }
            v1 = v1?.toString()
            val compare: Boolean = depends.operator[index].compare(v1, v2)
            if (depends.andOr === AndOr.AND) {
                if (!compare) {
                    return false
                }
            } else {
                if (compare) {
                    return true
                } else {
                    result = false
                }
            }
        }
        return result
    }

}