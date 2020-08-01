package org.kuark.base.validation

import org.hibernate.validator.HibernateValidator
import org.junit.jupiter.api.Test
import javax.validation.Validation


internal class ValidationTest {

    @Test
    fun test() {
        val validatorFactory = Validation.byProvider(HibernateValidator::class.java)
                .configure()
                .failFast(true)
                .buildValidatorFactory()
        val validator = validatorFactory.validator

        val bean = ValidationBean("name", 0.0F, null)

        val violations = validator.validate(bean)
        println(violations)

    }

}