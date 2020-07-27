package org.kuark.validation.constraints

import org.kuark.validation.constraints.validator.AtLeastValidator
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 至少需要(不为null)分组中的几个属性的验证规则注解 <br></br>
 * 注意： <br></br>
 * 1.该规则的分组类必须定义在当前Form类中，因为AtLeastValidator会从分组类名取所属Form的类名！ <br></br>
 * 2.必须在Form类上用GroupSequence注解指明分组验证的顺序(默认分组为当前Form的类，必须指定)，否则分组对应的规则都将被忽略！
 *
 * @author K
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = [AtLeastValidator::class])
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(RetentionPolicy.RUNTIME)
annotation class AtLeast(

    val message: String,

    /**
     * 分组类数组
     * 注意：该规则的分组类必须定义在当前Form类中，因为AtLeastValidator会从分组类名取所属Form的类名！
     * @return
     */
    val groups: Array<KClass<*>> = [],

    val payload: Array<KClass<out Payload>> = [],

    /**
     * 至少需要的个数
     *
     * @return 至少需要的个数
     */
    val count: Int = 1,

    /**
     * 获取javascript值的表达式，缺省为：$('[name="propertyName"]').val()
     * @return 获取javascript值的表达式
     */
    val jsValueExp: String = ""

) {
    @Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
    )
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    annotation class List(vararg val value: AtLeast)
}