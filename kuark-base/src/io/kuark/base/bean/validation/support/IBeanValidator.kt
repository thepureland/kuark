package io.kuark.base.bean.validation.support

/**
 * Bean验证器接口
 *
 * @param T bean类型
 * @author K
 * @since 1.0.0
 */
interface IBeanValidator<T> {

    /**
     * 执行校验
     *
     * @param bean 待验证的bean
     * @return 是否验证通过
     * @author K
     * @since 1.0.0
     */
    fun validate(bean: T): Boolean

}