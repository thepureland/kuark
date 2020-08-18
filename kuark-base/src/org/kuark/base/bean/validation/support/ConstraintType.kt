package org.kuark.base.bean.validation.support

import kotlin.reflect.KClass

/**
 * Kuark支持的所有约束类型枚举
 *
 * @author K
 * @since 1.0.0
 */
enum class ConstraintType(val annotationClass: KClass<out Annotation>) {

    // javax.validation定义的约束
    AssertFalse(javax.validation.constraints.AssertFalse::class),
    AssertTrue(javax.validation.constraints.AssertTrue::class),
    DecimalMax(javax.validation.constraints.DecimalMax::class),
    DecimalMin(javax.validation.constraints.DecimalMin::class),
    Digits(javax.validation.constraints.Digits::class),
    Email(javax.validation.constraints.Email::class),
    Future(javax.validation.constraints.Future::class),
    FutureOrPresent(javax.validation.constraints.FutureOrPresent::class),
    Max(javax.validation.constraints.Max::class),
    Min(javax.validation.constraints.Min::class),
    Negative(javax.validation.constraints.Negative::class),
    NegativeOrZero(javax.validation.constraints.NegativeOrZero::class),
    NotBlank(javax.validation.constraints.NotBlank::class),
    NotEmpty(javax.validation.constraints.NotEmpty::class),
    NotNull(javax.validation.constraints.NotNull::class),
    Null(javax.validation.constraints.Null::class),
    Past(javax.validation.constraints.Past::class),
    PastOrPresent(javax.validation.constraints.PastOrPresent::class),
    Pattern(javax.validation.constraints.Pattern::class),
    Positive(javax.validation.constraints.Positive::class),
    PositiveOrZero(javax.validation.constraints.PositiveOrZero::class),
    Size(javax.validation.constraints.Size::class),

    // hibernate定义的约束
    CodePointLength(org.hibernate.validator.constraints.CodePointLength::class),
    CreditCardNumber(org.hibernate.validator.constraints.CreditCardNumber::class),
    Currency(org.hibernate.validator.constraints.Currency::class),
    EAN(org.hibernate.validator.constraints.EAN::class),
    ISBN(org.hibernate.validator.constraints.ISBN::class),
    Length(org.hibernate.validator.constraints.Length::class),
    LuhnCheck(org.hibernate.validator.constraints.LuhnCheck::class),
    Mod10Check(org.hibernate.validator.constraints.Mod10Check::class),
    Mod11Check(org.hibernate.validator.constraints.Mod11Check::class),
    ParameterScriptAssert(org.hibernate.validator.constraints.ParameterScriptAssert::class),
    Range(org.hibernate.validator.constraints.Range::class),
    ScriptAssert(org.hibernate.validator.constraints.ScriptAssert::class),
    UniqueElements(org.hibernate.validator.constraints.UniqueElements::class),
    URL(org.hibernate.validator.constraints.URL::class),

    // kuark定义的约束
    AtLeast(org.kuark.base.bean.validation.constraint.annotaions.AtLeast::class),
    CnIdCardNo(org.kuark.base.bean.validation.constraint.annotaions.CnIdCardNo::class),
    Compare(org.kuark.base.bean.validation.constraint.annotaions.Compare::class),
    CustomConstraint(org.kuark.base.bean.validation.constraint.annotaions.CustomConstraint::class),
    DateTime(org.kuark.base.bean.validation.constraint.annotaions.DateTime::class),
    DictEnumCode(org.kuark.base.bean.validation.constraint.annotaions.DictEnumCode::class),
    Each(org.kuark.base.bean.validation.constraint.annotaions.Each::class),
    NotNullOn(org.kuark.base.bean.validation.constraint.annotaions.NotNullOn::class),
    Series(org.kuark.base.bean.validation.constraint.annotaions.Series::class),
}