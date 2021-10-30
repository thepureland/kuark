package io.kuark.base.bean.validation.constraint.annotaions

import io.kuark.base.bean.validation.constraint.validator.ConstraintsValidator
import io.kuark.base.bean.validation.support.Depends
import io.kuark.base.bean.validation.support.IBeanValidator
import io.kuark.base.support.enums.IDictEnum
import io.kuark.base.support.logic.AndOr
import org.hibernate.validator.constraints.*
import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import kotlin.reflect.KClass

/**
 * 组合约束注解，可以实现以下目的：
 * 1.按定义的注解顺序校验(可以替代@GroupSequence和@GroupSequenceProvider)
 * 2.支持其中一个约束通过就算校验通过(AndOr.Or)
 *
 * 使用限制：
 * 1.尚不支持约束注解中的List规范
 *
 * @author K
 * @since 1.0.0
 */
@Constraint(validatedBy = [ConstraintsValidator::class])
@Target(
    AnnotationTarget.TYPE,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Repeatable
annotation class Constraints(

    /** 子约束的校验顺序 */
    val order: Array<KClass<out Annotation>> = [],

    /** 各约束间的逻辑关系，为AND时所有约束校验通过才算通过，为OR时只要其中一个约束校验通过就算通过 */
    val andOr: AndOr = AndOr.AND,

    // javax.validation定义的约束
    /** 逻辑假约束，被校验对象类型必须为Boolean，且值为false */
    val assertFalse: AssertFalse = AssertFalse(message = MESSAGE),
    /** 逻辑真约束，被校验对象类型必须为Boolean，且值为true */
    val assertTrue: AssertTrue = AssertTrue(message = MESSAGE),
    /**
     * 最大值约束，通过String值来定义，可以指定包括或不包括，
     * 被校验对象类型必须为以下之一或其子类：CharSequence、Double、Integer、Long、Float、Byte、Short、BigDecimal、BigInteger、Number、MonetaryAmount
     */
    val decimalMax: DecimalMax = DecimalMax("", message = MESSAGE),
    /** 
     * 最小值约束，通过String值来定义，可以指定包括或不包括，
     * 被校验对象类型必须为以下之一或其子类：CharSequence、Double、Integer、Long、Float、Byte、Short、BigDecimal、BigInteger、Number、MonetaryAmount
     */
    val decimalMin: DecimalMin = DecimalMin("", message = MESSAGE),
    /** 数值约束，可分别指定整数和小数部分位数，被校验对象类型必须为以下之一或其子类：CharSequence、Number、MonetaryAmount */
    val digits: Digits = Digits(integer = 0, fraction = 0, message = MESSAGE),
    /** 电子邮箱账号约束，被校验对象类型必须为CharSequence或其子类。如无特殊情况，regexp不需要指定 */
    val email: Email = Email(message = MESSAGE),
    /** 
     * 未来日期时间约束，被校验对象类型必须为以下之一或其子类：LocalDate、LocalDateTime、LocalTime、Instant、Calendar、Date、HijrahDate、
     * JapaneseDate、MinguoDate、MonthDay、OffsetDateTime、OffsetTime、ThaiBuddhistDate、Year、YearMonth、ZonedDateTime
     */
    val future: Future = Future(message = MESSAGE),
    /**
     * 未来或现在日期时间约束，被校验对象类型必须为以下之一或其子类：LocalDate、LocalDateTime、LocalTime、Instant、Calendar、Date、HijrahDate、
     * JapaneseDate、MinguoDate、MonthDay、OffsetDateTime、OffsetTime、ThaiBuddhistDate、Year、YearMonth、ZonedDateTime
     */
    val futureOrPresent: FutureOrPresent = FutureOrPresent(message = MESSAGE),
    /**
     * 最大值约束，通过long值来定义，被校验对象类型必须为以下之一或其子类：
     * CharSequence、Double、Integer、Long、Float、Byte、Short、BigDecimal、BigInteger、Number、MonetaryAmount
     */
    val max: Max = Max(0, message = MESSAGE),
    /**
     * 最小值约束，通过long值来定义，被校验对象类型必须为以下之一或其子类：
     * CharSequence、Double、Integer、Long、Float、Byte、Short、BigDecimal、BigInteger、Number、MonetaryAmount 
     */
    val min: Min = Min(0, message = MESSAGE),
    /** 负数约束，被校验对象类型必须为以下之一或其子类： CharSequence、Double、Integer、Long、Float、Byte、Short、BigDecimal、BigInteger、Number、MonetaryAmount */
    val negative: Negative = Negative(message = MESSAGE),
    /** 负数或零约束，被校验对象类型必须为以下之一或其子类： CharSequence、Double、Integer、Long、Float、Byte、Short、BigDecimal、BigInteger、Number、MonetaryAmount */
    val negativeOrZero: NegativeOrZero = NegativeOrZero(message = MESSAGE),
    /** 非空白约束，被校验对象类型必须为CharSequence或其子类 */
    val notBlank: NotBlank = NotBlank(message = MESSAGE),
    /**
     * 非空约束, 被校验对象类型必须为以下之一或其子类：
     * CharSequence、Array<*>、Collection<*>、DoubleArray、IntArray、LongArray、CharArray、FloatArray、BooleanArray、ByteArray、ShortArray、Map<*, *>
     */
    val notEmpty: NotEmpty = NotEmpty(message = MESSAGE),
    /** 非null约束，被校验对象可以是任何类型 */
    val notNull: NotNull = NotNull(message = MESSAGE),
    /** null约束，被校验对象可以是任何类型 */
    val beNull: Null = Null(message = MESSAGE),
    /**
     * 过去日期时间约束，被校验对象类型必须为以下之一或其子类：LocalDate、LocalDateTime、LocalTime、Instant、Calendar、Date、HijrahDate、
     * JapaneseDate、MinguoDate、MonthDay、OffsetDateTime、OffsetTime、ThaiBuddhistDate、Year、YearMonth、ZonedDateTime
     */
    val past: Past = Past(message = MESSAGE),
    /**
     * 过去或现在日期时间约束，被校验对象类型必须为以下之一或其子类：LocalDate、LocalDateTime、LocalTime、Instant、Calendar、Date、HijrahDate、
     * JapaneseDate、MinguoDate、MonthDay、OffsetDateTime、OffsetTime、ThaiBuddhistDate、Year、YearMonth、ZonedDateTime
     */
    val pastOrPresent: PastOrPresent = PastOrPresent(message = MESSAGE),
    /** 正则约束，被校验对象类型必须为CharSequence或其子类 */
    val pattern: Pattern = Pattern(regexp = "", message = MESSAGE),
    /** 正数约束，被校验对象类型必须为以下之一或其子类： CharSequence、Double、Integer、Long、Float、Byte、Short、BigDecimal、BigInteger、Number、MonetaryAmount */
    val positive: Positive = Positive(message = MESSAGE),
    /** 非负数约束，被校验对象类型必须为以下之一或其子类： CharSequence、Double、Integer、Long、Float、Byte、Short、BigDecimal、BigInteger、Number、MonetaryAmount */
    val positiveOrZero: PositiveOrZero = PositiveOrZero(message = MESSAGE),
    /**
     * 尺寸约束, 被校验对象类型必须为以下之一或其子类：
     * CharSequence、Array<*>、Collection<*>、DoubleArray、IntArray、LongArray、CharArray、FloatArray、BooleanArray、ByteArray、ShortArray、Map<*, *>
     */
    val size: Size = Size(message = MESSAGE),

    // hibernate定义的约束
    /** 字符串代码点长度(实际字符数)约束，被校验对象类型必须为CharSequence或其子类 */
    val codePointLength: CodePointLength = CodePointLength(message = MESSAGE),
    /** 信用卡号约束，被校验对象类型必须为CharSequence或其子类 */
    val creditCardNumber: CreditCardNumber = CreditCardNumber(message = MESSAGE),
    /** 货币金额约束，被校验对象类型必须为MonetaryAmount或其子类 */
    val currency: Currency = Currency(message = MESSAGE),
    /** EAN商品条码约束，被校验对象类型必须为CharSequence或其子类 */
    val ean: EAN = EAN(message = MESSAGE),
    /** 书本条码约束，被校验对象类型必须为CharSequence或其子类 */
    val isbn: ISBN = ISBN(message = MESSAGE),
    /** 字符串长度约束，被校验对象类型必须为CharSequence或其子类 */
    val length: Length = Length(message = MESSAGE),
    /** 字符串Luhn算法(模10算法)约束，可检测银行卡、信用卡。被校验对象类型必须为CharSequence或其子类 */
    val luhnCheck: LuhnCheck = LuhnCheck(message = MESSAGE),
    /** 字符串模10算法约束，可检测银行卡、信用卡。被校验对象类型必须为CharSequence或其子类 */
    val mod10Check: Mod10Check = Mod10Check(message = MESSAGE),
    /** 字符串模11算法约束，被校验对象类型必须为CharSequence或其子类 */
    val mod11Check: Mod11Check = Mod11Check(message = MESSAGE),
    /** 参数脚本断言约束，被校验对象类型必须为对象数组 */
    val parameterScriptAssert: ParameterScriptAssert = ParameterScriptAssert(lang = "", script = "", message = MESSAGE),
    /** 区间约束，被校验对象类型必须为以下之一或其子类： CharSequence、Double、Integer、Long、Float、Byte、Short、BigDecimal、BigInteger、Number、MonetaryAmount */
    val range: Range = Range(message = MESSAGE),
//    /**  */
//    val scriptAssert: ScriptAssert = ScriptAssert(lang = "", script = ""), //kuark暂不支持
    /** 集合元素惟一约束，被校验对象类型必须为Collection或其子类 */
    val uniqueElements: UniqueElements = UniqueElements(message = MESSAGE),
    /** 网址约束，被校验对象类型必须为CharSequence或其子类 */
    val url: URL = URL(message = MESSAGE),

    // kuark定义的约束
    /** "至少需要几个"约束，被校验对象可以为任何类型 */
    val atLeast: AtLeast = AtLeast([], message = MESSAGE),
    /** 中国居民身份证号约束，被校验对象类型必须为CharSequence或其子类 */
    val cnIdCardNo: CnIdCardNo = CnIdCardNo(message = MESSAGE),
    /** 比较约束，比较的两者对象类型都必须实现Comparable接口且类型相同，支持Array<*>类型，但是两个数组的大小必须一致，且数组每个元素必须实现Comparable接口 */
    val compare: Compare = Compare("", message = MESSAGE),
    /** 自定义逻辑的约束，被校验对象可以为任何类型 */
    val custom: Custom = Custom(IBeanValidator::class, message = MESSAGE),
    /** 字符串日期时间约束，被校验对象类型必须为CharSequence或其子类 */
    val dateTime: DateTime = DateTime("", message = MESSAGE),
    /** 字典枚举代码约束，被校验对象类型必须为CharSequence或其子类 */
    val dictEnumCode: DictEnumCode = DictEnumCode(IDictEnum::class, message = MESSAGE),
    /** 非null依赖约束，被校验对象可以为任何类型 */
    val notNullOn: NotNullOn = NotNullOn(Depends([]), message = MESSAGE),
    /** 数列约束，被校验对象类型必须为以下之一或其子类: List<*>、Array<*> */
    val series: Series = Series(message = MESSAGE),

//  val each: Each = Each(Constraints(), message = MESSAGE),  // 会循环引用，而且本身就是组合约束，没必要作为Constraints的子约束了
//  val exist: Exist = Exist(Constraints(), message = MESSAGE),  // 会循环引用，而且本身就是组合约束，没必要作为Constraints的子约束了


    /**
     * 错误提示消息，仅在andOr值为Or时有意义
     */
    val message: String = "io.kuark.base.bean.validation.constraint.annotaions.Constraints.message",

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

    companion object {
        const val MESSAGE = "TEMP_MSG" // 利用该值来确定用户定义了哪些子约束
    }

}