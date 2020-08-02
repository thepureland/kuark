package org.kuark.base.validation.constraint.annotaions

import org.kuark.base.validation.support.CompareLogic
import org.kuark.base.validation.constraint.validator.CompareValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 比较约束注解（类级别）
 * 用于约束两个属性的值的逻辑关系，两个属性必须是相同类型的，且必须实现Comparable接口
 *
 * @author K
 * @since 1.0.0
 */
@MustBeDocumented
@Constraint(validatedBy = [CompareValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Compare(

    /** 比较的第一个属性(左操作数) */
    val firstProperty: String,

    /** 比较的第二个属性(右操作数) */
    val secondProperty: String,

    /** 比较的逻辑 */
    val logic: CompareLogic,

    /** 比较依赖的前提条件, 条件成立才进一步进行比较验证 */
    val depends: Depends = Depends(property = []),


    /** 校验不通过时的提示，或其国际化key */
    val message: String,

    /** 该校验规则所从属的分组类 */
    val groups: Array<KClass<*>> = [],

    /** 约束注解的有效负载(通常用来将一些元数据信息与该约束注解相关联，常用的一种情况是用负载表示验证结果的严重程度) */
    val payload: Array<KClass<out Payload>> = []

) {

    /**
     * 多值约束
     * 使用该约束注解时，Bean Validation 将 value 数组里面的每一个元素都处理为一个普通的约束注解，并对其进行验证，所有约束条件均符合时才会验证通过
     */
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    annotation class List(vararg val value: Compare)

}