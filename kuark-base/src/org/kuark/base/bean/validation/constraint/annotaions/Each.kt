package org.kuark.base.bean.validation.constraint.annotaions

import org.kuark.base.bean.validation.support.ConstraintRule
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern


@Constraints(
    [ConstraintRule.NotNull, ConstraintRule.NotNull],
    notNull = NotNull(message = "kkkkkkkkkkkkk"),
    pattern = Pattern(regexp = "kjkjkj")
)
annotation class Each(

    val constraints: Constraints


)