package org.kuark.base.bean.validation.constraint.annotaions

import org.hibernate.validator.constraints.*
import org.kuark.base.bean.validation.constraint.validator.ConstraintsValidator
import org.kuark.base.bean.validation.support.ConstraintType
import org.kuark.base.bean.validation.support.Depends
import org.kuark.base.bean.validation.support.IBeanValidator
import org.kuark.base.support.enums.IDictEnum
import org.kuark.base.support.logic.AndOr
import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import kotlin.reflect.KClass

/**
 * 组合约束注解，可以实现以下目的：
 * 1.按定义的注解顺序校验
 * 2.支持其中一个约束通过就算校验通过
 *
 * 使用限制：
 * 1.尚不支持约束注解中的List规范
 *
 * @author K
 * @since 1.0.0
 */
@Constraint(validatedBy = [ConstraintsValidator::class])
@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Repeatable
annotation class Constraints(

    val values: Array<ConstraintType>,

    val andOr: AndOr = AndOr.AND,

    // javax.validation定义的约束
    val assertFalse: AssertFalse = AssertFalse(),
    val assertTrue: AssertTrue = AssertTrue(),
    val decimalMax: DecimalMax = DecimalMax(""),
    val decimalMin: DecimalMin = DecimalMin(""),
    val digits: Digits = Digits(integer = 0, fraction = 0),
    val email: Email = Email(),
    val future: Future = Future(),
    val futureOrPresent: FutureOrPresent = FutureOrPresent(),
    val max: Max = Max(0),
    val min: Min = Min(0),
    val negative: Negative = Negative(),
    val negativeOrZero: NegativeOrZero = NegativeOrZero(),
    val notBlank: NotBlank = NotBlank(),
    val notEmpty: NotEmpty = NotEmpty(),
    val notNull: NotNull = NotNull(),
    val beNull: Null = Null(),
    val past: Past = Past(),
    val pastOrPresent: PastOrPresent = PastOrPresent(),
    val pattern: Pattern = Pattern(regexp = ""),
    val positive: Positive = Positive(),
    val positiveOrZero: PositiveOrZero = PositiveOrZero(),
    val size: Size = Size(),

    // hibernate定义的约束
    val codePointLength: CodePointLength = CodePointLength(),
    val creditCardNumber: CreditCardNumber = CreditCardNumber(),
//    val currency: Currency = Currency(), //kuark暂不支持
    val ean: EAN = EAN(),
    val isbn: ISBN = ISBN(),
    val length: Length = Length(),
    val luhnCheck: LuhnCheck = LuhnCheck(),
    val mod10Check: Mod10Check = Mod10Check(),
    val mod11Check: Mod11Check = Mod11Check(),
    val parameterScriptAssert: ParameterScriptAssert = ParameterScriptAssert(lang = "", script = ""),
    val range: Range = Range(),
    val scriptAssert: ScriptAssert = ScriptAssert(lang = "", script = ""),
    val uniqueElements: UniqueElements = UniqueElements(),
    val url: URL = URL(),

    // kuark定义的约束
    val atLeast: AtLeast = AtLeast([]),
    val cnIdCardNo: CnIdCardNo = CnIdCardNo(),
    val compare: Compare = Compare(""),
    val customConstraint: CustomConstraint = CustomConstraint(IBeanValidator::class),
    val dateTime: DateTime = DateTime(""),
    val dictEnumCode: DictEnumCode = DictEnumCode(IDictEnum::class),
//    val each: Each = Each(Constraints([])),
    val notNullOn: NotNullOn = NotNullOn(Depends([])),
    val series: Series = Series(),


    /**
     * 错误提示消息，仅在andOr值为Or时有意义
     */
    val message: String = "org.kuark.base.bean.validation.constraint.annotaions.Constraints.message",

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

)