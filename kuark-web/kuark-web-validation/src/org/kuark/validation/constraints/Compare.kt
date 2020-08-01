package org.kuark.validation.constraints

import org.kuark.validation.constraints.validator.CompareValidator
import org.kuark.validation.constraints.support.CompareLogic
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * Create by (admin) on 2015/1/22.
 */
@MustBeDocumented
@Constraint(validatedBy = [CompareValidator::class])
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Compare(

    val message: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],

    val logic: CompareLogic,
    /**
     * 待比较的另一个属性名
     *
     * @return 另一个属性名
     */
    val anotherProperty: String,
    /**
     * 比较的前提条件
     *
     * @return Depends
     */
    val depends: Depends = Depends(property = []),
    /**
     * compare的两个比较对象是否是数字，默认为""。会从compare所在的getter方法的方法值来自动设置这个值，如果已经人为指定，则以指定为准，
     * 自动设置的规则为：如果getter方法返回值是String则为"false"，如果返回值为Number类型，则为"true"。
     * 此值主要是为了前台js判断使用。
     * @return
     */
    val isNumber: String = ""
) {
    //	/**
    //	 * 多个表达式间的逻辑关系，默认为AND.
    //	 */
    //	AndOr andOr() default AndOr.AND;
    @Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
    )
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    annotation class List(vararg val value: Compare)
}