package org.kuark.base.validation.constraint.annotaions

import org.kuark.base.validation.constraint.validator.RemoteValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 远程验证注解，支持对多个字段的校验
 *
 *
 * Create by (admin) on 2015/1/22.
 */
@MustBeDocumented
@Constraint(validatedBy = [RemoteValidator::class])
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Remote(

    val message: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],

    /**
     * 验证的类，一般为Spring MVC的Controller类
     *
     * @return 验证的类
     */
    val checkClass: KClass<*>, //! 方法名不能以valid开头，会报javax.validation.ConstraintDefinitionException: Parameters starting with 'valid' are not allowed in a constraint.
    /**
     * 验证方法名称，该方法的签名必须如下：
     * 返回值：布尔值的String表示
     * 参数：含有HttpServletRequest类型或用RequestParam注解标注参数名且参数类型为String的参数
     *
     * @return 方法名
     */
    val checkMethod: String,
    /**
     * 附加属性数组。当只需要对单个属性(本身)进行校验时，不需要指定。
     * 注：有值时，远程验证的数据是以POST方式提交的!
     *
     * @return 属性名数组
     */
    val additionalProperties: Array<String> = [],
    /**
     * 获取javascript值的表达式，缺省为：$('[name="propertyName"]').val()
     * @return 获取javascript值的表达式
     */
    val jsValueExp: Array<String> = [],
    /**
     * Form表单名称.
     * @return
     */
    val formName: String = "",
    /**
     * Form表单ID.
     * @return
     */
    val formId: String = "",
    /**
     * 表单元素类型: input,div等
     * @return
     */
    val type: String = ""
) {
    @Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
    )
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    annotation class List(vararg val value: Remote)
}