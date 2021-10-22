package io.kuark.base.bean.validation.constraint.annotaions

import io.kuark.base.bean.validation.constraint.validator.AtLeastValidator
import io.kuark.base.bean.validation.support.AssertLogic
import io.kuark.base.query.enums.Operator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * "至少需要几个"约束注解，类级别注解，将校验指定的属性至少有几个满足给定的断言逻辑
 *
 * @author K
 * @since 1.0.0
 */
@MustBeDocumented
@Constraint(validatedBy = [AtLeastValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AtLeast(

    /** 要校验的属性名 */
    val properties: Array<String>,

    /** 断言逻辑，默认为判断值是否不为null */
    val logic: AssertLogic = AssertLogic.IS_NOT_NULL,

    /** 值满足给定的断言逻辑的属性的至少个数 */
    val count: Int = 1,


    /**
     * 校验不通过时的提示，或其国际化key。
     * 每个约束定义中都包含有一个用于提示验证结果的消息模版, 并且在声明一个约束条件的时候,你可以通过这个约束中的message属性来重写默认的消息模版,
     * 如果在校验的时候,这个约束条件没有通过,那么你配置的MessageInterpolator会被用来当成解析器来解析这个约束中定义的消息模版,
     * 从而得到最终的验证失败提示信息. 这个解析器会尝试解析模版中的占位符( 大括号括起来的字符串 ).
     * 其中, Hibernate Validator中默认的解析器 (MessageInterpolator) 会先在类路径下找名称为ValidationMessages.properties的ResourceBundle,
     * 然后将占位符和这个文件中定义的resource进行匹配,如果匹配不成功的话,那么它会继续匹配Hibernate Validator自带的位于
     * /org/hibernate/validator/ValidationMessages.properties的ResourceBundle, 依次类推,递归的匹配所有的占位符.
     */
    val message: String = "{io.kuark.base.bean.validation.constraint.annotaions.AtLeast.message}",

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

    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    annotation class List(vararg val value: AtLeast)

}