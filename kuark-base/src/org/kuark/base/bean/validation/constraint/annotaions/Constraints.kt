package org.kuark.base.bean.validation.constraint.annotaions

import org.kuark.base.bean.validation.support.ConstraintRule
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern


annotation class Constraints(

    val values: Array<ConstraintRule>,

    val notNull: NotNull = NotNull(),

    val notEmpty: NotEmpty = NotEmpty(),

    val pattern: Pattern = Pattern(regexp = "")

)