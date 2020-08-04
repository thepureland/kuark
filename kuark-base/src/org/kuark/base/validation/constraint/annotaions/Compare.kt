package org.kuark.base.validation.constraint.annotaions

import org.kuark.base.validation.support.CompareLogic
import org.kuark.base.validation.constraint.validator.CompareValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 比较约束注解，两个属性的值必须满足指定的逻辑条件，可以指定比较依赖的前提条件。
 *
 * 用于约束两个属性的值的逻辑关系，两个属性必须是相同类型的，且都必须实现Comparable接口。
 * 属性支持Array<*>类型，但是两个数组的大小必须一致，且数组每个元素必须实现Comparable接口，
 * 此时，两个数组相同索引对应的值作比较，全部满足约束才算最终满足约束。
 * 属性不支持IntArray，CharArray等类型。
 *
 * @author K
 * @since 1.0.0
 */
@Constraint(validatedBy = [CompareValidator::class])
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Compare(

    /** 比较的第二个属性(右操作数) */
    val anotherProperty: String,

    /** 比较的逻辑 */
    val logic: CompareLogic,

    /** 比较依赖的前提条件, 条件成立才进一步进行比较校验 */
    val depends: Depends = Depends(property = []),


    /** 校验不通过时的提示，或其国际化key */
    val message: String,

    /** 该校验规则所从属的分组类，通过分组可以过滤校验规则或排序校验顺序。默认值必须是空数组。 */
    val groups: Array<KClass<*>> = [],

    /** 约束注解的有效负载(通常用来将一些元数据信息与该约束注解相关联，常用的一种情况是用负载表示验证结果的严重程度) */
    val payload: Array<KClass<out Payload>> = []

) {

    /**
     * 多个Compare约束
     * 使用该约束注解时，Bean Validation 将 Compare 数组里面的每一个元素都处理为一个普通的约束注解，并对其进行验证，所有约束条件均符合时才会验证通过
     */
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    annotation class List(vararg val value: Compare)

}