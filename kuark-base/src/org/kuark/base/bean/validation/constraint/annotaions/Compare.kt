package org.kuark.base.bean.validation.constraint.annotaions

import org.kuark.base.bean.validation.support.CompareLogic
import org.kuark.base.bean.validation.constraint.validator.CompareValidator
import org.kuark.base.bean.validation.support.Depends
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 比较约束注解，属性级别注解。两个属性的值必须满足指定的逻辑条件，可以指定比较依赖的前提条件。
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
@Repeatable
annotation class Compare(

    /** 比较的第二个属性(右操作数) */
    val anotherProperty: String,

    /** 比较的逻辑 */
    val logic: CompareLogic,

    /** 比较依赖的前提条件, 条件成立才进一步进行比较校验 */
    val depends: Depends = Depends(
        property = []
    ),


    /**
     * 校验不通过时的提示，或其国际化key。
     * 每个约束定义中都包含有一个用于提示验证结果的消息模版, 并且在声明一个约束条件的时候,你可以通过这个约束中的message属性来重写默认的消息模版,
     * 如果在校验的时候,这个约束条件没有通过,那么你配置的MessageInterpolator会被用来当成解析器来解析这个约束中定义的消息模版,
     * 从而得到最终的验证失败提示信息. 这个解析器会尝试解析模版中的占位符( 大括号括起来的字符串 ).
     * 其中, Hibernate Validator中默认的解析器 (MessageInterpolator) 会先在类路径下找名称为ValidationMessages.properties的ResourceBundle,
     * 然后将占位符和这个文件中定义的resource进行匹配,如果匹配不成功的话,那么它会继续匹配Hibernate Validator自带的位于
     * /org/hibernate/validator/ValidationMessages.properties的ResourceBundle, 依次类推,递归的匹配所有的占位符.
     */
    val message: String,

    /**
     * 该校验规则所从属的分组类，通过分组可以过滤校验规则或排序校验顺序。默认值必须是空数组。
     * 校验组能够让你在验证的时候选择应用哪些约束条件. 这样在某些情况下( 例如向导 ) 就可以对每一步进行校验的时候, 选取对应这步的那些约束条件进行验证了.
     * 校验组是通过可变参数传递给validate, validateProperty 和 validateValue的.如果某个约束条件属于多个组,那么各个组在校验时候的顺序是不可预知的.
     * 如果一个约束条件没有被指明属于哪个组,那么它就会被归类到默认组(javax.validation.groups.Default).
     * @GroupSequence 定义组别之间校验的顺序，使用注意事项：
     * 1.作用于类上时,不能包含javax.validation.groups.Default::class分组，作用于接口上可以
     * 2.作用于类上时,不能没有待验证的Bean的Class的分组
     * @GroupSequenceProvider 根据对象状态动态重定义默认分组，实现类返回的分组必须包含待验证的Bean的Class的分组(因为如果`Default`组对T进行验证，
     * 则实际验证的实例将传递给此类以确定默认组序列)。
     * 注：在使用组序列验证的时候，如果序列前边的组验证失败，则后面的组将不再给予验证！
     * 注：同一分组间的约束校验是无序的
     */
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