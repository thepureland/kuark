package org.kuark.base.validation

import org.hibernate.validator.group.GroupSequenceProvider
import org.kuark.base.validation.constraint.annotaions.DateTime
import java.time.LocalDate
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@GroupSequenceProvider(CustomGroupSequenceProvider::class)
class ValidationBean(
    @get:DateTime(format = "sdkfjsk")
    val name: String,

    @get:DecimalMin("1.0")
    val weight: Float,

    @get:NotNull
    val birthday: LocalDate?
)