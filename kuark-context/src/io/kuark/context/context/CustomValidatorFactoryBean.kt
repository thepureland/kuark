package io.kuark.context.context

import io.kuark.context.kit.SpringKit
import org.hibernate.validator.HibernateValidatorConfiguration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import java.time.Clock
import javax.validation.ClockProvider
import javax.validation.Configuration
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorFactory


open class CustomValidatorFactoryBean: LocalValidatorFactoryBean() {

    override fun getClockProvider(): ClockProvider {
        return ClockProvider {
            Clock.systemDefaultZone()
        }
    }

    override fun postProcessConfiguration(configuration: Configuration<*>) {
        val hibernateConfiguration = configuration as HibernateValidatorConfiguration
        val constraintMapping = hibernateConfiguration.createConstraintMapping()
        val beans = SpringKit.getBeansOfType(IValidatorProvider::class).values
        beans.forEach { provider ->
            val validators = provider.provide<Annotation, ConstraintValidator<Annotation, *>>()
            validators.forEach { (constraint, validator) ->
                constraintMapping
                    .constraintDefinition(constraint.java)
                    .validatedBy(validator.java)
                    .includeExistingValidators(true)
            }
        }
        hibernateConfiguration.addMapping(constraintMapping)
    }

    override fun setConstraintValidatorFactory(constraintValidatorFactory: ConstraintValidatorFactory) {
        super.setConstraintValidatorFactory(constraintValidatorFactory)
    }


}