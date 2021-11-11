package io.kuark.context.config

import io.kuark.base.bean.validation.support.ValidationContext
import io.kuark.context.context.CustomConstraintValidatorFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean


@Configuration
open class ValidationConfiguration {

    @Bean
    open fun defaultValidator(): LocalValidatorFactoryBean {
        val validator = CustomConstraintValidatorFactory()
        ValidationContext.validator = validator
        return validator
    }

}